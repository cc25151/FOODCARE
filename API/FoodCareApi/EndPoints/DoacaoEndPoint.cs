using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class DoacaoEndPoint
{
    public static void MapDoacaoEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/doacoes");

        // 1. GET - Listar todos
        // Retorna o histórico completo de doações registradas na base de dados, trazendo os dados do doador e do alimento
        rotas.MapGet("/", async (AppDbContext bd) =>
            await bd.Doacao
                .Include(d => d.doador)
                .Include(d => d.alimento)
                .ToListAsync()
        );

        // 2. GET Realizadas - Listar doações finalizadas
        // Filtra e retorna as doações que já possuem uma nota de avaliação maior que zero
        rotas.MapGet("/realizadas", async (AppDbContext bd) =>
            await bd.Doacao
                .Where(d => d.avaliacao > 0)
                .Include(d => d.alimento)
                .ToListAsync()
        );

        // 3. GET Pendentes - Listar doações pendentes
        // Retorna as doações aguardando conclusão na plataforma, identificadas pela avaliação zerada
        rotas.MapGet("/pendentes", async (AppDbContext bd) =>
            await bd.Doacao
                .Where(d => d.avaliacao == 0)
                .Include(d => d.alimento)
                .ToListAsync()
        );

        // 4. GET por Doador - Consultar doações do doador
        // Busca e retorna todas as doações associadas ao ID específico do doador informado
        rotas.MapGet("/doador/{idDoador:int}", async (int idDoador, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.idDoador == idDoador)
                .Include(d => d.alimento)
                .ToListAsync();

            return doacoes.Any() ? Results.Ok(doacoes) : Results.NotFound();
        });

        // 5. GET por Avaliação - Filtrar por nota
        // Consulta o banco de dados filtrando os registros que receberam uma nota de avaliação específica
        rotas.MapGet("/avaliacao/{nota:int}", async (int nota, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.avaliacao == nota)
                .Include(d => d.doador)
                .ToListAsync();

            return Results.Ok(doacoes);
        });

        // 6. GET por Data - Consultar por dia específico
        // Converte a propriedade para DateTime para poder realizar a comparação de datas ignorando o horário
        rotas.MapGet("/data/{data:DateTime}", async (DateTime data, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.dataDoacao.ToDateTime(TimeOnly.MinValue).Date == data.Date)
                .Include(d => d.alimento)
                .ToListAsync();

            return Results.Ok(doacoes);
        });

        // 7. GET por Período - Consultar intervalo de datas
        // Filtra os registros que se enquadram entre a data inicial e final enviadas por parâmetro
        rotas.MapGet("/periodo", async (DateTime inicio, DateTime fim, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.dataDoacao >= DateOnly.FromDateTime(inicio) && d.dataDoacao <= DateOnly.FromDateTime(fim))
                .Include(d => d.alimento)
                .ToListAsync();

            return Results.Ok(doacoes);
        });

        // 8. POST - Iniciar Nova Doação
        // Cria a intenção de doação associando o doador ao alimento, forçando a avaliação inicial como pendente (zero)
        rotas.MapPost("/", async (Doacao novaDoacao, AppDbContext bd) =>
        {
            // Garante que o doador que está tentando registrar a doação realmente existe
            var doador = await bd.Doador.AnyAsync(d => d.idDoador == novaDoacao.idDoador);
            if (!doador) return Results.NotFound();

            // Validação de segurança: verifica se o alimento existe e se pertence de fato ao doador informado
            var alimento = await bd.Alimento.FirstOrDefaultAsync(a => 
                a.idAlimento == novaDoacao.idAlimento && a.idDoador == novaDoacao.idDoador);

            if (alimento == null) return Results.BadRequest();

            novaDoacao.avaliacao = 0; // Inicia obrigatoriamente sem nota (Pendente)
            
            // Caso o front-end não envie uma data, registra automaticamente a data atual do servidor
            if (novaDoacao.dataDoacao == default)
                novaDoacao.dataDoacao = DateOnly.FromDateTime(DateTime.Now);

            bd.Doacao.Add(novaDoacao);
            await bd.SaveChangesAsync();

            return Results.Created($"/doacoes/{novaDoacao.idDoacao}", new
            {
                id = novaDoacao.idDoacao,
                alimento = alimento.nome,
                status = "Pendente"
            });
        });

        // 9. PATCH - Finalizar Doação com avaliação
        // O EndPoint é chamado quando o beneficiário conclui a retirada e atribui uma nota de 1 a 5 para a doação
        rotas.MapPatch("/{id:int}/finalizar", async (int id, int nota, AppDbContext bd) =>
        {
            var doacao = await bd.Doacao.FindAsync(id);

            if (doacao is null) return Results.NotFound();
            
            // Impede notas fora do intervalo permitido de 1 a 5 estrelas
            if (nota <= 0 || nota > 5) return Results.BadRequest();

            doacao.avaliacao = nota; // Atualiza a nota, movendo a doação de 'pendente' para 'realizada'
            await bd.SaveChangesAsync();

            return Results.Ok();
        });
    }
}