/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.rest;

import com.mycompany.petadopt.entities.UserGroups;
import com.mycompany.petadopt.entities.UserGroupsPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;


@Stateless
@Path("com.mycompany.petadopt.entities.usergroups")
public class UserGroupsFacadeREST extends AbstractFacade<UserGroups> {

    @PersistenceContext(unitName = "com.mycompany_PetAdopt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private UserGroupsPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;email=emailValue;groupname=groupnameValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.mycompany.petadopt.entities.UserGroupsPK key = new com.mycompany.petadopt.entities.UserGroupsPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> email = map.get("email");
        if (email != null && !email.isEmpty()) {
            key.setEmail(email.get(0));
        }
        java.util.List<String> groupname = map.get("groupname");
        if (groupname != null && !groupname.isEmpty()) {
            key.setGroupname(groupname.get(0));
        }
        return key;
    }

    public UserGroupsFacadeREST() {
        super(UserGroups.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(UserGroups entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, UserGroups entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.mycompany.petadopt.entities.UserGroupsPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public UserGroups find(@PathParam("id") PathSegment id) {
        com.mycompany.petadopt.entities.UserGroupsPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserGroups> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserGroups> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
