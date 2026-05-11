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

        rotas.MapGet("/doadores/{id}", async (int id, AppDbContext bd) =>
            await bd.Doador
                .Include(d => d.usuarioDoador) // Faz o JOIN com a tabela Usuario
                .FirstOrDefaultAsync(d => d.idDoador == id)
                is Doador doador
                ? Results.Ok(doador)
                : Results.NotFound()
        );

        rotas.MapGet("/receptores/{id}", async (int id, AppDbContext bd) =>
            await bd.Receptor
                .Include(r => r.usuarioReceptor) 
                .FirstOrDefaultAsync(r => r.idReceptor == id)
                is Receptor receptor
                ? Results.Ok(receptor) 
                : Results.NotFound()
        );

        rotas.MapPost("/", async(Usuario usuario, AppDbContext bd) =>
        {
            bd.Usuarios.Add(usuario);
            await bd.SaveChangesAsync();
            return Results.Created($"/usuarios/{usuario.idUsuario}", usuario);
        });

        rotas.MapPost("/doadores", async (Doador doador, AppDbContext bd) =>
        {

            if (doador.usuarioDoador is null) return Results.BadRequest("Dados do usuário são necessários.");
            
            bd.Usuarios.Add(doador.usuarioDoador);
            await bd.SaveChangesAsync(); 

            doador.idUsuario = doador.usuarioDoador.idUsuario;
            
            bd.Doador.Add(doador);
            await bd.SaveChangesAsync();

            return Results.Created($"/usuarios/doadores/{doador.idDoador}", doador);
        });

        rotas.MapPost("/receptores", async (Receptor receptor, AppDbContext bd) =>
        {
            if (receptor.usuarioReceptor is null) return Results.BadRequest("Dados do usuário são necessários.");

            bd.Usuarios.Add(receptor.usuarioReceptor);
            await bd.SaveChangesAsync();

            receptor.idUsuario = receptor.usuarioReceptor.idUsuario;
            
            bd.Receptor.Add(receptor);
            await bd.SaveChangesAsync();

            return Results.Created($"/usuarios/receptores/{receptor.idReceptor}", receptor);
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