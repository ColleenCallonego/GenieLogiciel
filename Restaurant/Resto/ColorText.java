package Restaurant.Resto;

public enum ColorText {
    COL_RESET("\u001B[0m"),//reset couleur texte
    COL_RED("\u001B[31m"),//pour les table à débarasser
    COL_PURPLE("\033[0;35m"),//pour les table à dresser
    COL_GREEN("\u001B[32m"),//pour les tables libres
    COL_YELLOW("\u001B[33m"),//pour les tables occupées
    COL_CYAN("\u001B[36m"),//pour les tables réservées
    COL_BRIGHT_WHITE("\u001B[97m");// pour les tables non liées au serveur

    private final String valeur;

    ColorText(String valeur) {
        this.valeur = valeur;
    }
}