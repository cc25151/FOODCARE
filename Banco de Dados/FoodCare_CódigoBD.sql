create schema FoodCare

create table FoodCare.Categoria(
idCategoria int primary key,
nomeCategoria varchar(20) not null
)

create table FoodCare.Alimento(
idAlimento int primary key,
descAlimento varchar(100) not null,
nomeAlimento varchar(20) not null,
quantidade int not null,
validade date not null,
idCategoria int not null,
constraint fkIdCategoria foreign key(idCategoria) references FoodCare.Categoria(idCategoria)

)

create table FoodCare.Doador(
idDoador int primary key,
nomeDoador varchar(30) not null,
emailDoador varchar(20) not null,
senhaDoador varchar(300) not null,
locDoador int not null,
idAlimento int not null,
constraint fkIdAlimento foreign key(idAlimento) references FoodCare.Alimento(idAlimento)

)

create table FoodCare.Receptor(
idReceptor int primary key,
nomeReceptor varchar(30) not null,
emailReceptor varchar(30) not null,
senhaReceptor varchar(300) not null,
locReceptor int not null

)

create table FoodCare.Doacao(
idDoacao int primary key,
lugarRet int not null,
idDoador int not null,
idReceptor int not null,
idAlimento int not null
constraint fkIdAlimentoDoacao foreign key(idAlimento) references FoodCare.Alimento(idAlimento),
constraint fkIdDoador foreign key(idDoador) references FoodCare.Doador(idDoador),
constraint fkIdReceptor foreign key(idReceptor) references FoodCare.Receptor(idReceptor)
)

create table FoodCare.Usuario(
idUsuario
)