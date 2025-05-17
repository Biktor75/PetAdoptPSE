/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.rest;

import com.mycompany.petadopt.entities.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author victo
 */
@Stateless
@Path("com.mycompany.petadopt.entities.users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "com.mycompany_PetAdopt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Users entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Users entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @DELETE
    @Path("email/{email}")
    public void deleteByEmail(@PathParam("email") String email) {
        Users u = em.find(Users.class, email);
        if (u == null) {
            throw new NotFoundException("Usuario no encontrado");
        }

        // 1. Borrar de user_groups (si existe)
        em.createQuery("DELETE FROM UserGroups ug WHERE ug.userGroupsPK.email = :email")
                .setParameter("email", email)
                .executeUpdate();

        // 2. Borrar de clientes si existe
        em.createQuery("DELETE FROM Clientes c WHERE c.email = :email")
                .setParameter("email", email)
                .executeUpdate();

        // 3. Borrar de refugios si existe
        em.createQuery("DELETE FROM Refugios r WHERE r.email = :email")
                .setParameter("email", email)
                .executeUpdate();

        // 4. Borrar solicitudes y mascotas (opcional, si no lo haces ya)
        em.createQuery("DELETE FROM SolicitudesAdopcion s WHERE s.clienteEmail = :email")
                .setParameter("email", email)
                .executeUpdate();

        em.createQuery("DELETE FROM Mascotas m WHERE m.refugioEmail = :email")
                .setParameter("email", email)
                .executeUpdate();

        // 5. Finalmente, eliminar de users
        em.remove(u);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Users find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
