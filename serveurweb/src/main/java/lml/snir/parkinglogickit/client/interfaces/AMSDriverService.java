package lml.snir.parkinglogickit.client.interfaces;

import java.util.Random;

/**
 * @author Sylvain Crocquevieille
 */
public interface AMSDriverService {

    /**
     * Retourne id.
     *
     * @return Long : valeur retournée par la méthode
     */
    public Long getId();

    /**
     * Modifie id.
     *
     * @param id : paramètre utilisé par la méthode
     */
    public void setId(Long id);

    /**
     * Retourne prenom.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getPrenom();

    /**
     * Modifie prenom.
     *
     * @param prenom : paramètre utilisé par la méthode
     */
    public void setPrenom(String prenom);

    /**
     * Retourne nom.
     *
     * @return String : valeur retournée par la méthode
     */
    public String getNom();

    /**
     * Modifie nom.
     *
     * @param nom : paramètre utilisé par la méthode
     */
    public void setNom(String nom);

    /**
     * Retourne age.
     *
     * @return Integer : valeur retournée par la méthode
     */
    public Integer getAge();

    /**
     * Modifie age.
     *
     * @param age : paramètre utilisé par la méthode
     */
    public void setAge(Integer age);

    /**
     * Retourne login.
     *
     * @return String : valeur retournée par la méthode
     */
    public default String getLogin() {
        Random random = new Random();
        Integer integer = random.nextInt(10, 100);
        return getPrenom().toLowerCase() + "." + getNom().toLowerCase() + integer.toString();
    }

    /**
     * Indique si masculin.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isMasculin();

    /**
     * Modifie masculin.
     *
     * @param masculin : paramètre utilisé par la méthode
     */
    public void setMasculin(boolean masculin);

    /**
     * Indique si admin.
     *
     * @return boolean : valeur retournée par la méthode
     */
    public boolean isAdmin();

    /**
     * Modifie admin.
     *
     * @param admin : paramètre utilisé par la méthode
     */
    public void setAdmin(boolean admin);

    /**
     * Retourne password.
     *
     * @return String : valeur retournée par la méthode
     */
    public default String getPassword() {
        return this.getPrenom().toLowerCase().substring(0, 0) + this.getNom().toLowerCase().substring(0, 0) + this.getAge();
    }
}
