using FoodCareApi.Data;
using FoodCareApi.Endpoints;
using FoodCareApi.Models;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims; 

public static class AlimentoEndPoint
{
    public static void MapAlimentoEndPoints(this WebApplication app)
    {
        var rotas = app.MapGroup("/alimentos");

        // 1. GET - Listar Feed com cálculo de distância
        // Retorna todos os alimentos disponíveis calculando a distância em KM entre o usuário logado e o doador utilizando a fórmula de Haversine
        rotas.MapGet("/feed/{idUsuario}", async (int idUsuario, AppDbContext bd) =>
        {
            var usuario = await bd.Usuario.FindAsync(idUsuario);

            if (usuario is null)
                return Results.NotFound("Usuário não encontrado.");

            // Verifica se o usuário possui coordenadas geográficas antes de calcular as distâncias do feed
            if (usuario.latitude is null || usuario.longitude is null)
                return Results.BadRequest("Usuário sem localização cadastrada.");

            // Traz a lista de alimentos incluindo os relacionamentos necessários para obter a localização do doador
            var alimentos = await bd.Alimento
                                        .Include(a => a.doador)
                                        .ThenInclude(d => d.usuario)
                                        .ToListAsync();


            foreach (var alimento in alimentos)
            {
                var usuarioDoador = alimento.doador.usuario;

                // Só realiza o cálculo matemático se o doador também possuir uma localização 
                if (usuarioDoador?.latitude != null &&
                    usuarioDoador?.longitude != null)
                {
                    var lat1 = (double)usuario.latitude!;
                    var lon1 = (double)usuario.longitude!;

                    var lat2 = (double)usuarioDoador.latitude!;
                    var lon2 = (double)usuarioDoador.longitude!;

                    var R = 6371.0; // Raio da Terra em Quilômetros

                    // Conversão de graus para radianos
                    var dLat = (lat2 - lat1) * Math.PI / 180.0;
                    var dLon = (lon2 - lon1) * Math.PI / 180.0;

                    // Aplicação da fórmula de Haversine para determinar a distância em linha reta na esfera
                    var a =
                        Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                        Math.Cos(lat1 * Math.PI / 180.0) *
                        Math.Cos(lat2 * Math.PI / 180.0) *
                        Math.Sin(dLon / 2) * Math.Sin(dLon / 2);

                    var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

                    alimento.distancia = R * c; // Atribui o resultado em KM diretamente na propriedade do objeto
                }
            }

            return Results.Ok(alimentos);
        });

        // 2. GET por Categoria - Filtrar alimentos
        // Consulta todos os alimentos associados a uma categoria específica informada na URL ignorando maiúsculas/minúsculas
        rotas.MapGet("/{categoria}", async (string categoria, AppDbContext bd) =>
        {
            var resultados = await bd.Alimento
                .Where(a => a.categoria.nome.ToLower() == categoria.ToLower())
                .ToListAsync();
                
            return resultados.Any() ? Results.Ok(resultados) : Results.NotFound();
        });

        

        rotas.MapGet("/id/{idAlimento}", async (int idAlimento, ClaimsPrincipal user, AppDbContext bd) =>
        {
            // 1. Extrai o ID do usuário de dentro das Claims do Token JWT de forma segura
            var idUsuarioToken = user.FindFirst("idUsuario")?.Value;
            
            if (string.IsNullOrEmpty(idUsuarioToken))
                return Results.Unauthorized(); // Se o token for inválido ou não tiver o ID

            if (!int.TryParse(idUsuarioToken, out int idUsuarioLogado))
                return Results.Unauthorized();

            // 2. Busca o usuário logado para pegar a latitude/longitude dele
            var usuarioLogado = await bd.Usuario.FindAsync(idUsuarioLogado);
            if (usuarioLogado is null || usuarioLogado.latitude is null || usuarioLogado.longitude is null)
                return Results.BadRequest("Usuário logado sem localização cadastrada.");

            // 3. Busca o alimento trazendo JUNTO o doador e o usuário do doador (.Include)
            var alimento = await bd.Alimento
                .Include(a => a.doador)
                .ThenInclude(d => d.usuario)
                .FirstOrDefaultAsync(a => a.idAlimento == idAlimento);

            if (alimento == null)
                return Results.NotFound("Alimento não encontrado.");
            
            var usuarioDoador = alimento.doador?.usuario;

            // 4. Só realiza o cálculo matemático se o doador também possuir uma localização 
            if (usuarioDoador?.latitude != null && usuarioDoador?.longitude != null)
            {
                var lat1 = (double)usuarioLogado.latitude!;
                var lon1 = (double)usuarioLogado.longitude!;

                var lat2 = (double)usuarioDoador.latitude!;
                var lon2 = (double)usuarioDoador.longitude!;

                var R = 6371.0; // Raio da Terra em Quilômetros

                var dLat = (lat2 - lat1) * Math.PI / 180.0;
                var dLon = (lon2 - lon1) * Math.PI / 180.0;

                var a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                        Math.Cos(lat1 * Math.PI / 180.0) *
                        Math.Cos(lat2 * Math.PI / 180.0) *
                        Math.Sin(dLon / 2) * Math.Sin(dLon / 2);

                var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

                alimento.distancia = R * c; // Atribui a distância calculada
            }

            return Results.Ok(new
            {
                alimento.idAlimento,
                alimento.nome,
                alimento.idCategoria,
                alimento.idDoador,
                alimento.descricao,
                alimento.distancia,
                alimento.validade,
                usuarioDoador?.latitude,
                usuarioDoador?.longitude
            });
        })
        .RequireAuthorization(); // Bloqueia a rota caso o Android não envie o Token

        rotas.MapGet("/doador/{idUsuario}", async (int idUsuario, AppDbContext bd) =>
        {
            var alimentos = await bd.Alimento
                    .Include(a => a.doador)
                    .Include(a => a.categoria)
                    .Where(a => a.doador.idUsuario == idUsuario)
                    .ToListAsync();

            return Results.Ok(alimentos.Select(a => new
            {
                a.idAlimento,
                a.nome,
                a.idCategoria,
                categoria = a.categoria.nome,
                a.qntd,
                a.descricao,
                a.validade

            }).ToList()); 
        });

        // PATCH - Atualizar Parcialmente Dados do Alimento via Objeto
        // Recebe o idAlimento pela URL e um objeto contendo apenas qntd, validade e descricao no corpo
        rotas.MapPatch("/alterar/{idAlimento}", async (int idAlimento, Alimento dadosAlterados, AppDbContext bd) =>
        {
        // 1. Busca o alimento no banco de dados
        var alimento = await bd.Alimento.FindAsync(idAlimento);

        // Se o ID enviado pelo Android não existir no BD, o EF retorna null
        if (alimento is null) 
        {
            return Results.NotFound(new { erro = "Alimento não encontrado no banco." });
        }

        // 2. Aplica as novas propriedades que vieram do Android
        alimento.qntd = dadosAlterados.qntd;
        alimento.descricao = dadosAlterados.descricao;

        // Correção da data: Se veio uma data do Android, atualiza (mesmo se no BD estiver nulo)
        if (dadosAlterados.validade != null)
        {
            alimento.validade = dadosAlterados.validade;
        }

        // 3. Salva de fato no banco de dados (Gera o comando UPDATE)
        await bd.SaveChangesAsync();

        // 4. Retorna 200 OK limpo para o Retrofit saber que foi um sucesso absoluto
        return Results.Ok(); 
        });

        
   

        // 4. POST - Cadastrar Alimento para Doação
        // Vincula um novo alimento ao ID do doador logado e o disponibiliza na plataforma
        rotas.MapPost("/doador/{idUsuario}", async (Alimento novoAlimento, int idUsuario, AppDbContext bd) => {
            
            var doador = await bd.Doador.FirstOrDefaultAsync(d => d.idUsuario == idUsuario);
            if (doador == null)         
                return Results.NotFound($"Doador não encontrado."); // Ou não existe na tabela doador, ou não existe na tabela usuário

            novoAlimento.idDoador = doador.idDoador;
            bd.Alimento.Add(novoAlimento);
            await bd.SaveChangesAsync();

            return Results.Created();
        });

        // 5. PUT - Atualizar Dados do Alimento
        // Altera as informações gerais de um alimento existente (nome, descrição, quantidade, validade e categoria)
        rotas.MapPut("/{id:int}", async (int id, Alimento alimentoAlterado, AppDbContext bd) =>
        {
            var alimento = await bd.Alimento.FindAsync(id);


            if (alimento is null) return Results.NotFound("Alimento não encontrado.");

            // Atualização com as novas informações recebidas do Front-end
            alimento.nome = alimentoAlterado.nome;
            alimento.descricao = alimentoAlterado.descricao;
            alimento.qntd = alimentoAlterado.qntd;
            alimento.validade = alimentoAlterado.validade;
            alimento.idCategoria = alimentoAlterado.idCategoria;

            await bd.SaveChangesAsync();

            return Results.Ok(new { mensagem = "Alimento alterado com sucesso!" });
        });

        

        // 6. DELETE - Remover Alimento
        // Exclui o registro do alimento da base de dados com base no ID informado
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