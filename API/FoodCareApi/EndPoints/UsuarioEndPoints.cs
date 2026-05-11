using FoodCareApi.Data;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class UsuarioEndPoints
{
    public static void mapUsuarioEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/usuarios");

        rotas.MapGet("/", async (AppDbContext bd) =>
            await bd.Usuarios.ToListAsync()
        );

        rotas.MapGet("/{id}", async (int id, AppDbContext bd) =>
            await bd.Usuarios.FindAsync(id) 
                is Usuario user
                ? Results.Ok(user) 
                : Results.NotFound()
        );

        rotas.MapPost("/", async(Usuario usuario, AppDbContext bd) =>
        {
            bd.Usuarios.Add(usuario);
            await bd.SaveChangesAsync();
            return Results.Created($"/usuarios/{usuario.idUsuario}", usuario);
        });

        rotas.MapPut("/{id}", async (int id, Usuario usuarioAlterado, AppDbContext bd) =>
        {
            var user = await bd.Usuarios.FindAsync(id);
            if (user is null) return Results.NotFound();

            user.nome = usuarioAlterado.nome;
            user.email = usuarioAlterado.email;
            user.tipoPessoa = usuarioAlterado.tipoPessoa;
            user.documento = usuarioAlterado.documento;
            user.senha = usuarioAlterado.senha;
            user.cep = usuarioAlterado.cep;
            user.cidade = usuarioAlterado.cidade;
            user.bairro = usuarioAlterado.bairro;
            user.rua = usuarioAlterado.rua;
            user.numero = usuarioAlterado.numero;
            user.Latitude = usuarioAlterado.Latitude;
            user.Longitude = usuarioAlterado.Longitude;

            await bd.SaveChangesAsync();
            return Results.NoContent();
        });

        rotas.MapDelete("/{id}", async (int id, AppDbContext bd) =>
        {
            if (await bd.Usuarios.FindAsync(id) is Usuario user)
            {
                bd.Usuarios.Remove(user);
                await bd.SaveChangesAsync();
                return Results.Ok(user);
            }
            return Results.NotFound();
        });
    }
}