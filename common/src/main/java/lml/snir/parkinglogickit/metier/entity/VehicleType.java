package lml.snir.parkinglogickit.metier.entity;

/**
 * Types: Moto, Voiture, Camionette, Camion
 *
 * @author sylvain
 */
public enum VehicleType {

    Moto("Moto"),
    Voiture("Voiture"),
    Camionnette("Camionette"),
    Camion("Camion");

    private String name;

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
