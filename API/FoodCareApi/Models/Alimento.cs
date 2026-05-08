namespace FoodCareApi.Models;

public class Alimento{
        public int idAlimento{ get; set; }
        public int idCategoria{ get; set; }
        public Categoria categoria { get; set;}
        public int idDoador{ get; set; }
        public Doador doador {get; set;}
        public string nome  { get; set; } = string.Empty;
        public string descricao { get; set; } = string.Empty;
        public string qntd { get; set; } = string.Empty;
        public DateOnly validade {get; set;} = string.Empty;
        public string documento {get; set;} = string.Empty;
        
     
    }