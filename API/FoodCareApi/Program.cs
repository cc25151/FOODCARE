using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Endpoints;

var builder = WebApplication.CreateBuilder(args);

// 1. CONFIGURAÇÃO DE SERVIÇOS

// Configura o Swagger (Tela de testes das rotas)
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Configura a conexão com o Banco de Dados (SQL Server)
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));


var app = builder.Build();

// Ativa o Swagger 
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Redireciona requisições HTTP para HTTPS automaticamente
app.UseHttpsRedirection();



app.MapUsuarioEndpoints(); // Registra as rotas de Usuários 
app.MapAutenticacaoEndpoints(); // Registra as rotas de Login/Autenticação
app.MapDoadorEndpoints(); // Registra as rotas de doador
app.MapDoacaoEndPoints(); // Registra as rotas de doações
app.MapAlimentoEndPoints(); // Registra as rotas de alimentos
app.MapReceptorEndpoints();// Registra as rotas de receptor

// Deixa a API ativa e escutando o aplicativo Android
app.Run();