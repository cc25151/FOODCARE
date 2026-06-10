using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;

namespace FoodCareApi.Endpoints;

public static class ReceptorEndpoints
{
    public static void MapReceptorEndpoints(this WebApplication app)
    {
        var grupo = app.MapGroup("/receptores"); 
        
        // 1. GET por ID - Consulta o receptor com aquele Id
        // Retorna as informações do perfil de receptor específico localizado pelo ID correspondente
        grupo.MapGet("/{id}" , async (int id, AppDbContext db) =>
        {
            var receptor = await db.Receptor.FindAsync(id);
            if (receptor is null)
                return Results.NotFound("Receptor não encontrado.");

            return Results.Ok(receptor);
        });

        // 2. POST - Cadastro como Receptor
        // O usuário estende sua conta para se tornar um receptor na plataforma, habilitando seu perfil para receber doações
        grupo.MapPost("/cadastro", async (Receptor novoReceptor, AppDbContext db) =>
        {
            // Verifica se o idUsuario enviado realmente existe na tabela de usuários do sistema
            var usuarioExiste = await db.Usuario.AnyAsync(u => u.idUsuario == novoReceptor.idUsuario);
            if (!usuarioExiste)
            {
                return Results.BadRequest("O usuário informado não existe.");
            }

            // Garante que o usuário não crie mais de um perfil de receptor
            var jaEhReceptor = await db.Receptor.AnyAsync(r => r.idUsuario == novoReceptor.idUsuario);
            if (jaEhReceptor)
            {
                return Results.BadRequest("Este usuário já está cadastrado como receptor.");
            }


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