using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class AlimentoEndPoint
{
    public static void MapAlimentoEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/alimentos");

        rotas.MapGet("/", async (AppDbContext bd) =>
            await bd.Alimento.ToListAsync()
        );

        rotas.MapGet("/{categoria}", async (string categoria, AppDbContext bd) =>
        {
            var resultados = await bd.Alimento
                .Where(a => a.categoria.nome.ToLower() == categoria.ToLower())
                .ToListAsync();
                
            return resultados.Any() ? Results.Ok(resultados) : Results.NotFound();
        });

        rotas.MapGet("/doador/{nomeDoador}", async (string nomeDoador, AppDbContext bd) =>
        {
            var alimentos = await bd.Alimento
                .Include(a => a.doador)
                    .ThenInclude(d => d.usuarioDoador)

                .Where(a => a.doador.usuarioDoador.nome.ToLower() == nomeDoador.ToLower())
                .ToListAsync();

            return alimentos.Any() 
                ? Results.Ok(alimentos) 
                : Results.NotFound($"Nenhum alimento encontrado para o doador: {nomeDoador}");
        });

        // talvez fazer filtro por distancia

        rotas.MapPost("/doador/{nomeDoador}", async (Alimento novoAlimento, string nomeDoador, AppDbContext bd) => {
            var doador = await bd.Doador
                .Include(d => d.usuarioDoador)
                .FirstOrDefaultAsync(d => d.usuarioDoador.nome.ToLower() == nomeDoador.ToLower());

            if (doador == null)         
                return Results.NotFound($"Doador '{nomeDoador}' não encontrado.");

            var jaExiste = await bd.Alimento.AnyAsync(a => 
                a.idDoador == doador.idDoador && 
                a.nome.ToLower() == novoAlimento.nome.ToLower());

            if (jaExiste)
                return Results.Conflict($"O doador {nomeDoador} já possui um alimento cadastrado com o nome '{novoAlimento.nome}'.");

            novoAlimento.idDoador = doador.idDoador;
            bd.Alimento.Add(novoAlimento);
            await bd.SaveChangesAsync();

            return Results.Created($"/alimentos/{novoAlimento.idAlimento}", new 
            { 
                id = novoAlimento.idAlimento, 
                nome = novoAlimento.nome,
                quantidade = novoAlimento.qntd,
                mensagem = "Alimento cadastrado com sucesso e disponível para doação!" 
            });
        });

        rotas.MapPut("/{id:int}", async (int id, Alimento alimentoAlterado, AppDbContext bd) =>
        {
            var alimento = await bd.Alimento.FindAsync(id);


            if (alimento is null) return Results.NotFound("Alimento não encontrado.");

            alimento.nome = alimentoAlterado.nome;
            alimento.descricao = alimentoAlterado.descricao;
            alimento.qntd = alimentoAlterado.qntd;
            alimento.validade = alimentoAlterado.validade;
            alimento.idCategoria = alimentoAlterado.idCategoria;

            await bd.SaveChangesAsync();

            return Results.Ok(new { mensagem = "Alimento alterado com sucesso!" });
        });

        rotas.MapDelete("/{id:int}", async (int id, AppDbContext bd) =>
        {
            var alimento = await bd.Alimento.FindAsync(id);
            
            if (alimento is not null)
            {
                bd.Alimento.Remove(alimento);
                await bd.SaveChangesAsync();
                return Results.Ok(new { mensagem = "Alimento removido com sucesso!" });
            }

            return Results.NotFound("Alimento não encontrado.");
        });
    }
}