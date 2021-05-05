package Restaurant.Resto;

public enum ColorText {
    COL_RESET("\u001B[0m"),
    COL_RED("\u001B[31m"),
    COL_GREEN("\u001B[32m"),
    COL_YELLOW("\u001B[33m"),
    COL_CYAN("\u001B[36m"),
    COL_BRIGHT_WHITE("\u001B[97m");

    private final String valeur;

    ColorText(String valeur) {
        this.valeur = valeur;
    }
}