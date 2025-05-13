/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author victo
 */
@Entity
@Table(name = "mensajes_chat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MensajesChat.findAll", query = "SELECT m FROM MensajesChat m"),
    @NamedQuery(name = "MensajesChat.findById", query = "SELECT m FROM MensajesChat m WHERE m.id = :id"),
    @NamedQuery(name = "MensajesChat.findByEmisorEmail", query = "SELECT m FROM MensajesChat m WHERE m.emisorEmail = :emisorEmail"),
    @NamedQuery(name = "MensajesChat.findByReceptorEmail", query = "SELECT m FROM MensajesChat m WHERE m.receptorEmail = :receptorEmail"),
    @NamedQuery(name = "MensajesChat.findByContenido", query = "SELECT m FROM MensajesChat m WHERE m.contenido = :contenido"),
    @NamedQuery(name = "MensajesChat.findByFecha", query = "SELECT m FROM MensajesChat m WHERE m.fecha = :fecha")})
public class MensajesChat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "emisor_email")
    private String emisorEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "receptor_email")
    private String receptorEmail;
    @Size(max = 1000)
    @Column(name = "contenido")
    private String contenido;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public MensajesChat() {
    }

    public MensajesChat(Integer id) {
        this.id = id;
    }

    public MensajesChat(Integer id, String emisorEmail, String receptorEmail) {
        this.id = id;
        this.emisorEmail = emisorEmail;
        this.receptorEmail = receptorEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmisorEmail() {
        return emisorEmail;
    }

    public void setEmisorEmail(String emisorEmail) {
        this.emisorEmail = emisorEmail;
    }

    public String getReceptorEmail() {
        return receptorEmail;
    }

    public void setReceptorEmail(String receptorEmail) {
        this.receptorEmail = receptorEmail;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajesChat)) {
            return false;
        }
        MensajesChat other = (MensajesChat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.petadopt.entities.MensajesChat[ id=" + id + " ]";
    }
    
}
