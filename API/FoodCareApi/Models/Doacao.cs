namespace FoodCareApi.Models;

public class Doacao{
        public int idDoacao { get; set; }
        public DateOnly dataDoacao { get; set; }
        public TimeOnly horario { get; set; }
        public int? avaliacao { get; set; }
        //Chave estrangeira do doador
        public int idDoador { get; set; }
        public string status { get; set; } = "Pendente";
        public Doador doador { get; set; } = null!;
        //Chave estrangeira do receptor
        public int idReceptor { get; set; }
        public Receptor receptor { get; set; } = null!;
        //Chave estrangeira do alimento
        public int idAlimento { get; set; }
        public Alimento alimento { get; set; } = null!;
    }
