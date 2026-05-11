using FoodCareApi.Data;
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
    }
}