package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
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
import java.util.Set;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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

            // Solo traer las que no han sido adoptadas
            mascotas = getMascotasDisponibles(email);

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
        try {
            client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas")
                    .path(String.valueOf(m.getId()))
                    .request()
                    .delete();

            cargarMascotas();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Mascota eliminada", "La mascota ha sido eliminada correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar", "No se pudo eliminar la mascota."));
            e.printStackTrace();
        }
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

    public List<Mascotas> getMascotasDisponibles(String refugioEmail) {
        List<Mascotas> todas = client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas/refugio/" + refugioEmail)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Mascotas>>() {
                });

        List<SolicitudesAdopcion> solicitudes = client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/refugio/" + refugioEmail)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SolicitudesAdopcion>>() {
                });

        // Recoger los IDs de mascotas adoptadas
        java.util.Set<Integer> idsAdoptadas = new java.util.HashSet<Integer>();
        for (SolicitudesAdopcion s : solicitudes) {
            if ("aceptada".equalsIgnoreCase(s.getEstado())) {
                idsAdoptadas.add(s.getMascotaId());
            }
        }

        // Filtrar solo las disponibles
        java.util.List<Mascotas> disponibles = new java.util.ArrayList<Mascotas>();
        for (Mascotas m : todas) {
            if (!idsAdoptadas.contains(m.getId())) {
                disponibles.add(m);
            }
        }

        return disponibles;
    }

    public List<SolicitudesAdopcion> getTodasSolicitudes() {
        return client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SolicitudesAdopcion>>() {
                });
    }

}
