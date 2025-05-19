/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped; 
import javax.inject.Inject;
import javax.inject.Named;


@Named
@RequestScoped
public class SolicitudesView {

    @Inject
    private AdopcionService adopcionService;

    private List<SolicitudesAdopcion> listaSolicitudes;

    private Map<Integer, Mascotas> mascotasPorId;

    @PostConstruct
    public void init() {
        listaSolicitudes = adopcionService.getSolicitudesDelCliente();
        mascotasPorId = new HashMap<>();

        for (SolicitudesAdopcion s : listaSolicitudes) {
            Integer id = s.getMascotaId();
            Mascotas m = adopcionService.buscarMascotaPorId(id);
            mascotasPorId.put(id, m);
        }

    }

    public List<SolicitudesAdopcion> getListaSolicitudes() {
        return listaSolicitudes;
    }

    public Mascotas getMascotaPorId(Integer id) {
        return mascotasPorId.get(id);
    }

    public void cancelar(int idSolicitud) {
        adopcionService.cancelarSolicitud(idSolicitud);
        listaSolicitudes = adopcionService.getSolicitudesDelCliente(); // refrescar
    }


}
