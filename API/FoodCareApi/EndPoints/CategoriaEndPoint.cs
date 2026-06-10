using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class CategoriaEndPoint
{
    public static void MapCategoriaEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/categorias");

        // 1. GET - Listar todas
        // Retorna a lista completa de categorias cadastradas para classificação dos alimentos
        rotas.MapGet("/", async (AppDbContext bd) =>
            await bd.Categoria.ToListAsync()
        );

        // 2. GET por Nome - Consulta categoria específica
        // Busca categorias filtrando pelo nome exato enviado na URL
        rotas.MapGet("/{nome}", async (string nome, AppDbContext bd) =>
        {
            var resultados = await bd.Categoria
                .Where(c => c.nome.ToLower() == nome.ToLower())
                .ToListAsync();

            return resultados.Any() ? Results.Ok(resultados) : Results.NotFound();
        });
    }
}