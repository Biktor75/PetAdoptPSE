package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Users;
import com.mycompany.petadopt.entities.UserGroups;
import com.mycompany.petadopt.entities.UserGroupsPK;
import com.mycompany.petadopt.entities.Clientes;
import com.mycompany.petadopt.entities.Refugios;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    public Users createUser(Users user, String role) {
        try {
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));

            if (role == null || (!role.equalsIgnoreCase("cliente") && !role.equalsIgnoreCase("refugio"))) {
                throw new IllegalArgumentException("Rol no válido o no especificado: " + role);
            }

            String sanitizedRole = role.toLowerCase();
            UserGroups group = new UserGroups();
            group.setUserGroupsPK(new UserGroupsPK(user.getEmail(), sanitizedRole));

            em.persist(user);

            em.persist(group);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Users findByEmail(String email) {
        TypedQuery<Users> query = em.createNamedQuery("Users.findByEmail", Users.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void persistCliente(Clientes cliente) {
        try {
            em.persist(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void persistRefugio(Refugios refugio) {
        try {

            em.persist(refugio);

        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> v : e.getConstraintViolations()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Refugios findRefugioByEmail(String email) {
        try {
            TypedQuery<Refugios> query = em.createQuery(
                    "SELECT r FROM Refugios r WHERE r.email = :email", Refugios.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Clientes findClienteByEmail(String email) {
        try {
            TypedQuery<Clientes> query = em.createQuery(
                    "SELECT c FROM Clientes c WHERE c.email = :email", Clientes.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Refugios> findRefugiosNoAutorizados() {
        return em.createQuery("SELECT r FROM Refugios r WHERE r.autorizado = false", Refugios.class)
                .getResultList();
    }

    public void autorizarRefugio(String email) {
        try {
            Refugios refugio = em.find(Refugios.class, email);
            if (refugio != null && Boolean.FALSE.equals(refugio.getAutorizado())) {
                refugio.setAutorizado(true);
                em.merge(refugio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
