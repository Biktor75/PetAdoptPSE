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

    // Método para crear un usuario y su grupo
    public Users createUser(Users user, String role) {
        try {
            System.out.println("🔐 Codificando contraseña...");
            user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword())); // Codificación de la contraseña
            System.out.println("✅ Contraseña codificada");

            // Fallback al rol "cliente" si no se especifica un rol
            String sanitizedRole = (role != null && !role.isEmpty()) ? role : "cliente"; 
            
            // Crear el grupo de usuario
            UserGroups group = new UserGroups();
            group.setUserGroupsPK(new UserGroupsPK(user.getEmail(), sanitizedRole)); // Asocia el grupo con el usuario
            System.out.println("🧷 Grupo creado: " + sanitizedRole);

            // Persistir el usuario
            em.persist(user);
            System.out.println("✅ Usuario persistido");

            // Persistir el grupo
            em.persist(group);
            System.out.println("✅ Grupo persistido");

            return user;
        } catch (Exception e) {
            System.out.println("❌ ERROR EN createUser:");
            e.printStackTrace();
            return null;
        }
    }

    // Buscar un usuario por su email
    public Users findByEmail(String email) {
        TypedQuery<Users> query = em.createNamedQuery("Users.findByEmail", Users.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult(); // Devuelve el usuario encontrado
        } catch (Exception e) {
            return null; // Si no se encuentra el usuario, devuelve null
        }
    }

    // Persistir un cliente en la base de datos
    public void persistCliente(Clientes cliente) {
        try {
            em.persist(cliente); // Persistir la entidad Cliente
            System.out.println("✅ Cliente persistido");
        } catch (Exception e) {
            System.out.println("❌ ERROR al persistir cliente:");
            e.printStackTrace();
        }
    }

    // Persistir un refugio en la base de datos
    public void persistRefugio(Refugios refugio) {
        try {
            em.persist(refugio); // Persistir la entidad Refugio
            System.out.println("✅ Refugio persistido");
        } catch (Exception e) {
            System.out.println("❌ ERROR al persistir refugio:");
            e.printStackTrace();
        }
    }
}
