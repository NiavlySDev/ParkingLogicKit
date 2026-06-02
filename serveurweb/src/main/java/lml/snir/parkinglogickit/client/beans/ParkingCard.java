package lml.snir.parkinglogickit.client.beans;

/**
 * Petit objet d'affichage utilisé par le tableau de bord. Il permet de préparer
 * les informations d'un parking avant de les envoyer à la page JSF, sans mettre
 * de calculs directement dans le XHTML.
 *
 * @author Sylvain Crocquevieille
 */
public class ParkingCard {

    private final String nom;
    private final int totalPlaces;
    private final int placesLibres;

    /**
     * Construit une instance de ParkingCard.
     *
     * @param nom : paramètre utilisé par la méthode
     * @param totalPlaces : paramètre utilisé par la méthode
     * @param placesLibres : paramètre utilisé par la méthode
     */
    public ParkingCard(String nom, int totalPlaces, int placesLibres) {
        this.nom = nom;
        this.totalPlaces = totalPlaces;
        this.placesLibres = placesLibres;
    }

    /**
     * Indique si complet.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isComplet() {
        return placesLibres >= totalPlaces;
    }

    /**
     * Retourne nom.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne total places.
     *
     * @return int : valeur retournée par la méthode
     */
    public int getTotalPlaces() {
        return totalPlaces;
    }

    /**
     * Retourne places libres.
     *
     * @return int : valeur retournée par la méthode
     */
    public int getPlacesLibres() {
        return placesLibres;
    }
}
