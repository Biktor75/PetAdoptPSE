package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

@Stateless
public class MascotasService {

    @PersistenceContext(unitName = "com.mycompany_PetAdopt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public List<Mascotas> findByEspecie(String especie) {
        return em.createNamedQuery("Mascotas.findByEspecie", Mascotas.class)
                .setParameter("especie", especie)
                .getResultList();
    }

    public Mascotas findById(Integer id) {
        return em.find(Mascotas.class, id);
    }

    public List<SolicitudesAdopcion> getTodasSolicitudes() {
        Client client = ClientBuilder.newClient(); // ← construirlo aquí
        List<SolicitudesAdopcion> solicitudes = client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SolicitudesAdopcion>>() {
                });
        client.close(); // buena práctica
        return solicitudes;
    }

}
