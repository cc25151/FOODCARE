    //Para fins de organização, foi criado um EndPoint específio para a autentificação de um login

    using Microsoft.EntityFrameworkCore;
    using FoodCareApi.Data;
    using FoodCareApi.Models;
    using Microsoft.IdentityModel.Tokens;
    using System.IdentityModel.Tokens.Jwt;
    using System.Security.Claims;
    using System.Text;

    namespace FoodCareApi.Endpoints;

    public record RequisicaoLogin(string email, string senha);

    public static class AutenticacaoEndpoints
    {
        public static void MapAutenticacaoEndpoints(this WebApplication app)
        {
            app.MapPost("/login", async (RequisicaoLogin usuarioLogin, AppDbContext db, IConfiguration config) =>
            {
                //Proucura um usuário por email e senha
                var usuario = await db.Usuario
                    .FirstOrDefaultAsync(u => u.email == usuarioLogin.email);

                //Verifica se o usuário existe e se a senha digitada bate com o hash do banco
                if (usuario is null || !BCrypt.Net.BCrypt.Verify(usuarioLogin.senha, usuario.senha)) 
                    return Results.Unauthorized();

                //Se encontrar, prepara a geração do Token JWT
                var tokenHandler = new JwtSecurityTokenHandler();
                
                // Pega a chave secreta do arquivo appsettings.json
                var chaveSecreta = config["JwtSettings:ChaveSecreta"]!;
                var chaveBytes = Encoding.ASCII.GetBytes(chaveSecreta); // Pega o número de bytes da chave secreta

                // Define as informações que vão dentro do Token Reinvidicações
                var DescricaoToken = new SecurityTokenDescriptor
                {
                    Subject = new ClaimsIdentity(new[] // Claims são informações que são passadas através da reinvidicações
                    {
                        new Claim(ClaimTypes.Name, usuario.nome), //Nome do usuário dentro do token
                        new Claim(ClaimTypes.Email, usuario.email), //E-mail do usuário dentro do token
                        new Claim("idUsuario", usuario.idUsuario.ToString()), //Claim personalizada para id do usuário
                        new Claim("tipo", usuario.tipoPessoa) // Útil para saber se é PF ou PJ no Front-end, Claim personalizada
                    }),
                    Expires = DateTime.UtcNow.AddHours(1), // Token vale por 1 hora
                    //Tranca e assina o token para garantir que ninguém adultere os dados.
                    SigningCredentials = new SigningCredentials( 
                        new SymmetricSecurityKey(chaveBytes),   // Usa a sua chave secreta convertida em bytes para gerar a assinatura digital
                        SecurityAlgorithms.HmacSha256Signature) // HmacSha256 é um algoritmo matemático moderno de criptografia
                };

                // Cria o Token e envia para o usuário
                var token = tokenHandler.CreateToken(DescricaoToken); //cria o objeto interno do Token

                // Transforma o objeto do Token em uma string real formatada
                var tokenString = tokenHandler.WriteToken(token);

                // checa qual tipo de usuário
                var ehDoador = db.Doador.Any(d => d.idUsuario == usuario.idUsuario);
                var ehReceptor = db.Receptor.Any(r => r.idUsuario == usuario.idUsuario);
                var tipo = "";

                if(ehDoador){
                    if(ehReceptor)
                        tipo = "ambos";
                    else
                        tipo = "doador";
                }
                else
                    tipo = "receptor";

                // Envia o nome do usuário e a string do token para usar nas próximas requisições.
                return Results.Ok(new 
                { 
                    usuario.idUsuario,
                    usuario.nome,
                    token = tokenString,
                    tipoUsuario = tipo
                });
            });
        }
    }