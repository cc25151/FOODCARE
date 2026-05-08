using Microsoft.EntityFrameworkCore; //Importa as funcionalidades do FrameWork
using FoodCareApi.Models;           //IMPORTANTE: Ajustei para o seu namespace FoodCare

namespace FoodCareApi.Data; 

public class AppDbContext : DbContext 
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<Usuario> Usuarios { get; set; }
    public DbSet<Doador> Doador { get; set;} 
    public DbSet<Receptor> Receptor { get; set;} 
    public DbSet<Alimento> Alimento { get; set;} 
    public DbSet<Doacao> Doacao { get; set;} 
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        // Define o schema do banco de dados
        modelBuilder.HasDefaultSchema("FoodCare");

        //-----------------------TABELA USUÁRIO-----------------------//
        modelBuilder.Entity<Usuario>(entity =>
        {
            // Chave Primária
            entity.HasKey(u => u.IdUsuario); 

            // Campos básicos e obrigatórios
            entity.Property(u => u.Nome).IsRequired().HasColumnType("varchar(50)");
            entity.Property(u => u.Email).IsRequired().HasColumnType("varchar(100)");
            entity.HasIndex(u => u.Email).IsUnique();     //Email é único na tabela
            
            entity.Property(u => u.Senha).IsRequired().HasColumnType("varchar(255)");
            entity.Property(u => u.TipoPessoa).IsRequired().HasColumnType("char(2)"); 
            entity.Property(u => u.Documento).IsRequired().HasColumnType("varchar(14)");
            entity.HasIndex(u => u.Documento).IsUnique(); //CPF ou CNPJ é único na tabela

            //Os endereços não utilizam .IsRequired() pois podem ser preenchidos depois
            entity.Property(u => u.CEP).HasColumnType("char(8)");
            entity.Property(u => u.Cidade).HasColumnType("varchar(30)");
            entity.Property(u => u.Bairro).HasColumnType("varchar(60)");
            entity.Property(u => u.Rua).HasColumnType("varchar(60)");
            entity.Property(u => u.Numero).HasColumnType("varchar(10)");

            // Latitude e Longitude usadas para calcular a coordenada através da API externa
            entity.Property(u => u.Latitude).HasColumnType("decimal(9,6)");
            entity.Property(u => u.Longitude).HasColumnType("decimal(9,6)");
        }); 
        //-----------------------TABELA DOADOR-----------------------//
        modelBuilder.Entity(<Doador>(entity =>
        {
            //Chave Primária
            entity.HasKey(d => d.idDoador);

            // Configuração da Chave Estrangeira (Relacionamento 1:1)
            entity.Property(d => d.IdUsuario).IsRequired();
            
            
            // Define que o Doador tem apenas um usuário
            // O .HasForeignKey<Doador> indica que o ID do usuário fica na tabela Doador
            entity.HasOne(d => d.Usuario)
                  .WithOne()
                  .HasForeignKey<Doador>(d => d.IdUsuario);
        }
        ))
        //-----------------------TABELA RECEPTOR-----------------------//
        modelBuilder.Entity<Receptor>(entity =>
        {   
            //Chave Primária
            entity.HasKey(r => r.IdReceptor);

            // Define que o Receptor tem apenas um usuário
            entity.HasOne(r => r.Usuario)
                  .WithOne()
                  .HasForeignKey<Receptor>(r => r.IdUsuario);
        });
        //-----------------------TABELA CATEGORIA-----------------------//
        modelBuilder.Entity<Receptor>(entity =>
        {
            //Chave Primária
            entity.HasKey(c => c.IdCategoria);

            entity.Property(c => c.Nome).IsRequired().HasColumnType("varchar(30)");
            entity.Property(c => c.Imagem).IsRequired().HasColumnType("varchar(300)");
          });
        //-----------------------TABELA ALIMENTO-----------------------//
        modelBuilder.Entity<Alimento>(entity =>
        {   
            //Chave primária
            entity.HasKey(a => a.IdAlimento);

            entity.Property(a => a.Nome).IsRequired().HasColumnType("varchar(50)");
            entity.Property(a => a.Descricao).IsRequired().HasColumnType("varchar(100)");
            entity.Property(a => a.Validade).IsRequired().HasColumnType("date");

            // Um Alimento pertence a uma Categoria
            entity.HasOne(a => a.Categoria)
                  .WithMany()
                  .HasForeignKey(a => a.IdCategoria);

            // Um Alimento é postado por um Doador
            entity.HasOne(a => a.Doador)
                  .WithMany()
                  .HasForeignKey(a => a.IdDoador);
        });
        //-----------------------TABELA DOACAO-----------------------//
        modelBuilder.Entity<Doacao>(entity =>
        {
            //Chave Primária
            entity.HasKey(do => do.IdDoacao);

            entity.Property(do => do.DataDoacao).IsRequired().HasColumnType("date");
            entity.Property(do => do.HorarioInicial).IsRequired().HasColumnType("time");
            entity.Property(do => do.HorarioFinal).HasColumnType("time");
            
            
            entity.Property(do => do.Avaliacao).HasColumnType("int");

            //Relacionamentos
            entity.HasOne(do => do.Doador)
                  .WithMany()
                  .HasForeignKey(do => do.IdDoador)
                  

            entity.HasOne(d => d.Receptor)
                  .WithMany()
                  .HasForeignKey(d => d.IdReceptor)
                  

            entity.HasOne(d => d.Alimento)
                  .WithMany()
                  .HasForeignKey(d => d.IdAlimento)
                  
        });
    }
}