package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Clientes;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.persistence.TypedQuery;

@Named
@SessionScoped
public class ClienteView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private UserEJB userEJB;

    @Inject
    private LoginView loginView;

    private Clientes cliente;

    @PostConstruct
    public void init() {

        if (loginView.getAuthenticatedUser() != null) {
            String email = loginView.getAuthenticatedUser().getEmail();
            this.cliente = userEJB.findClienteByEmail(email);
        }
    }

    public Clientes getCliente() {
        return cliente;
    }

    private boolean editandoTelefono = false;
    private boolean editandoDomicilio = false;

    public boolean isEditandoTelefono() {
        return editandoTelefono;
    }

    public boolean isEditandoDomicilio() {
        return editandoDomicilio;
    }

    public void activarEdicionTelefono() {
        this.editandoTelefono = true;
    }

    public void cancelarEdicionTelefono() {
        this.editandoTelefono = false;
    }

    public void activarEdicionDomicilio() {
        this.editandoDomicilio = true;
    }

    public void cancelarEdicionDomicilio() {
        this.editandoDomicilio = false;
    }

    public void guardarTelefono() {
        actualizarCliente();
        this.editandoTelefono = false;
    }

    public void guardarDomicilio() {
        actualizarCliente();
        this.editandoDomicilio = false;
    }

    private void actualizarCliente() {
        try {
            String email = cliente.getEmail();

            if (email == null || email.trim().isEmpty()) {
                return;
            }

            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newClient();

            javax.ws.rs.core.Response response = client
                    .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.clientes")
                    .path(email)
                    .request()
                    .put(javax.ws.rs.client.Entity.entity(cliente, javax.ws.rs.core.MediaType.APPLICATION_JSON));

            int status = response.getStatus();
            String body = response.readEntity(String.class);

            response.close();
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
