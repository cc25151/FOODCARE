namespace FoodCareApi.Models;

public class Doacao{
        public int IdDoacao { get; set; }
        public DateOnly DataDoacao { get; set; }
        public TimeSpan HorarioInicial { get; set; }
        public TimeSpan HorarioFinal { get; set; } 
        public int? Avaliacao { get; set; }
        //Chave estrangeira do doador
        public int IdDoador { get; set; }
        public Doador Doador { get; set; } = null!;
        //Chave estrangeira do receptor
        public int IdReceptor { get; set; }
        public Receptor Receptor { get; set; } = null!;
        //Chave estrangeira do alimento
        public int IdAlimento { get; set; }
        public Alimento Alimento { get; set; } = null!;
    }
