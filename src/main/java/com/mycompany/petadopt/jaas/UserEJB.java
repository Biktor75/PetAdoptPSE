package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Users;
import com.mycompany.petadopt.entities.UserGroups;
import com.mycompany.petadopt.entities.UserGroupsPK;
import com.mycompany.petadopt.entities.Clientes;
import com.mycompany.petadopt.entities.Refugios;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    public Users createUser(Users user, String role) {
        try {
            System.out.println("🔐 Codificando contraseña...");
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
            System.out.println("✅ Contraseña codificada");

            String sanitizedRole = (role != null && !role.isEmpty()) ? role.toLowerCase() : "cliente";

            UserGroups group = new UserGroups();
            group.setUserGroupsPK(new UserGroupsPK(user.getEmail(), sanitizedRole));
            System.out.println("🧷 Grupo creado: " + sanitizedRole);

            em.persist(user);
            System.out.println("✅ Usuario persistido");

            em.persist(group);
            System.out.println("✅ Grupo persistido");

            return user;
        } catch (Exception e) {
            System.out.println("❌ ERROR EN createUser:");
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
            System.out.println("✅ Cliente persistido");
        } catch (Exception e) {
            System.out.println("❌ ERROR al persistir cliente:");
            e.printStackTrace();
        }
    }

    public void persistRefugio(Refugios refugio) {
        try {
            em.persist(refugio);
            System.out.println("✅ Refugio persistido");
        } catch (Exception e) {
            System.out.println("❌ ERROR al persistir refugio:");
            e.printStackTrace();
        }
    }
}
