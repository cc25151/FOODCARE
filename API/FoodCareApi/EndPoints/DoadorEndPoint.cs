using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;

namespace FoodCareApi.Endpoints;


public static class DoadorEndpoints
{
    public static void MapDoadorEndpoints(this WebApplication app)
    {
        var grupo = app.MapGroup("/doadores");

        // 1. GET por ID - Consulta o doador com aquele Id
        // Retorna as informações do perfil de doador específico localizado pelo ID correspondente
        grupo.MapGet("/{id}", async (int id, AppDbContext db) =>
        {
            var doador = await db.Doador.FindAsync(id);
            if (doador is null)
                return Results.NotFound("Doador não encontrado.");

            return Results.Ok(doador);
        });

        // 2. POST - Cadastro como Doador
        // O usuário estende sua conta para se tornar um doador na plataforma, iniciando seu perfil com pontuação zerada
        grupo.MapPost("/cadastro", async (Doador doador, AppDbContext db) =>
        {
            // Verifica se o idUsuario enviado realmente existe na tabela de usuários do sistema
            var usuarioExiste = await db.Usuario.AnyAsync(u => u.idUsuario == doador.idUsuario);
            if (!usuarioExiste)
            {
                return Results.BadRequest("O usuário informado não existe.");
            }

            // Garante que o usuário não crie mais de um perfil de doador
            var jaEhDoador = await db.Doador.AnyAsync(d => d.idUsuario == doador.idUsuario);
            if (jaEhDoador)
            {
                return Results.BadRequest("Este usuário já está cadastrado como doador.");
            }

           var novoDoador = new Doador
            {
                idUsuario = doador.idUsuario,
                pontuacao = 0 //A pontuação inicia zerada no cadastro do doador
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