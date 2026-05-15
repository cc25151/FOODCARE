using Microsoft.EntityFrameworkCore;
using FoodCareApi.Data;
using FoodCareApi.Models;
using Microsoft.AspNetCore.Authorization;

namespace FoodCareApi.Endpoints;

public static class DoadorEndPoints
{
    public static void MapDoadorEndpoints(this WebApplication app)
    {
    var grupo = app.MapGroup("/doador"); //Grupo de rotas de doador

    //GET por nome, específico para doador. (Usado ao pesquisar um restaurante
        grupo.MapGet("/{nome}", async (string nome, AppDbContext db) =>
            await db.Usuarios.FindAsync(nome) is Usuario usuarioDoador
                ? Results.Ok(usuarioDoador) 
                : Results.NotFound("Restaurante não encontrado."));


}}