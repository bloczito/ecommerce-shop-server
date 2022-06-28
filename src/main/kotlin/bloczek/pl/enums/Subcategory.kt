package bloczek.pl.enums

enum class Subcategory(
    val title: String
) {
    // SWEATSHIRTS
    HOODIE("Hoodie"),
    CRAWNECK("Bez kaptura"),

    // T-SHIRTS
    T_SHIRT("Z krótkim rękawem"),
//    LONG_SLEEVE("Z długim rękawem"),
    LONG_SLEEVE("Longsleeve"),
    POLO("Polo"),

    // TROUSERS
    CHINOS("Chinosy"),
    JEANS("Jeansy"),
    ELEGANT("Eleganckie"),

    // ACCESSORIES
    HATS("Czapki i kapelusze"),
    WATCHES("Zegarki"),
    STRIPES("Paski"),
    BACKPACKS("Plecaki")
}
