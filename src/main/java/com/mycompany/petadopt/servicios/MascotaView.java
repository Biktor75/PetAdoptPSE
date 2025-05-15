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
    private Mascotas mascotaSeleccionada = new Mascotas();

    @Inject
    private LoginView loginView;

    private final Client client = ClientBuilder.newClient();

    @PostConstruct
    public void init() {
        try {
            if (loginView.getAuthenticatedUser() != null) {
                cargarMascotas();
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
            String email = loginView.getAuthenticatedUser().getEmail();
            System.out.println("🔍 Cargando mascotas del refugio: " + email);

            mascotas = client
                    .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                    .path("refugio/" + email)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Mascotas>>() {
                    });

            System.out.println("✅ Mascotas cargadas: " + mascotas.size());
        } catch (Exception e) {
            System.err.println("❌ Error al cargar mascotas:");
            e.printStackTrace();
        }
    }

    public String prepararNuevaMascota() {
        mascotaSeleccionada = new Mascotas(); // nueva instancia vacía
        return "/refugios/nuevaMascota.xhtml?faces-redirect=true";
    }

    public String cargarParaEditar(Mascotas m) {
        mascotaSeleccionada = m;
        return "/refugios/nuevaMascota.xhtml?faces-redirect=true";
    }

    public String guardarMascota() {
        try {
            mascotaSeleccionada.setRefugioEmail(loginView.getAuthenticatedUser().getEmail());

            if (mascotaSeleccionada.getId() == null) {
                // Crear
                client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                        .request()
                        .post(Entity.entity(mascotaSeleccionada, MediaType.APPLICATION_JSON));
                System.out.println("✅ Nueva mascota creada");
            } else {
                // Editar
                client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                        .path(String.valueOf(mascotaSeleccionada.getId()))
                        .request()
                        .put(Entity.entity(mascotaSeleccionada, MediaType.APPLICATION_JSON));
                System.out.println("✅ Mascota actualizada");
            }

            cargarMascotas();
            return "/refugios/mascotas.xhtml?faces-redirect=true";
        } catch (Exception e) {
            System.err.println("❌ Error al guardar mascota:");
            e.printStackTrace();
            return null;
        }
    }

    public void eliminarMascota(Mascotas m) {
        client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                .path(String.valueOf(m.getId()))
                .request()
                .delete();
        cargarMascotas();
    }

    public String prepararEdicion(Mascotas m) {
        this.mascotaSeleccionada = m;
        return "/refugios/nuevaMascota.xhtml?faces-redirect=true";
    }

    // Getters y setters
    public List<Mascotas> getMascotas() {
        return mascotas;
    }

    public Mascotas getMascotaSeleccionada() {
        return mascotaSeleccionada;
    }

    public void setMascotaSeleccionada(Mascotas mascotaSeleccionada) {
        this.mascotaSeleccionada = mascotaSeleccionada;
    }
}
