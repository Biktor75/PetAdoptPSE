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

@Entity
@Table(name = "solicitudes_adopcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SolicitudesAdopcion.findAll", query = "SELECT s FROM SolicitudesAdopcion s"),
    @NamedQuery(name = "SolicitudesAdopcion.findById", query = "SELECT s FROM SolicitudesAdopcion s WHERE s.id = :id"),
    @NamedQuery(name = "SolicitudesAdopcion.findByClienteEmail", query = "SELECT s FROM SolicitudesAdopcion s WHERE s.clienteEmail = :clienteEmail"),
    @NamedQuery(name = "SolicitudesAdopcion.findByMascotaId", query = "SELECT s FROM SolicitudesAdopcion s WHERE s.mascotaId = :mascotaId"),
    @NamedQuery(name = "SolicitudesAdopcion.findByFechaSolicitud", query = "SELECT s FROM SolicitudesAdopcion s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SolicitudesAdopcion.findByEstado", query = "SELECT s FROM SolicitudesAdopcion s WHERE s.estado = :estado")})
public class SolicitudesAdopcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cliente_email")
    private String clienteEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mascota_id")
    private int mascotaId;
    @Column(name = "fecha_solicitud")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Size(max = 20)
    @Column(name = "estado")
    private String estado;

    public SolicitudesAdopcion() {
    }

    public SolicitudesAdopcion(Integer id) {
        this.id = id;
    }

    public SolicitudesAdopcion(Integer id, String clienteEmail, int mascotaId) {
        this.id = id;
        this.clienteEmail = clienteEmail;
        this.mascotaId = mascotaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SolicitudesAdopcion)) {
            return false;
        }
        SolicitudesAdopcion other = (SolicitudesAdopcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "com.mycompany.petadopt.entities.SolicitudesAdopcion[ id=" + id + " ]";
    }
    
}
