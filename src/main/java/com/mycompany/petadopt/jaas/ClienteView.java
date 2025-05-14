package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Clientes;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;

@Named
@RequestScoped
public class ClienteView {

    @Inject
    private UserEJB userEJB;

    @Inject
    private LoginView loginView;

    private Clientes cliente;

    @PostConstruct
    public void init() {
        System.out.println("🟡 ClienteView inicializado");

        if (loginView.getAuthenticatedUser() != null) {
            String email = loginView.getAuthenticatedUser().getEmail();
            System.out.println("🔍 Buscando cliente con email: " + email);
            this.cliente = userEJB.findClienteByEmail(email);

            if (cliente != null) {
                System.out.println("✅ Cliente encontrado: NIF = " + cliente.getNif());
            } else {
                System.out.println("❌ No se encontró cliente");
            }
        } else {
            System.out.println("❌ Usuario no autenticado");
        }
    }

    public Clientes getCliente() {
        return cliente;
    }
}
