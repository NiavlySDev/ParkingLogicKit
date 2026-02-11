/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.client.fakedata;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author sylvain
 */
public class Associate implements Serializable {
    private Long id;
    private Badge badge;
    private Driver driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
 
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Associate)) {
            return false;
        }
        Associate other = (Associate) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.badge);
        hash = 79 * hash + Objects.hashCode(this.driver);
        return hash;
    }

    @Override
    public String toString() {
        return "lml.snir.gestiontemperature.metier.entity.Attribution[ id=" + getId() + " ]";
    }

    /**
     * @return the badge
     */
    public Badge getBadge() {
        return badge;
    }

    /**
     * @param badge the badge to set
     */
    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    /**
     * @return the utilisateur
     */
    public Driver getUtilisateur() {
        return driver;
    }

    /**
     * @param utilisateur the utilisateur to set
     */
    public void setUtilisateur(Driver utilisateur) {
        this.driver = utilisateur;
    }
}
