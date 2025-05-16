package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.adopcion.Adopcion;
import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import com.mycompany.petadopt.jaas.LoginView;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.GenericType;

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
            System.err.println("❌ Error al enviar solicitud de adopción:");
            e.printStackTrace();
            return null;
        }
    }

    public List<SolicitudesAdopcion> getSolicitudesDelCliente() {
        String email = loginView.getAuthenticatedUser().getEmail();
        System.out.println("📩 Buscando solicitudes para: " + email);

        List<SolicitudesAdopcion> resultado = client
                .target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.solicitudesadopcion/cliente/" + email)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SolicitudesAdopcion>>() {
                });

        System.out.println("📋 Solicitudes encontradas: " + resultado.size());
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

}
