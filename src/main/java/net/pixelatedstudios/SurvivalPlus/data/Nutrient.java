package net.pixelatedstudios.SurvivalPlus.data;

/**
 * Player nutrient types
 */
public enum Nutrient {

    CARBS("Carbs"),
    PROTEIN("Protein"),
    SALTS("Salts");

    private final String name;

    Nutrient(String nutrient) {
        name = nutrient;
    }

    public String getName() {
        return name;
    }

}
