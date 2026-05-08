namespace FoodCareApi.Models;

    public class Usuario{
        public int idUsuario{get;set;}
        public string nome  { get; set; } = string.Empty;
        public string email { get; set; } = string.Empty;
        public string senha { get; set; } = string.Empty;
        public string tipoPessoa {get; set;} = string.Empty;
        public string documento {get; set;} = string.Empty;
        public string? cep  { get; set; }
        public string? idade { get; set; }
        public string? Bairro { get; set; }
        public string? Rua    { get; set; }
        public string? Numero { get; set; }

        // Campos que a API externa vai preencher
        public decimal? Latitude  { get; set; }
        public decimal? Longitude { get; set; }
          

    }
