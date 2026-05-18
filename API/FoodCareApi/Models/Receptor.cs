namespace FoodCareApi.Models;

public class Receptor{
        public int idReceptor { get; set; }
        public int idUsuario  { get; set; }
        public Usuario usuarioReceptor { get; set; } = null; // Usado para acessar os atributos de usuário
    }