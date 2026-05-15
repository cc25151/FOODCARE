//Para fins de organização, foi criado um EndPoint específio para a autentificação de um login

using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace FoodCareApi.Endpoints;

public static class AutenticacaoEndpoints
{
    public static void MapAutenticacaoEndpoints(this WebApplication app)
    {
        app.MapPost("/login", async (Usuario usuarioLogin, AppDbContext db, IConfiguration config) =>
        {
            //Proucura um usuário por email e senha
            var usuario = await db.Usuarios
                .FirstOrDefaultAsync(u => u.email == usuarioLogin.email && u.senha == usuarioLogin.senha);

            //Se não encontrar, retorna Não Autorizado (código http 401)
            if (usuario is null) 
                return Results.Unauthorized();

            //Se encontrar, prepara a geração do Token JWT
            var tokenHandler = new JwtSecurityTokenHandler();
            
            // Pega a chave secreta 
            var chaveSecreta = "chaveSecreta";
            var chaveBytes = Encoding.ASCII.GetBytes(chaveSecreta); // Pega o número de bytes da chave secreta

            // 4. Define as informações que vão dentro do Token Reinvidicações
            var DescricaoToken = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[]
                {
                    new Claim(ClaimTypes.Name, usuario.nome),
                    new Claim(ClaimTypes.Email, usuario.email),
                    new Claim("id", usuario.idUsuario.ToString()),
                    new Claim("tipo", usuario.tipoPessoa) // Útil para saber se é PF ou PJ no Front-end
                }),
                Expires = DateTime.UtcNow.AddHours(3), // Token vale por 3 horas
                SigningCredentials = new SigningCredentials(
                    new SymmetricSecurityKey(chaveBytes), 
                    SecurityAlgorithms.HmacSha256Signature)
            };

            // 5. Cria o Token e envia para o usuário
            var token = tokenHandler.CreateToken(DescricaoToken);
            var tokenString = tokenHandler.WriteToken(token);

            return Results.Ok(new 
            { 
                usuario = usuario.nome, 
                token = tokenString 
            });
        });
    }
}