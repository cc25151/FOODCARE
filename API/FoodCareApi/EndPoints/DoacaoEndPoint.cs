using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class DoacaoEndPoint
{
    public static void MapDoacaoEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/doacoes");

        // get geral
        rotas.MapGet("/", async (AppDbContext bd) =>
            await bd.Doacao
                .Include(d => d.Doador)
                    .ThenInclude(u => u.usuarioDoador)
                .Include(d => d.Alimento)
                .ToListAsync()
        );

        // get das doacoes finalizadas
        rotas.MapGet("/realizadas", async (AppDbContext bd) =>
            await bd.Doacao
                .Where(d => d.Avaliacao > 0)
                .Include(d => d.Alimento)
                .ToListAsync()
        );

        // get das doacoes pendentes
        rotas.MapGet("/pendentes", async (AppDbContext bd) =>
            await bd.Doacao
                .Where(d => d.Avaliacao == 0)
                .Include(d => d.Alimento)
                .ToListAsync()
        );

        // get das doacoes por doador
        rotas.MapGet("/doador/{idDoador:int}", async (int idDoador, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.IdDoador == idDoador)
                .Include(d => d.Alimento)
                .ToListAsync();

            return doacoes.Any() ? Results.Ok(doacoes) : Results.NotFound();
        });

        // doacoes por avaliacao
        rotas.MapGet("/avaliacao/{nota:int}", async (int nota, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.Avaliacao == nota)
                .Include(d => d.Doador)
                .ToListAsync();

            return Results.Ok(doacoes);
        });

        // doacoes por data especifica
        rotas.MapGet("/data/{data:DateTime}", async (DateTime data, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.DataDoacao.ToDateTime(TimeOnly.MinValue).Date == data.Date)
                .Include(d => d.Alimento)
                .ToListAsync();

            return Results.Ok(doacoes);
        });

        // doacoes por periodo de tempo
        rotas.MapGet("/periodo", async (DateTime inicio, DateTime fim, AppDbContext bd) =>
        {
            var doacoes = await bd.Doacao
                .Where(d => d.DataDoacao >= DateOnly.FromDateTime(inicio) && d.DataDoacao <= DateOnly.FromDateTime(fim))
                .Include(d => d.Alimento)
                .ToListAsync();

            return Results.Ok(doacoes);
        });

        rotas.MapPost("/", async (Doacao novaDoacao, AppDbContext bd) =>
        {
            var doador = await bd.Doador.AnyAsync(d => d.idDoador == novaDoacao.IdDoador);
            if (!doador) return Results.NotFound();

            var alimento = await bd.Alimento.FirstOrDefaultAsync(a => 
                a.idAlimento == novaDoacao.IdAlimento && a.idDoador == novaDoacao.IdDoador);

            if (alimento == null) return Results.BadRequest();

            novaDoacao.Avaliacao = 0;
            
            if (novaDoacao.DataDoacao == default)
                novaDoacao.DataDoacao = DateOnly.FromDateTime(DateTime.Now);

            bd.Doacao.Add(novaDoacao);
            await bd.SaveChangesAsync();

            return Results.Created($"/doacoes/{novaDoacao.IdDoacao}", new
            {
                id = novaDoacao.IdDoacao,
                alimento = alimento.nome,
                status = "Pendente"
            });
        });

        rotas.MapPatch("/{id:int}/finalizar", async (int id, int nota, AppDbContext bd) =>
        {
            var doacao = await bd.Doacao.FindAsync(id);

            if (doacao is null) return Results.NotFound();
            if (nota <= 0 || nota > 5) return Results.BadRequest();

            doacao.Avaliacao = nota; 
            await bd.SaveChangesAsync();

            return Results.Ok();
        });
    }
}