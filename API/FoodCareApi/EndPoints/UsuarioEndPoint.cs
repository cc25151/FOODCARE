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
            await db.Usuario.ToListAsync()); 

        // 2. GET por ID - Consulta o usuário com aquele Id (Sintaxe corrigida)
        grupo.MapGet("/{id}", async (int id, AppDbContext db) =>
        {
            var usuario = await db.Usuario.FindAsync(id);
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
            if (await db.Usuario.AnyAsync(u => u.email == usuario.email))
                return Results.BadRequest("Este e-mail já está em uso.");

            if (await db.Usuario.AnyAsync(u => u.documento == usuario.documento))
                return Results.BadRequest("Este CPF/CNPJ já está cadastrado.");
            
            // Parte fundamental da criptografia da senha, usando BCrypt
            usuario.senha = BCrypt.Net.BCrypt.HashPassword(usuario.senha); 
            usuario.tipoPessoa = usuario.tipoPessoa.ToUpper();
        
            db.Usuario.Add(usuario);
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
        grupo.MapPatch("/completar-perfil/{id}", async (int id, Usuario dadosPerfil, AppDbContext db) =>
        {
            var usuario = await db.Usuario.FindAsync(id);
            
            if (usuario is null) return Results.NotFound("Usuário não encontrado.");

            // Atualização dos campos de endereço que vieram nulos
         usuario.cep    =  !string.IsNullOrWhiteSpace(dadosPerfil.cep) ? dadosPerfil.cep : usuario.cep;
         usuario.cidade =  !string.IsNullOrWhiteSpace(dadosPerfil.cidade) ? dadosPerfil.cidade : usuario.cidade;
         usuario.bairro =  !string.IsNullOrWhiteSpace(dadosPerfil.bairro) ? dadosPerfil.bairro : usuario.bairro;
         usuario.rua    =  !string.IsNullOrWhiteSpace(dadosPerfil.rua) ? dadosPerfil.rua : usuario.rua;
         usuario.numero =  !string.IsNullOrWhiteSpace(dadosPerfil.numero) ? dadosPerfil.numero : usuario.numero;
    
        // Para as coordenadas calculadas pela api externa, checamos se elas foram enviadas (diferentes de zero)
        usuario.latitude = dadosPerfil.latitude != 0 ? dadosPerfil.latitude : usuario.latitude;
        usuario.longitude = dadosPerfil.longitude != 0 ? dadosPerfil.longitude : usuario.longitude;
                
        
                await db.SaveChangesAsync();

                return Results.Ok(new { mensagem = "Perfil e localização atualizados com sucesso!" });
            }); 

        // 5. DELETE - Excluir conta
        grupo.MapDelete("/{id}", async (int id, AppDbContext db) =>
        {
            var usuario = await db.Usuario.FindAsync(id);
            if (usuario is null) return Results.NotFound();

            db.Usuario.Remove(usuario);
            await db.SaveChangesAsync();
            return Results.Ok($"Usuário {id} removido.");
        }); 
    }
}