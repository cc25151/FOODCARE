create schema FoodCare

create table FoodCare.Usuario(
	idUsuario int identity primary key,
	nome varchar(50) not null,
	email varchar(100) not null unique,
	tipoPessoa char(2) not null,
    documento varchar(14) not null unique,
	senha varchar(255) not null,
	CEP char(8) null,
	cidade varchar(30) null,
	bairro varchar(60) null,
	rua varchar(60) null,
	numero varchar(10) null,
	latitude decimal(9,6),
	longitude decimal(9,6),
	check (
    (tipoPessoa = 'PF' and LEN(documento) = 11)
    or
    (tipoPessoa = 'PJ' and LEN(documento) = 14)
	),
	constraint ckCEP check(CEP like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]')
)

create table FoodCare.Categoria(
	idCategoria int identity primary key,
	nome varchar(30) not null,
	imagem varchar(300) not null
)

create table FoodCare.Doador(
	idDoador int identity primary key,
	idUsuario int not null unique,
	pontuacao decimal(2,1) null,
	constraint fkUsuario foreign key(idUsuario) references FoodCare.Usuario(idUsuario)
)

create table FoodCare.Receptor(
	idReceptor int identity primary key,
	idUsuario int not null unique,
	constraint fkUsuarioReceptor foreign key(idUsuario) references FoodCare.Usuario(idUsuario)
)


create table FoodCare.Alimento(
	idAlimento int identity primary key,
	idCategoria int not null,
	idDoador int not null,
	nome varchar(50) not null,
	descricao varchar(100) not null,
	qntd int not null,
	validade date not null,
	constraint fkDoador foreign key(idDoador) references FoodCare.Doador(idDoador),
	constraint fkCategoria foreign key(idCategoria) references FoodCare.Categoria(idCategoria)
)

create table FoodCare.Doacao(
	idDoacao int identity primary key,
	dataDoacao date not null,
	horarioInicial time not null,
	horarioFinal time null,
	avaliacao int null,
	idDoador int not null,
	idReceptor int not null,
	idAlimento int not null,
	constraint fkDoadorDoacao foreign key(idDoador) references FoodCare.Doador(idDoador),
    check (avaliacao between 1 and 5),
	constraint fkReceptor foreign key(idReceptor) references FoodCare.Receptor(idReceptor),
	constraint fkAlimento foreign key(idAlimento) references FoodCare.Alimento(idAlimento)
)

DROP TABLE FoodCare.Doacao;
DROP TABLE FoodCare.Alimento;
DROP TABLE FoodCare.Doador;
DROP TABLE FoodCare.Receptor;
DROP TABLE FoodCare.Categoria;
DROP TABLE FoodCare.Usuario;

/*UPDATE d
SET pontuacao = (
    SELECT AVG(avaliacao)
    FROM FoodCare.Doacao
    WHERE idDoador = d.idDoador
    AND avaliacao IS NOT NULL
)
FROM FoodCare.Doador d
WHERE d.idDoador = @idDoador;
*/

