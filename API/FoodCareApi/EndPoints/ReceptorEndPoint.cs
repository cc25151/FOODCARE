using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;

namespace FoodCareApi.Endpoints;

public static class ReceptorEndpoints
{
    public static void MapReceptorEndpoints(this WebApplication app)
    {
        var grupo = app.MapGroup("/receptores"); 


        grupo.MapPost("/cadastro", async (Receptor novoReceptor, AppDbContext db) =>
        {
            var usuarioExiste = await db.Usuario.AnyAsync(u => u.idUsuario == novoReceptor.idUsuario);
            if (!usuarioExiste)
            {
                return Results.BadRequest("O usuário informado não existe.");
            }

            var jaEhReceptor = await db.Receptor.AnyAsync(r => r.idUsuario == novoReceptor.idUsuario);
            if (jaEhReceptor)
            {
                return Results.BadRequest("Este usuário já está cadastrado como receptor.");
            }

            novoReceptor.usuarioReceptor = null!;


            db.Receptor.Add(novoReceptor);
            await db.SaveChangesAsync();


            return Results.Created($"/receptores/{novoReceptor.idReceptor}", new
            {
                idReceptor = novoReceptor.idReceptor,
                idUsuario = novoReceptor.idUsuario,
            });
        }); 
    }
}