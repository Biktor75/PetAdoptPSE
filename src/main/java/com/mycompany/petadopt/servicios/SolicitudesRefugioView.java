/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import com.mycompany.petadopt.jaas.LoginView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author victo
 */
@Named
@RequestScoped
public class SolicitudesRefugioView {

    @Inject
    private AdopcionService adopcionService;

    @Inject
    private LoginView loginView;

    private List<SolicitudesAdopcion> solicitudes;
    private Map<Integer, Mascotas> mascotasPorId;

    @PostConstruct
    public void init() {
        System.out.println("✅ Bean SolicitudesRefugioView inicializado");
        String emailRefugio = loginView.getAuthenticatedUser().getEmail();
        solicitudes = adopcionService.getSolicitudesPorRefugio(emailRefugio);
        mascotasPorId = new HashMap<>();

        for (SolicitudesAdopcion s : solicitudes) {
            Mascotas m = adopcionService.buscarMascotaPorId(s.getMascotaId());
            mascotasPorId.put(s.getMascotaId(), m);
        }
    }

    public Mascotas mascotaPorId(Integer id) {
        return mascotasPorId.get(id);
    }

    public List<SolicitudesAdopcion> getSolicitudes() {
        return solicitudes;
    }

    public void actualizarEstado(int idSolicitud, String nuevoEstado) {
        adopcionService.actualizarEstadoSolicitud(idSolicitud, nuevoEstado);
        // recargar
        init();
    }

    public boolean puedeAceptar(SolicitudesAdopcion solicitud) {
        return !adopcionService.estaEnListaNegra(solicitud.getClienteEmail());
    }

}
