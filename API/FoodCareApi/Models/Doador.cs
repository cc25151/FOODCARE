namespace FoodCareApi.Models;

public class Doador{
        public int idDoador      { get; set; }
        public int idUsuario     { get; set; }
        public Usuario usuarioDoador  { get; set; } = null!; //Remover
        public decimal pontuacao { get; set; }
    }