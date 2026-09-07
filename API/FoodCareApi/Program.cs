using Microsoft.EntityFrameworkCore;
using System.Text;
using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using Microsoft.IdentityModel.Tokens;

var builder = WebApplication.CreateBuilder(args);

// 1. CONFIGURAÇÃO DE SERVIÇOS

// Configura o Swagger (Tela de testes das rotas)
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddAuthorization();

// 1. Busca a chave secreta que está dentro do seu appsettings.json
// (Ajuste o caminho "JwtSettings:ChaveSecreta" se o seu JSON for diferente, ex: "ChaveSecreta" direto)
var chaveSecreta = builder.Configuration["JwtSettings:ChaveSecreta"]; 

if (string.IsNullOrEmpty(chaveSecreta))
{
    throw new InvalidOperationException("A chave secreta do JWT não foi configurada no appsettings.json!");
}

// 2. Configura a Autenticação usando a chave para a validação do Token
builder.Services.AddAuthentication("Bearer")
    .AddJwtBearer("Bearer", options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            // Força a validação da assinatura criptográfica usando a nossa chave
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(chaveSecreta)),

            // Como você está em desenvolvimento local, vamos desligar a validação estrita de emissor e audiência
            // (Isso evita erros chatos de porta/IP ao testar no emulador do Android)
            ValidateIssuer = false,
            ValidateAudience = false,

            // Garante que o token enviado ainda está no prazo de validade
            ValidateLifetime = true,
            ClockSkew = TimeSpan.Zero // Remove a tolerância padrão de 5 minutos para expiração
        };
    });

// Configura a conexão com o Banco de Dados (SQL Server)
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));


var app = builder.Build();

app.UseRouting(); 

// 1. Ativa a autenticação (Quem é você? Valida o Token JWT)
app.UseAuthentication(); 

// 2. Ativa a autorização (Você tem permissão para acessar essa rota?)
app.UseAuthorization();

// Ativa o Swagger 
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Redireciona requisições HTTP para HTTPS automaticamente
//app.UseHttpsRedirection();



app.MapUsuarioEndpoints(); // Registra as rotas de Usuários 
app.MapAutenticacaoEndpoints(); // Registra as rotas de Login/Autenticação
app.MapDoadorEndpoints(); // Registra as rotas de doador
app.MapDoacaoEndPoints(); // Registra as rotas de doações
app.MapAlimentoEndPoints(); // Registra as rotas de alimentos
app.MapReceptorEndpoints();// Registra as rotas de receptor
app.MapCategoriaEndPoints();

// Deixa a API ativa e escutando o aplicativo Android
app.Run();