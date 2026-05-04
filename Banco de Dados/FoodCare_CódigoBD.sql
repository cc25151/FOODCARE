create schema FoodCare

create table FoodCare.Usuario(
	idUsuario int primary key,
	nome varchar(50) not null,
	email varchar(30) not null,
	senha varchar(20) not null
)

create table FoodCare.Doador(
	idDoador int primary key,
	idUsuario int not null,
	pontuacao decimal not null,
	CEP char(9) not null,
	cidade varchar(30) not null,
	bairro varchar(60) not null,
	rua varchar(60) not null,
	num varchar(60) not null,
	constraint ckCEP check(CEP like '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
	constraint fkUsuario foreign key(idUsuario) references FoodCare.Usuario(idUsuario)
)

create table FoodCare.Receptor(
	idReceptor int primary key,
	idUsuario int not null,
	constraint fkUsuarioReceptor foreign key(idUsuario) references FoodCare.Usuario(idUsuario)
)



create table FoodCare.Alimento(
	idAlimento int primary key,
	idCategoria int not null,
	idDoador int not null,
	nome varchar(50) not null,
	info varchar(100) not null,
	qntd int not null,
	validade date not null,
	constraint fkDoador foreign key(idDoador) references FoodCare.Doador(idDoador),
	constraint fkCategoria foreign key(idCategoria) references FoodCare.Categoria(idCategoria)
)

create table FoodCare.Categoria(
idCategoria int primary key,
nome varchar(30) not null,
imagem varchar(300) not null,
)

create table FoodCare.Doacao(
idDoacao int primary key,
dataDoacao date not null,
horarioInicial time not null,
horarioFinal time null,
idDoador int not null,
idReceptor int not null,
idAlimento int not null,
constraint fkDoadorDoacao foreign key(idDoador) references FoodCare.Doador(idDoador),
constraint fkReceptor foreign key(idReceptor) references FoodCare.Receptor(idReceptor),
constraint fkAlimento foreign key(idAlimento) references FoodCare.Alimento(idAlimento)
)

