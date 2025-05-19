/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "mascotas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mascotas.findAll", query = "SELECT m FROM Mascotas m"),
    @NamedQuery(name = "Mascotas.findById", query = "SELECT m FROM Mascotas m WHERE m.id = :id"),
    @NamedQuery(name = "Mascotas.findByNombre", query = "SELECT m FROM Mascotas m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Mascotas.findByEspecie", query = "SELECT m FROM Mascotas m WHERE m.especie = :especie"),
    @NamedQuery(name = "Mascotas.findByRaza", query = "SELECT m FROM Mascotas m WHERE m.raza = :raza"),
    @NamedQuery(name = "Mascotas.findByEdad", query = "SELECT m FROM Mascotas m WHERE m.edad = :edad"),
    @NamedQuery(name = "Mascotas.findByEstadoSalud", query = "SELECT m FROM Mascotas m WHERE m.estadoSalud = :estadoSalud"),
    @NamedQuery(name = "Mascotas.findByCosteAdopcion", query = "SELECT m FROM Mascotas m WHERE m.costeAdopcion = :costeAdopcion"),
    @NamedQuery(name = "Mascotas.findByRefugioEmail", query = "SELECT m FROM Mascotas m WHERE m.refugioEmail = :refugioEmail")})
public class Mascotas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 100)
    @Column(name = "especie")
    private String especie;
    @Size(max = 100)
    @Column(name = "raza")
    private String raza;
    @Column(name = "edad")
    private Integer edad;
    @Size(max = 255)
    @Column(name = "estado_salud")
    private String estadoSalud;
    @Column(name = "coste_adopcion")
    private BigDecimal costeAdopcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "refugio_email")
    private String refugioEmail;

    public Mascotas() {
    }

    public Mascotas(Integer id) {
        this.id = id;
    }

    public Mascotas(Integer id, String refugioEmail) {
        this.id = id;
        this.refugioEmail = refugioEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public BigDecimal getCosteAdopcion() {
        return costeAdopcion;
    }

    public void setCosteAdopcion(BigDecimal costeAdopcion) {
        this.costeAdopcion = costeAdopcion;
    }

    public String getRefugioEmail() {
        return refugioEmail;
    }

    public void setRefugioEmail(String refugioEmail) {
        this.refugioEmail = refugioEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Mascotas)) {
            return false;
        }
        Mascotas other = (Mascotas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.petadopt.entities.Mascotas[ id=" + id + " ]";
    }
    
}
