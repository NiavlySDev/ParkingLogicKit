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
    private String contenu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.contenu);
        return hash;
    }

    @Override
    public String toString() {
        return "lml.snir.gestiontemperature.metier.entity.Badge[ id=" + getId() + " ]";
    }

    /**
     * @return the contenu
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * @param contenu the contenu to set
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
}
