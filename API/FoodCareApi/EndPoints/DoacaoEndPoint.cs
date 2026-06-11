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
        // Cruzamos as tabelas manualmente para pegar os nomes dos usuários sem depender de navegações complexas no Receptor
        rotas.MapGet("/", async (AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Include(d => d.doador).ThenInclude(doad => doad.usuario)
                .Include(d => d.receptor)
                .Include(d => d.alimento)
                .ToListAsync();

            // Buscamos a lista de usuários para mapear o nome do receptor sem precisar do objeto interno
            var usuarios = await bd.Usuario.ToListAsync();

            var resultado = doacoes.Select(d => new
            {
                d.idDoacao,
                d.dataDoacao,
                d.horario,
                d.avaliacao,
                d.status,
                doadorNome = d.doador?.usuario?.nome ?? "Não informado",
                // Busca o nome do usuário na tabela de usuários que tenha o mesmo idUsuario cadastrado no receptor
                receptorNome = usuarios.FirstOrDefault(u => u.idUsuario == d.receptor.idUsuario)?.nome ?? "Não informado",
                alimentoNome = d.alimento?.nome ?? "Não informado"
            }).ToList();

            return Results.Ok(resultado);
        });

        // 2. GET Realizadas - Filtrado por Status
        rotas.MapGet("/realizadas", async (AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.status.ToLower() == "finalizada")
                .Include(d => d.alimento)
                .ToListAsync();

            var resultado = doacoes.Select(d => new
            {
                d.idDoacao,
                d.dataDoacao,
                d.horario,
                d.avaliacao,
                d.status,
                alimentoNome = d.alimento?.nome ?? "Não informado"
            }).ToList();

            return Results.Ok(resultado);
        });

        // 3. GET Pendentes - Filtrado por Status
        rotas.MapGet("/pendentes", async (AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.status.ToLower() == "pendente")
                .Include(d => d.alimento)
                .ToListAsync();

            var resultado = doacoes.Select(d => new
            {
                d.idDoacao,
                d.dataDoacao,
                d.horario,
                d.avaliacao,
                d.status,
                alimentoNome = d.alimento?.nome ?? "Não informado"
            }).ToList();

            return Results.Ok(resultado);
        });

        // 4. GET por Doador - Consultar doações de um doador específico
        rotas.MapGet("/doador/{idDoador:int}", async (int idDoador, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.idDoador == idDoador)
                .Include(d => d.alimento)
                .ToListAsync();

            if (!doacoes.Any()) 
                return Results.NotFound("Nenhuma doação encontrada para este doador.");

            var resultado = doacoes.Select(d => new
            {
                d.idDoacao,
                d.dataDoacao,
                d.horario,
                d.avaliacao,
                d.status,
                alimentoNome = d.alimento?.nome ?? "Não informado"
            }).ToList();

            return Results.Ok(resultado);
        });

        // 8. POST - Iniciar Nova Doação
        rotas.MapPost("/", async (Doacao novaDoacao, AppDbContext bd) =>
        {
            var doadorExiste = await bd.Doador.AnyAsync(d => d.idDoador == novaDoacao.idDoador);
            if (!doadorExiste) 
                return Results.NotFound("Doador não encontrado na base de dados.");

            var receptorExiste = await bd.Receptor.AnyAsync(r => r.idReceptor == novaDoacao.idReceptor);
            if (!receptorExiste)
                return Results.NotFound("Receptor não encontrado na base de dados.");

            var alimento = await bd.Alimento.FirstOrDefaultAsync(a => 
                a.idAlimento == novaDoacao.idAlimento && a.idDoador == novaDoacao.idDoador);

            if (alimento == null) 
                return Results.BadRequest("Alimento inválido ou não pertence a este doador.");

            novaDoacao.avaliacao = 0; 
            novaDoacao.status = "Pendente"; // Garante o status inicial correto
            
            if (novaDoacao.dataDoacao == default) novaDoacao.dataDoacao = DateOnly.FromDateTime(DateTime.Now);
            if (novaDoacao.horario == default) novaDoacao.horario = TimeOnly.FromDateTime(DateTime.Now);

            bd.Doacao.Add(novaDoacao);
            await bd.SaveChangesAsync();

            return Results.Created($"/doacoes/{novaDoacao.idDoacao}", new
            {
                id = novaDoacao.idDoacao,
                alimento = alimento.nome,
                status = novaDoacao.status,
                mensagem = "Doação registrada com sucesso!"
            });
        });

        // 9. PATCH - Finalizar Doação com avaliação e mudança de Status
        rotas.MapPatch("/{id:int}/finalizar", async (int id, int nota, AppDbContext bd) =>
        {
            var doacao = await bd.Doacao.FindAsync(id);

            if (doacao is null) 
                return Results.NotFound("Doação não encontrada.");
            
            if (nota < 1 || nota > 5) 
                return Results.BadRequest("A nota de avaliação deve estar entre 1 e 5 estrelas.");

            doacao.avaliacao = nota; 
            doacao.status = "Finalizada"; // Atualiza para finalizada junto com a nota
            
            await bd.SaveChangesAsync();

            return Results.Ok(new { mensagem = "Doação finalizada e avaliada com sucesso!" });
        });
    }
}