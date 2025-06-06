package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.adopcion.Adopcion;
import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import com.mycompany.petadopt.jaas.LoginView;
import java.io.StringReader;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

@Named
@RequestScoped
public class AdopcionService {

    private final Client client = ClientBuilder.newClient();

    @Inject
    private LoginView loginView;

    public String enviarSolicitud(Adopcion adopcion) {
        try {
            SolicitudesAdopcion s = new SolicitudesAdopcion();
            s.setClienteEmail(loginView.getAuthenticatedUser().getEmail());
            s.setMascotaId(adopcion.getMascotaSeleccionada().getId());
            s.setFechaSolicitud(new Date());
            s.setEstado("pendiente");

            client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON));

            return "/adopcion/exito.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SolicitudesAdopcion> getSolicitudesDelCliente() {
        String email = loginView.getAuthenticatedUser().getEmail();

        List<SolicitudesAdopcion> resultado = client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/cliente/" + email)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SolicitudesAdopcion>>() {
                });

        return resultado;
    }

    public Mascotas buscarMascotaPorId(Integer id) {
        return client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.mascotas/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(Mascotas.class);
    }

    public void cancelarSolicitud(int idSolicitud) {
        client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/" + idSolicitud)
                .request()
                .delete();
    }

    public List<SolicitudesAdopcion> getSolicitudesPorRefugio(String email) {
        return client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/refugio/" + email)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SolicitudesAdopcion>>() {
                });
    }

    public void actualizarEstadoSolicitud(int idSolicitud, String nuevoEstado) {
        // Paso 1: obtener la solicitud completa
        SolicitudesAdopcion s = client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/" + idSolicitud)
                .request(MediaType.APPLICATION_JSON)
                .get(SolicitudesAdopcion.class);

        // Paso 2: actualizar el campo estado
        s.setEstado(nuevoEstado);

        // Paso 3: enviar todo el objeto de vuelta con PUT
        client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/" + idSolicitud)
                .request()
                .put(Entity.entity(s, MediaType.APPLICATION_JSON));
    }

    public boolean estaEnListaNegra(String email) {
        try {
            Response response = client
                    .target("http://serpis.infor.uva.es:80/darklist/api/validar_adoptante/" + email)
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 404) {
                return false; // No está en la base de datos = permitido
            }

            if (response.getStatus() == 200) {
                String resultado = response.readEntity(String.class);

                try (JsonReader jsonReader = Json.createReader(new StringReader(resultado))) {
                    JsonObject jsonObject = jsonReader.readObject();
                    String estado = jsonObject.getString("listaNegra", "no");
                    return "si".equalsIgnoreCase(estado);
                }
            }

            return true; // Por seguridad, se deniega
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}
