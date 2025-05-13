package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Users;
import com.mycompany.petadopt.entities.UserGroups;
import com.mycompany.petadopt.entities.UserGroupsPK;

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
            user.setPassword(com.mycompany.petadopt.jaas.AuthenticationUtils.encodeSHA256(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserGroups group = new UserGroups();
        group.setUserGroupsPK(new UserGroupsPK(user.getEmail(), role)); // "cliente" o "refugio"

        em.persist(user);
        em.persist(group);

        return user;
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
}
