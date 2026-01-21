package lml.snir.parklogickit.metier.entity;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 *
 * @author ethan
 */
@Entity
public class Badge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    /**
     * Récupérer l'identifiant unique du Badge
     * @return id : Identifiant Unique du Badge
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifier l'identifiant unique du Badge
     * @param id : L'identifiant unique du Badge
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupérer le contenu du Badge.
     * @return content : Retourne le contenu du Badge
     */
    public String getContent() {
        return content;
    }

    /**
     * Modifier le contenu du Badge
     * @param content : Le contenu du Badge a modifier
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Vérifier si un objet est égal au Badge
     * @param object : L'objet a vérifier
     * @return True ou False
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Badge)) {
            return false;
        }
        Badge other = (Badge) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    /**
     * Retourne le Badge sous forme de String
     * @return Badge sous forme de String.
     */
    @Override
    public String toString() {
        return "Badge n°"+getId()+", Contenu: "+getContent();
    }
    /**
     * Retourne un identifiant unique correspondant au Badge uniquement
     * @return Identifiant unique correspondant au Badge uniquement.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.content);
        return hash;
    }
}