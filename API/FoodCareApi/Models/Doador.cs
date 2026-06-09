namespace FoodCareApi.Models;

public class Doador{
        public int idDoador      { get; set; }
        public int idUsuario     { get; set; }
        public decimal? pontuacao { get; set; }
        public TimeOnly horarioInicial { get; set; }
        public TimeOnly horarioFinal { get; set; }
        public Usuario usuario { get; set; } = null!;
    }