package lml.snir.parkinglogickit.client.beans;

/**
 * Petit objet d'affichage utilisé par le tableau de bord.
 * Il permet de préparer les informations d'un parking avant de les envoyer à
 * la page JSF, sans mettre de calculs directement dans le XHTML.
 *
 * @author Sylvain Crocquevieille
 */
public class ParkingCard {

    private final String nom;
    private final int totalPlaces;
    private final int placesLibres;

    public ParkingCard(String nom, int totalPlaces, int placesLibres) {
        this.nom = nom;
        this.totalPlaces = totalPlaces;
        this.placesLibres = placesLibres;
    }

    public boolean isComplet() {
        return placesLibres >= totalPlaces;
    }

    public String getNom() {
        return nom;
    }

    public int getTotalPlaces() {
        return totalPlaces;
    }

    public int getPlacesLibres() {
        return placesLibres;
    }
}
