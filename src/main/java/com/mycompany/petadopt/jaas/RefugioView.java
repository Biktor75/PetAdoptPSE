package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Refugios;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import com.mycompany.petadopt.servicios.AdopcionService;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Named
@SessionScoped
public class RefugioView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserEJB userEJB;

    @Inject
    private LoginView loginView;

    private Refugios refugio;

    @PostConstruct
    public void init() {

        if (loginView.getAuthenticatedUser() != null) {
            String email = loginView.getAuthenticatedUser().getEmail();
            this.refugio = userEJB.findRefugioByEmail(email);
        }
    }

    public Refugios getRefugio() {
        return refugio;
    }

    public void guardarDatosRefugio() {
        try {
            String email = refugio.getEmail();

            if (email == null || email.trim().isEmpty()) {
                return;
            }

            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newClient();

            javax.ws.rs.core.Response response = client
                    .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.refugios")
                    .path(email)
                    .request()
                    .put(javax.ws.rs.client.Entity.entity(refugio, javax.ws.rs.core.MediaType.APPLICATION_JSON));

            int status = response.getStatus();
            String body = response.readEntity(String.class); // intenta leer cuerpo de respuesta si lo hay

            response.close();
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean editandoTelefono = false;

    public boolean isEditandoTelefono() {
        return editandoTelefono;
    }

    public void setEditandoTelefono(boolean editandoTelefono) {
        this.editandoTelefono = editandoTelefono;
    }

    public void activarEdicionTelefono() {
        this.editandoTelefono = true;
    }

    public void cancelarEdicionTelefono() {
        this.editandoTelefono = false;
    }

    public void guardarTelefono() {

        System.out.println("🔧 Llamado a guardarTelefono()");
        guardarDatosRefugio();
        this.editandoTelefono = false;
    }

}
