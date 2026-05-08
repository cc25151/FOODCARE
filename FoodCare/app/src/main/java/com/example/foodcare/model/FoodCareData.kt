package com.example.foodcare.model

data class Produto(
    val id: Int,
    val nome: String,
    val descricao: String,
    val categoria: String,
    val doador: String,
    val distancia: String,
    val validade: String,
    val imageColor: Long   // using color as placeholder for image
)

data class Categoria(val nome: String)

// ─── Sample data ──────────────────────────────────────────────────────────────
object FoodCareData {

    val categorias = listOf(
        Categoria("Categoria A"),
        Categoria("Categoria B"),
        Categoria("Categoria C"),
        Categoria("Categoria D")
    )

    val produtos = listOf(
        Produto(
            id = 1,
            nome = "Red Quinoa Fruit Salad",
            descricao = "Red Quinoa, Lime, Honey, Blueberries, Strawberries, Mango, Fresh mint.\n\nIf you are looking for a tasty fruit salad to make, this quinoa is the perfect brunch for you.",
            categoria = "Categoria A",
            doador = "Mercado Central",
            distancia = "1.2 km",
            validade = "Até 02/05/2025",
            imageColor = 0xFFE53935
        ),
        Produto(
            id = 2,
            nome = "Tropical Fruit Salad",
            descricao = "Manga, Abacaxi, Mamão, Coco fresco e Limão. Combinação tropical rica em vitaminas.",
            categoria = "Categoria B",
            doador = "Restaurante Sol",
            distancia = "0.8 km",
            validade = "Até 01/05/2025",
            imageColor = 0xFFFF8F00
        ),
        Produto(
            id = 3,
            nome = "Quinoa Fruit Salad",
            descricao = "Quinoa branca, frutas vermelhas e mel natural. Opção saudável e nutritiva.",
            categoria = "Categoria A",
            doador = "Padaria Bom Dia",
            distancia = "2.1 km",
            validade = "Até 03/05/2025",
            imageColor = 0xFF66BB6A
        ),
        Produto(
            id = 4,
            nome = "Cesta de Pães",
            descricao = "Pães integrais, de centeio e francês frescos do dia.",
            categoria = "Categoria C",
            doador = "Padaria Nova",
            distancia = "0.5 km",
            validade = "Hoje",
            imageColor = 0xFFBCAAA4
        ),
        Produto(
            id = 5,
            nome = "Kit Verduras",
            descricao = "Alface, rúcula, espinafre e temperos frescos colhidos hoje.",
            categoria = "Categoria D",
            doador = "Feira do Bairro",
            distancia = "1.5 km",
            validade = "Até 30/04/2025",
            imageColor = 0xFF43A047
        ),
        Produto(
            id = 6,
            nome = "Leite e Derivados",
            descricao = "Leite integral, queijo minas e iogurte natural.",
            categoria = "Categoria B",
            doador = "Laticínios Silva",
            distancia = "3.0 km",
            validade = "Até 05/05/2025",
            imageColor = 0xFF81D4FA
        )
    )
}
