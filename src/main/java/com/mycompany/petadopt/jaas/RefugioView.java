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

    public void guardarDatosRefugio() {
        try {
            String email = refugio.getEmail();

            if (email == null || email.trim().isEmpty()) {
                System.err.println("❌ Email del refugio es null o vacío. No se puede actualizar.");
                return;
            }

            // Mostramos todos los datos que vamos a enviar
            System.out.println("💾 Enviando datos actualizados del refugio:");
            System.out.println("📧 Email: " + email);
            System.out.println("🏠 Domicilio: " + refugio.getDomicilio());
            System.out.println("📞 Teléfono: " + refugio.getTelefono());
            System.out.println("📜 CIF: " + refugio.getCif());
            System.out.println("✅ Autorizado: " + refugio.getAutorizado());

            javax.ws.rs.client.Client client = javax.ws.rs.client.ClientBuilder.newClient();

            javax.ws.rs.core.Response response = client
                    .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.refugios")
                    .path(email)
                    .request()
                    .put(javax.ws.rs.client.Entity.entity(refugio, javax.ws.rs.core.MediaType.APPLICATION_JSON));

            int status = response.getStatus();
            String body = response.readEntity(String.class); // intenta leer cuerpo de respuesta si lo hay

            System.out.println("🔁 Código de respuesta HTTP: " + status);
            System.out.println("📦 Cuerpo de respuesta: " + body);

            if (status == 200 || status == 204) {
                System.out.println("✅ Datos del refugio actualizados correctamente.");
            } else {
                System.err.println("❌ Error al guardar: Código " + status);
            }

            response.close();
            client.close();

        } catch (Exception e) {
            System.err.println("❌ Excepción al actualizar datos del refugio:");
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
