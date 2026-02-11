package lml.snir.parkinglogickit.metier.entity;

/**
 *
 * @author Sylvain Crocquevieille
 */
public enum AccessType {
    ENTER("Entré"),
    LEFT("Sorti");

    public final String name;

    /**
     * Constructeur de l'enum AccessType
     *
     * @param name : Le string de l'enum AccessType
     */
    private AccessType(String name) {
        this.name = name;
    }

    /**
     * Retourne le nom de l'AccessType
     *
     * @return name : Le Nom de l'AccessType
     */
    public String getName() {
        return name;
    }
}
