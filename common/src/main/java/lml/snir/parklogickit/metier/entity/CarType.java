package lml.snir.parklogickit.metier.entity;

/**
 * Types: Moto, Voiture, Camionette, Camion
 * @author sylvain
 */
public enum CarType {
    
    Moto("Moto"),
    Voiture("Voiture"),
    Camionnette("Camionette"),
    Camion("Camion");
    
    private String name;

    private CarType(String name) {
        this.name = name;
    }

    /**
     * Récupérer le nom du type de Voiture (CarType)
     * @return name : le nom du type de Voiture (CarType)
     */
    public String getName() {
        return name;
    }
}