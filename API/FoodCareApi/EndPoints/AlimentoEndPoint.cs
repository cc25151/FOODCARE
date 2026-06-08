using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;

public static class AlimentoEndPoint
{
    public static void MapAlimentoEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/alimentos");

        rotas.MapGet("/feed/{idUsuario}", async (int idUsuario, AppDbContext bd) =>
        {
            var usuario = await bd.Usuario.FindAsync(idUsuario);

            if (usuario is null)
                return Results.NotFound("Usuário não encontrado.");

            if (usuario.latitude is null || usuario.longitude is null)
                return Results.BadRequest("Usuário sem localização cadastrada.");

            var alimentos = await bd.Alimento
                                        .Include(a => a.doador)
                                        .ThenInclude(d => d.usuario)
                                        .ToListAsync();


            foreach (var alimento in alimentos)
            {
                var usuarioDoador = alimento.doador.usuario;

                if (usuarioDoador?.latitude != null &&
                    usuarioDoador?.longitude != null)
                {
                    var lat1 = (double)usuario.latitude!;
                    var lon1 = (double)usuario.longitude!;

                    var lat2 = (double)usuarioDoador.latitude!;
                    var lon2 = (double)usuarioDoador.longitude!;

                    var R = 6371.0;

                    var dLat = (lat2 - lat1) * Math.PI / 180.0;
                    var dLon = (lon2 - lon1) * Math.PI / 180.0;

                    var a =
                        Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                        Math.Cos(lat1 * Math.PI / 180.0) *
                        Math.Cos(lat2 * Math.PI / 180.0) *
                        Math.Sin(dLon / 2) * Math.Sin(dLon / 2);

                    var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

                    alimento.distancia = R * c;
                }
            }

            return Results.Ok(alimentos);
        });

        rotas.MapGet("/{categoria}", async (string categoria, AppDbContext bd) =>
        {
            var resultados = await bd.Alimento
                .Where(a => a.categoria.nome.ToLower() == categoria.ToLower())
                .ToListAsync();
                
            return resultados.Any() ? Results.Ok(resultados) : Results.NotFound();
        });

        rotas.MapGet("/doador/{nomeDoador}", async (string nomeDoador, AppDbContext bd) =>
        {
            var usuario = await bd.Usuario.FirstOrDefaultAsync(u => u.nome.ToLower() == nomeDoador.ToLower());
            
            if (usuario == null) 
                return Results.NotFound($"Nenhum alimento encontrado para o doador: {nomeDoador}");

            var doador = await bd.Doador.FirstOrDefaultAsync(d => d.idUsuario == usuario.idUsuario);
            
            if (doador == null) 
                return Results.NotFound($"Nenhum alimento encontrado para o doador: {nomeDoador}");

            var alimentos = await bd.Alimento
                .Where(a => a.idDoador == doador.idDoador)
                .ToListAsync();
                

            return alimentos.Any() 
                ? Results.Ok(alimentos) 
                : Results.NotFound($"Nenhum alimento encontrado para o doador: {nomeDoador}");
        });

        // talvez fazer filtro por distancia

        rotas.MapPost("/doador/{nomeDoador}", async (Alimento novoAlimento, string nomeDoador, AppDbContext bd) => {
            var usuario = await bd.Usuario.FirstOrDefaultAsync(u => u.nome.ToLower() == nomeDoador.ToLower());
            if (usuario == null) 
                            return Results.NotFound($"Doador '{nomeDoador}' não encontrado.");

            var doador = await bd.Doador.FirstOrDefaultAsync(d => d.idUsuario == usuario.idUsuario);
            if (doador == null)         
                return Results.NotFound($"Doador '{nomeDoador}' não encontrado.");

            var jaExiste = await bd.Alimento.AnyAsync(a => 
                a.idDoador == doador.idDoador && 
                a.nome.ToLower() == novoAlimento.nome.ToLower());

            if (jaExiste)
                return Results.Conflict($"O doador {nomeDoador} já possui um alimento cadastrado com o nome '{novoAlimento.nome}'.");

            novoAlimento.idDoador = doador.idDoador;
            bd.Alimento.Add(novoAlimento);
            await bd.SaveChangesAsync();

            return Results.Created($"/alimentos/{novoAlimento.idAlimento}", new 
            { 
                id = novoAlimento.idAlimento, 
                nome = novoAlimento.nome,
                quantidade = novoAlimento.qntd,
                mensagem = "Alimento cadastrado com sucesso e disponível para doação!" 
            });
        });

        rotas.MapPut("/{id:int}", async (int id, Alimento alimentoAlterado, AppDbContext bd) =>
        {
            var alimento = await bd.Alimento.FindAsync(id);


            if (alimento is null) return Results.NotFound("Alimento não encontrado.");

            alimento.nome = alimentoAlterado.nome;
            alimento.descricao = alimentoAlterado.descricao;
            alimento.qntd = alimentoAlterado.qntd;
            alimento.validade = alimentoAlterado.validade;
            alimento.idCategoria = alimentoAlterado.idCategoria;

            await bd.SaveChangesAsync();

            return Results.Ok(new { mensagem = "Alimento alterado com sucesso!" });
        });

        rotas.MapDelete("/{id:int}", async (int id, AppDbContext bd) =>
        {
            var alimento = await bd.Alimento.FindAsync(id);
            
            if (alimento is not null)
            {
                bd.Alimento.Remove(alimento);
                await bd.SaveChangesAsync();
                return Results.Ok(new { mensagem = "Alimento removido com sucesso!" });
            }

            return Results.NotFound("Alimento não encontrado.");
        });
    }
}