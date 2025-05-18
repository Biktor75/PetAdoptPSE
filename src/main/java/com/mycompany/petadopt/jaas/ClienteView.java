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
        System.out.println("📞 Guardando teléfono: " + cliente.getTelefono());
        actualizarCliente();
        this.editandoTelefono = false;
    }

    public void guardarDomicilio() {
        System.out.println("🏠 Guardando domicilio: " + cliente.getDomicilio());
        actualizarCliente();
        this.editandoDomicilio = false;
    }

    private void actualizarCliente() {
        try {
            String email = cliente.getEmail();

            if (email == null || email.trim().isEmpty()) {
                System.err.println("❌ Email del cliente es null o vacío. No se puede actualizar.");
                return;
            }

            System.out.println("💾 Actualizando datos del cliente:");
            System.out.println("📧 Email: " + email);
            System.out.println("🏠 Domicilio: " + cliente.getDomicilio());
            System.out.println("📞 Teléfono: " + cliente.getTelefono());

            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newClient();

            javax.ws.rs.core.Response response = client
                    .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.clientes")
                    .path(email)
                    .request()
                    .put(javax.ws.rs.client.Entity.entity(cliente, javax.ws.rs.core.MediaType.APPLICATION_JSON));

            int status = response.getStatus();
            String body = response.readEntity(String.class);

            System.out.println("🔁 Código de respuesta HTTP: " + status);
            System.out.println("📦 Cuerpo de respuesta: " + body);

            if (status == 200 || status == 204) {
                System.out.println("✅ Datos del cliente actualizados correctamente.");
            } else {
                System.err.println("❌ Error al guardar cliente: Código " + status);
            }

            response.close();
            client.close();

        } catch (Exception e) {
            System.err.println("❌ Excepción al actualizar cliente:");
            e.printStackTrace();
        }
    }

}
