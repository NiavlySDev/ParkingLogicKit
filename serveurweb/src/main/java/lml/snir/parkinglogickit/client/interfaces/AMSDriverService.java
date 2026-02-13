package lml.snir.parkinglogickit.client.interfaces;

import java.util.Random;

/**
 *
 * @author Sylvain Crocquevieille
 */
public interface AMSDriverService {

    public Long getId();

    public void setId(Long id);

    public String getPrenom();

    public void setPrenom(String prenom);

    public String getNom();

    public void setNom(String nom);

    public Integer getAge();

    public void setAge(Integer age);

    public default String getLogin() {
        Random random = new Random();
        Integer integer = random.nextInt(10, 100);
        return getPrenom().toLowerCase() + "." + getNom().toLowerCase() + integer.toString();
    }

    public boolean isMasculin();

    public void setMasculin(boolean masculin);

    public boolean isAdmin();

    public void setAdmin(boolean admin);

    public default String getPassword() {
        return this.getPrenom().toLowerCase().substring(0, 0) + this.getNom().toLowerCase().substring(0, 0) + this.getAge();
    }
}
