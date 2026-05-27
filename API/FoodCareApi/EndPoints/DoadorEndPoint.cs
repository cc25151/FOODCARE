using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;

namespace FoodCareApi.Endpoints;


public static class DoadorEndpoints
{
    public static void MapDoadorEndpoints(this WebApplication app)
    {
        var grupo = app.MapGroup("/doadores");

        grupo.MapPost("/cadastro", async (Doador doador, AppDbContext db) =>
        {
            var usuarioExiste = await db.Usuario.AnyAsync(u => u.idUsuario == doador.idUsuario);
            if (!usuarioExiste)
            {
                return Results.BadRequest("O usuário informado não existe.");
            }

            var jaEhDoador = await db.Doador.AnyAsync(d => d.idUsuario == doador.idUsuario);
            if (jaEhDoador)
            {
                return Results.BadRequest("Este usuário já está cadastrado como doador.");
            }

           var novoDoador = new Doador
            {
                idUsuario = doador.idUsuario,
                pontuacao = 0
            };

            db.Doador.Add(novoDoador);
            await db.SaveChangesAsync();

            return Results.Created($"/doadores/{novoDoador.idDoador}", new
            {   
                mensagem = "Doador cadastrado com sucesso!",
                idDoador = novoDoador.idDoador,
                idUsuario = novoDoador.idUsuario,
            });
        });
    }
}