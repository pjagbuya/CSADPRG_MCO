enum class Text_Color(val ansiCode : String) {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    CYAN("\u001B[36m"),
    ORANGE("\u001B[1;38;5;202m");

    override fun toString(): String {
        return ansiCode
    }
}