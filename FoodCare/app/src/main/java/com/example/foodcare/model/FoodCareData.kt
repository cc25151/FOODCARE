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

data class CategoriaUi(
    val id: Int,
    val nome: String
)


data class AlimentoFormData(
    val nome: String,
    val descricao: String,
    val quantidade: Int,
    val validade: String,
    val idCategoria: Int
)

data class CategoriaVisual(val nome: String, val emoji: String)

data class Categoria(val nome: String)

// ─── Sample data ──────────────────────────────────────────────────────────────
object FoodCareData {

    val categoriasVisuais = listOf(
        CategoriaVisual("Marmitas", "🍱"),
        CategoriaVisual("Não perecíveis", "🌾"),
        CategoriaVisual("Frutas", "🍎"),
        CategoriaVisual("Laticínios", "🧀"),
        CategoriaVisual("Bebidas", "🧃")
    )

    val produtos = listOf(
        Produto(1, "Cesta Básica",        "", "Alimentos", "Mercado", "1 km",  "Hoje",  0xFFFFF3E0),
        Produto(2, "Marmita Média",       "", "Marmitas",  "Rest.",   "1 km",  "Hoje",  0xFFFFEBEE),
        Produto(3, "Marmita Pequena",     "", "Marmitas",  "Rest.",   "1 km",  "Hoje",  0xFFFFF3E0),
        Produto(4, "Pacote de Arroz",     "", "Grãos",     "Mercado", "1 km",  "Hoje",  0xFFFFF8E1),
        Produto(5, "Caixas de Legumes",   "", "Vegetais",  "Horta",   "2 km",  "Amanhã",0xFFE8F5E9),
        Produto(6, "Pães Artesanais",     "", "Padaria",   "Padaria", "500 m", "Hoje",  0xFFFCE4EC)
    )
}
