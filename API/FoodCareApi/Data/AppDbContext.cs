using Microsoft.EntityFrameworkCore; //Importa as funcionalidades do FrameWork
using MinhaLojaApi.Models;           //Importa as classes do projeto

namespace FoodCareApi.Data; 

public class AppDbContext : DbContext 
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<Usuario> Usuarios { get; set; } 

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
    
        modelBuilder.HasDefaultSchema("FoodCare");

        //-----------------------TABELA USUÁRIO-----------------------
        modelBuilder.Entity<Usuario>(entity =>
        {
           modelBuilder.Entity<Usuario>(entity =>
{
            // Chave Primária e Identidade
            entity.HasKey(u => u.IdUsuario); 

            // Campos básicos
            entity.Property(u => u.Nome).IsRequired().HasColumnType("varchar(50)");
            entity.Property(u => u.Email).IsRequired().HasColumnType("varchar(100)");
            entity.HasIndex(u => u.Email).IsUnique(); // unique do seu SQL
            entity.Property(u => u.Senha).IsRequired().HasColumnType("varchar(255)");

            // Endereço (Note que aqui usamos .HasColumnType para bater com seu SQL)
            entity.Property(u => u.CEP).HasColumnType("char(8)");
            entity.Property(u => u.Cidade).HasColumnType("varchar(30)");
            entity.Property(u => u.Bairro).HasColumnType("varchar(60)");
            entity.Property(u => u.Rua).HasColumnType("varchar(60)");
            entity.Property(u => u.Numero).HasColumnType("varchar(10)");

           
            entity.Property(u => u.TipoPessoa).IsRequired().HasColumnType("varchar(2)"); 
            entity.Property(u => u.Documento).IsRequired().HasColumnType("varchar(14)");

            // Coordenadas para a API de Geolocalização
            entity.Property(u => u.Latitude).HasColumnType("decimal(9,6)");
            entity.Property(u => u.Longitude).HasColumnType("decimal(9,6)");
});
        });
    
    }

    
}