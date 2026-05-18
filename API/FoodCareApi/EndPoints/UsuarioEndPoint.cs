using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;

namespace FoodCareApi.Endpoints;

public static class UsuarioEndpoints
{
    public static void MapUsuarioEndpoints(this WebApplication app)
    {
        var grupo = app.MapGroup("/usuarios"); // Cria um grupo de rotas que começam com /usuarios
    
        // 1. GET - Listar todos 
        grupo.MapGet("/", async (AppDbContext db) => 
            await db.Usuarios.ToListAsync()); 

        // 2. GET por ID - Consulta o usuário com aquele Id (Sintaxe corrigida)
        grupo.MapGet("/{id}", async (int id, AppDbContext db) =>
        {
            var usuario = await db.Usuarios.FindAsync(id);
            if (usuario is null)
                return Results.NotFound("Usuário não encontrado.");

            // Zera a senha na memória antes de enviar para o Front-end por segurança
            usuario.senha = string.Empty; 
            return Results.Ok(usuario);
        });

        // 3. POST - Cadastro Inicial (Sem Endereço)
        // O usuário cria a conta sem as informações de endereço, apenas com: Nome, Senha, E-mail e CPF/CNPJ
        grupo.MapPost("/cadastro", async (Usuario usuario, AppDbContext db) =>
        {
            // Verifica se o usuário já é cadastrado
            if (await db.Usuarios.AnyAsync(u => u.email == usuario.email))
                return Results.BadRequest("Este e-mail já está em uso.");

            if (await db.Usuarios.AnyAsync(u => u.documento == usuario.documento))
                return Results.BadRequest("Este CPF/CNPJ já está cadastrado.");
            
            // Parte fundamental da criptografia da senha, usando BCrypt
            usuario.senha = BCrypt.Net.BCrypt.HashPassword(usuario.senha); 
            
            db.Usuarios.Add(usuario);
            await db.SaveChangesAsync();

            return Results.Created($"/usuarios/{usuario.idUsuario}", new 
            { 
                usuario.idUsuario, 
                usuario.nome,
                mensagem = "Cadastro realizado! Complete seu perfil para doar." 
            });
        });

        // 4. PUT - Completar Perfil com as informações de endereço
        // O EndPoint é chamado quando o usuario entra na página de perfil
        grupo.MapPut("/completar-perfil/{id}", async (int id, Usuario dadosPerfil, AppDbContext db) =>
        {
            var usuario = await db.Usuarios.FindAsync(id);
            
            if (usuario is null) return Results.NotFound("Usuário não encontrado.");

            // Atualização dos campos de endereço que vieram nulos
            usuario.cep = dadosPerfil.cep;
            usuario.cidade = dadosPerfil.cidade;
            usuario.bairro = dadosPerfil.bairro;
            usuario.rua = dadosPerfil.rua;
            usuario.numero = dadosPerfil.numero;
            
            // Coordenadas obtidas via Bing Maps
            usuario.Latitude = dadosPerfil.Latitude;
            usuario.Longitude = dadosPerfil.Longitude;

            await db.SaveChangesAsync();

            return Results.Ok(new { mensagem = "Perfil e localização atualizados com sucesso!" });
        }); 

        // 5. DELETE - Excluir conta
        grupo.MapDelete("/{id}", async (int id, AppDbContext db) =>
        {
            var usuario = await db.Usuarios.FindAsync(id);
            if (usuario is null) return Results.NotFound();

            db.Usuarios.Remove(usuario);
            await db.SaveChangesAsync();
            return Results.Ok($"Usuário {id} removido.");
        }); 
    }
}