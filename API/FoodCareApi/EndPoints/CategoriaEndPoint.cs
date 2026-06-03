using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class CategoriaEndPoint
{
    public static void MapAlimentoEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/categoria")

        rotas.MapGet("/", async (AppDbContext bd) =>
            await bd.Categoria.ToListAsync()
        );

        rotas.MapGet("/{nome}", async (string nome, AppDbContext bd)
        {
            var resultados = await bd.Categoria
                .Where(c => c.categoria.nome.ToLower() == categoria.nome.ToLower())
                .ToListAsync();

            return resultados.Any() ? Results.Ok(resultados) : Results.NotFound();
        });
    }
}