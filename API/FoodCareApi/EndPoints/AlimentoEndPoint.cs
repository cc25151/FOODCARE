using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class AlimentoEndPoint
{
    public static void mapAlimentoEndPoints(this WebApplication app)
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

                .Where(a => a.doador.usuarioDoador.nome == nomeDoador)
                .ToListAsync();

            return alimentos.Any() 
                ? Results.Ok(alimentos) 
                : Results.NotFound($"Nenhum alimento encontrado para o doador: {nomeDoador}");
        });
    }
}