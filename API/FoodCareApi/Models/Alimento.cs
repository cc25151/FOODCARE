namespace FoodCareApi.Models;

public class Alimento{
        public int idAlimento{ get; set; }
        public int idCategoria{ get; set; }
        public Categoria categoria { get; set;}
        public int idDoador{ get; set; }
        public Doador doador {get; set;}
        public string nome  { get; set; } = string.Empty;
        public string descricao { get; set; } = string.Empty;
        public int qntd { get; set; } = 0;
        public DateOnly validade {get; set;} = default;
        public string documento {get; set;} = string.Empty;
        
     
    }