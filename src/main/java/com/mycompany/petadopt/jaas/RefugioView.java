package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Refugios;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Named
@RequestScoped
public class RefugioView {

    @Inject
    private UserEJB userEJB;

    @Inject
    private LoginView loginView;

    private Refugios refugio;

    @PostConstruct
    public void init() {
        System.out.println("🟡 RefugioView inicializado");

        if (loginView.getAuthenticatedUser() != null) {
            String email = loginView.getAuthenticatedUser().getEmail();
            System.out.println("🔍 Buscando refugio con email: " + email);
            this.refugio = userEJB.findRefugioByEmail(email);

            if (refugio != null) {
                System.out.println("✅ Refugio encontrado: CIF = " + refugio.getCif());
            } else {
                System.out.println("❌ No se encontró ningún refugio con ese email");
            }
        } else {
            System.out.println("❌ Usuario no autenticado en LoginView");
        }
    }

    public Refugios getRefugio() {
        return refugio;
    }
}
