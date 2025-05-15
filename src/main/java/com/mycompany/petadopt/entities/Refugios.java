/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author victo
 */
@Entity
@Table(name = "refugios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Refugios.findAll", query = "SELECT r FROM Refugios r"),
    @NamedQuery(name = "Refugios.findByEmail", query = "SELECT r FROM Refugios r WHERE r.email = :email"),
    @NamedQuery(name = "Refugios.findByCif", query = "SELECT r FROM Refugios r WHERE r.cif = :cif"),
    @NamedQuery(name = "Refugios.findByDomicilio", query = "SELECT r FROM Refugios r WHERE r.domicilio = :domicilio"),
    @NamedQuery(name = "Refugios.findByTelefono", query = "SELECT r FROM Refugios r WHERE r.telefono = :telefono"),
    @NamedQuery(name = "Refugios.findByAutorizado", query = "SELECT r FROM Refugios r WHERE r.autorizado = :autorizado")})
public class Refugios implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cif")
    private String cif;
    @Size(max = 255)
    @Column(name = "domicilio")
    private String domicilio;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "autorizado")
    private Boolean autorizado;


    public Refugios() {
    }

    public Refugios(String email) {
        this.email = email;
    }

    public Refugios(String email, String cif) {
        this.email = email;
        this.cif = cif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Boolean autorizado) {
        this.autorizado = autorizado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Refugios)) {
            return false;
        }
        Refugios other = (Refugios) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.petadopt.entities.Refugios[ email=" + email + " ]";
    }

    public void setUsers(Users user) {
        // Igual que antes: si no hay relación directa con `Users`, puedes eliminar este método.
    }

    
}
