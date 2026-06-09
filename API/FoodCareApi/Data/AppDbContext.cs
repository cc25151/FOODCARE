using Microsoft.EntityFrameworkCore; //Importa as funcionalidades do FrameWork
using FoodCareApi.Models;           

namespace FoodCareApi.Data; 

public class AppDbContext : DbContext 
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<Usuario>  Usuario  { get; set; }
    public DbSet<Doador>   Doador    { get; set;} 
    public DbSet<Receptor> Receptor  { get; set;} 
    public DbSet<Categoria> Categoria { get; set;}
    public DbSet<Alimento> Alimento  { get; set;} 
    public DbSet<Doacao>   Doacao    { get; set;} 
    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        // Define o schema do banco de dados
        modelBuilder.HasDefaultSchema("FoodCare");

        //-----------------------TABELA USUÁRIO-----------------------//
        modelBuilder.Entity<Usuario>(entity =>
        {
            // Chave Primária
            entity.HasKey(u => u.idUsuario); 

            // Campos básicos e obrigatórios
            entity.Property(u => u.nome).IsRequired().HasColumnType("varchar(50)");
            entity.Property(u => u.email).IsRequired().HasColumnType("varchar(100)");
            entity.HasIndex(u => u.email).IsUnique();     //Email é único na tabela
            
            entity.Property(u => u.senha).IsRequired().HasColumnType("varchar(255)");
            entity.Property(u => u.tipoPessoa).IsRequired().HasColumnType("char(2)"); 
            entity.Property(u => u.documento).IsRequired().HasColumnType("varchar(14)");
            entity.HasIndex(u => u.documento).IsUnique(); //CPF ou CNPJ é único na tabela

            //Os endereços não utilizam .IsRequired() pois podem ser preenchidos depois
            entity.Property(u => u.cep).HasColumnType("char(8)");
            entity.Property(u => u.cidade).HasColumnType("varchar(30)");
            entity.Property(u => u.bairro).HasColumnType("varchar(60)");
            entity.Property(u => u.rua).HasColumnType("varchar(60)");
            entity.Property(u => u.numero).HasColumnType("varchar(10)");

            // Latitude e Longitude usadas para calcular a coordenada através da API externa
            entity.Property(u => u.latitude).HasColumnType("float");
            entity.Property(u => u.longitude).HasColumnType("float");
        }); 
        //-----------------------TABELA DOADOR-----------------------//
        modelBuilder.Entity<Doador>(entity =>
        {
            //Chave Primária
            entity.HasKey(d => d.idDoador);

            // Configuração da Chave Estrangeira (Relacionamento 1:1)
            entity.Property(d => d.idUsuario).IsRequired();
            entity.Property(d => d.horarioInicial).HasColumnType("time");
            entity.Property(d => d.horarioFinal).HasColumnType("time");
            
            
            // Define que o Doador tem apenas um usuário
            // O .HasForeignKey<Doador> indica que o ID do usuário fica na tabela Doador

            entity.HasOne(d => d.usuario)
                .WithOne()
                .HasForeignKey<Doador>(d => d.idUsuario);
        });
        //-----------------------TABELA RECEPTOR-----------------------//
        modelBuilder.Entity<Receptor>(entity =>
        {   
            //Chave Primária
            entity.HasKey(r => r.idReceptor);

            // Define que o Receptor tem apenas um usuário
            entity.HasOne<Usuario>() // Cada receptor deve ter um usuário associado
                  .WithOne()
                  .HasForeignKey<Receptor>(r => r.idUsuario);
        });
        //-----------------------TABELA CATEGORIA-----------------------//
        modelBuilder.Entity<Categoria>(entity =>
        {
            //Chave Primária
            entity.HasKey(c => c.idCategoria);

            entity.Property(c => c.nome).IsRequired().HasColumnType("varchar(30)");
            entity.Property(c => c.imagem).IsRequired().HasColumnType("varchar(300)");
        });
        //-----------------------TABELA ALIMENTO-----------------------//
        modelBuilder.Entity<Alimento>(entity =>
        {   
            //Chave primária
            entity.HasKey(a => a.idAlimento);

            entity.Property(a => a.nome).IsRequired().HasColumnType("varchar(50)");
            entity.Property(a => a.descricao).IsRequired().HasColumnType("varchar(100)");
            entity.Property(a => a.validade).IsRequired().HasColumnType("date");

            // Um Alimento pertence a uma Categoria
            entity.HasOne(a => a.categoria)
                  .WithMany()
                  .HasForeignKey(a => a.idCategoria);

            // Um Alimento é postado por um Doador
            entity.HasOne(a => a.doador)
                  .WithMany()
                  .HasForeignKey(a => a.idDoador);
        });
        //-----------------------TABELA DOACAO-----------------------//
        modelBuilder.Entity<Doacao>(entity =>
        {
            //Chave Primária
            entity.HasKey(doa => doa.idDoacao);

            entity.Property(doa => doa.dataDoacao).IsRequired().HasColumnType("date");
            entity.Property(doa => doa.horario).IsRequired().HasColumnType("time");
            
            
            entity.Property(doa => doa.avaliacao).HasColumnType("int");

            //Relacionamentos
            entity.HasOne(doa => doa.doador)
                  .WithMany()
                  .HasForeignKey(doa => doa.idDoador);

            entity.HasOne(d => d.receptor)
                  .WithMany()
                  .HasForeignKey(d => d.idReceptor);

            entity.HasOne(d => d.alimento)
                  .WithOne()
                  .HasForeignKey<Doacao>(d => d.idAlimento);
        });
    }
}