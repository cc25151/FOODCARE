namespace FoodCareApi.Models;


    public class Usuario{
        public int idUsuario{get;}
        public string nome  { get; set; } = string.Empty;
        public string email { get; set; } = string.Empty;
        public string senha { get; set; } = string.Empty;
        public string tipoPessoa {get; set;} = string.Empty;
        public string documento {get; set;} = string.Empty;
        public string? cep  { get; set; }
        public string? cidade { get; set; }
        public string? bairro { get; set; }
        public string? rua    { get; set; }
        public string? numero { get; set; }

        // Campos que a API externa vai preencher
        public decimal? latitude  { get; set; } 
        public decimal? longitude { get; set; }
          

    }



    


