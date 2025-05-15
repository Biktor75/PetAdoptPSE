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
        System.out.println("🔐 Codificando contraseña...");
        user.setPassword(AuthenticationUtils.encodeSHA256(user.getPassword()));
        System.out.println("✅ Contraseña codificada");

        if (role == null || (!role.equalsIgnoreCase("cliente") && !role.equalsIgnoreCase("refugio"))) {
            throw new IllegalArgumentException("Rol no válido o no especificado: " + role);
        }

        String sanitizedRole = role.toLowerCase();
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
        System.out.println("📧 Email: " + refugio.getEmail());
        System.out.println("🔐 CIF: " + refugio.getCif());
        System.out.println("🏠 Domicilio: " + refugio.getDomicilio());
        System.out.println("📞 Teléfono: " + refugio.getTelefono());

        em.persist(refugio);
        System.out.println("✅ Refugio persistido");

    } catch (ConstraintViolationException e) {
        System.out.println("❌ Violaciones de validación:");
        for (ConstraintViolation<?> v : e.getConstraintViolations()) {
            System.out.println("🔴 " + v.getPropertyPath() + ": " + v.getMessage());
        }
    } catch (Exception e) {
        System.out.println("❌ ERROR general al persistir refugio:");
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
        System.out.println("❌ Error al buscar refugio por email:");
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
        System.out.println("❌ Error al buscar cliente por email:");
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
            em.merge(refugio); // ⬅️ Esto guarda el cambio
            System.out.println("✅ Refugio autorizado en DB: " + email);
        } else {
            System.out.println("⚠️ Refugio ya autorizado o no encontrado: " + email);
        }
    } catch (Exception e) {
        System.out.println("❌ ERROR al autorizar refugio:");
        e.printStackTrace();
    }
}






}
