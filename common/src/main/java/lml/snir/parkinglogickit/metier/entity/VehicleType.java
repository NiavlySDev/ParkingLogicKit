package lml.snir.parkinglogickit.metier.entity;

/**
 * Types: Moto, Voiture, Camionette, Camion
 *
 * @author Sylvain Crocquevieille
 */
public enum VehicleType {

    Moto("Moto"), //Number 0
    Voiture("Voiture"),//Number 1
    Camionnette("Camionette"),//Number 2
    Camion("Camion");//Number 3

    private String name;

    /**
     * Constructeur de la Classe
     *
     * @param name : Le nom du véhicule
     */
    private VehicleType(String name) {
        this.name = name;
    }

    /**
     * Récupérer le nom du type de Voiture (VehicleType)
     *
     * @return name : le nom du type de Voiture (VehicleType)
     */
    public String getName() {
        return name;
    }
}
