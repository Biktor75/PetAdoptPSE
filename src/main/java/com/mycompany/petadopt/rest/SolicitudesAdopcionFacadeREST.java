package com.mycompany.petadopt.rest;

import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import com.mycompany.petadopt.entities.Mascotas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("com.mycompany.petadopt.entities.solicitudesadopcion")
public class SolicitudesAdopcionFacadeREST extends AbstractFacade<SolicitudesAdopcion> {

    @PersistenceContext(unitName = "com.mycompany_PetAdopt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public SolicitudesAdopcionFacadeREST() {
        super(SolicitudesAdopcion.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(SolicitudesAdopcion entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, SolicitudesAdopcion entity) {
        SolicitudesAdopcion original = super.find(id);

        if (original == null) {
            throw new NotFoundException("Solicitud no encontrada");
        }

        original.setEstado(entity.getEstado());

        // ✅ Si se acepta, rechazar las demás solicitudes para esa mascota
        if ("aceptada".equalsIgnoreCase(entity.getEstado())) {
            em.createQuery("UPDATE SolicitudesAdopcion s SET s.estado = 'rechazada' "
                    + "WHERE s.mascotaId = :mid AND s.id <> :sid")
                    .setParameter("mid", original.getMascotaId())
                    .setParameter("sid", original.getId())
                    .executeUpdate();
        }

        super.edit(original);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public SolicitudesAdopcion find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<SolicitudesAdopcion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<SolicitudesAdopcion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("cliente/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SolicitudesAdopcion> findByCliente(@PathParam("email") String email) {
        return em.createQuery("SELECT s FROM SolicitudesAdopcion s WHERE s.clienteEmail = :email", SolicitudesAdopcion.class)
                .setParameter("email", email)
                .getResultList();
    }

    @GET
    @Path("refugio/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SolicitudesAdopcion> findByRefugioEmail(@PathParam("email") String email) {
        return em.createQuery(
                "SELECT s FROM SolicitudesAdopcion s WHERE s.mascotaId IN "
                + "(SELECT m.id FROM Mascotas m WHERE m.refugioEmail = :email)",
                SolicitudesAdopcion.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
