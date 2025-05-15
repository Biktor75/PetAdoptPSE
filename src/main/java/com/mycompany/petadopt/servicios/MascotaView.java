package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.jaas.LoginView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class MascotaView implements Serializable {

    private List<Mascotas> mascotas;

    private Mascotas nuevaMascota = new Mascotas();

    @Inject
    private LoginView loginView;

    private final Client client = ClientBuilder.newClient();

    @PostConstruct
    public void init() {
        try {
            if (loginView.getAuthenticatedUser() != null) {
                cargarMascotas(); // ✅ CORRECTO
            } else {
                System.err.println("⚠️ Usuario no autenticado en MascotaView");
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR en init() de MascotaView:");
            e.printStackTrace();
        }
    }

    public void cargarMascotas() {
        try {
            String refugioEmail = loginView.getAuthenticatedUser().getEmail();
            System.out.println("🔍 Cargando mascotas del refugio: " + refugioEmail);

            mascotas = client
                    .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                    .path("refugio/" + refugioEmail)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Mascotas>>() {
                    });

            System.out.println("✅ Mascotas cargadas: " + mascotas.size());
        } catch (Exception e) {
            System.err.println("❌ Error al cargar mascotas:");
            e.printStackTrace();
        }
    }

    public String guardarNuevaMascota() {
        nuevaMascota.setRefugioEmail(loginView.getAuthenticatedUser().getEmail());

        client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
              .request()
                .post(Entity.entity(nuevaMascota, MediaType.APPLICATION_JSON));

        nuevaMascota = new Mascotas(); // reset
        cargarMascotas(); // recargar lista
        return "/refugios/mascotas.xhtml?faces-redirect=true";
    }
    private Mascotas mascotaSeleccionada;

    public void cargarParaEditar(Mascotas m) {
        this.mascotaSeleccionada = m;
    }

    public void guardarEdicion() {
        try {
            System.out.println("✏️ Editando mascota: " + mascotaSeleccionada.getId());

            client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                    .path(String.valueOf(mascotaSeleccionada.getId()))
                    .request()
                    .put(Entity.entity(mascotaSeleccionada, MediaType.APPLICATION_JSON));

            cargarMascotas();  // Refrescar la lista en la tabla
            System.out.println("✅ Mascota actualizada");

        } catch (Exception e) {
            System.err.println("❌ Error al guardar edición:");
            e.printStackTrace();
        }
    }

    public void eliminarMascota(Mascotas m) {
        client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                .path(String.valueOf(m.getId()))
                .request()
                .delete();

        cargarMascotas();
    }

    public Mascotas getMascotaSeleccionada() {
        return mascotaSeleccionada;
    }

    public void setMascotaSeleccionada(Mascotas mascotaSeleccionada) {
        this.mascotaSeleccionada = mascotaSeleccionada;
    }


    // Getters y setters
    public List<Mascotas> getMascotas() { return mascotas; }
    public Mascotas getNuevaMascota() { return nuevaMascota; }
    public void setNuevaMascota(Mascotas nuevaMascota) { this.nuevaMascota = nuevaMascota; }
}
