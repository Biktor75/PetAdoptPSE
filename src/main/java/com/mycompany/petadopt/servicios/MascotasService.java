package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Mascotas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
}
