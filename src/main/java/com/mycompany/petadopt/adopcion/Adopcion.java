package com.mycompany.petadopt.adopcion;

import com.mycompany.petadopt.entities.Mascotas;
import com.mycompany.petadopt.entities.SolicitudesAdopcion;
import com.mycompany.petadopt.servicios.MascotasService;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;

@Named
@FlowScoped("adopcion")
public class Adopcion implements Serializable {

    private String especie;
    private List<Mascotas> resultados;
    private Mascotas mascotaSeleccionada;
    private SolicitudesAdopcion solicitud;

    @Inject
    private MascotasService mascotasService;

    public String buscar() {
        this.resultados = mascotasService.findByEspecie(especie);

        // Obtener todas las solicitudes
        List<SolicitudesAdopcion> solicitudes = mascotasService.getTodasSolicitudes(); // <-- necesitas este método

        // Filtrar las que ya están adoptadas
        filtrarResultadosDisponibles(solicitudes);

        return "resultados";
    }

    // Getters y setters
    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public List<Mascotas> getResultados() {
        return resultados;
    }

    public void setResultados(List<Mascotas> resultados) {
        this.resultados = resultados;
    }

    public Mascotas getMascotaSeleccionada() {
        return mascotaSeleccionada;
    }

    public void setMascotaSeleccionada(Mascotas mascotaSeleccionada) {
        this.mascotaSeleccionada = mascotaSeleccionada;
    }

    public SolicitudesAdopcion getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudesAdopcion solicitud) {
        this.solicitud = solicitud;
    }

    public void filtrarResultadosDisponibles(List<SolicitudesAdopcion> solicitudes) {
        List<Mascotas> filtradas = new java.util.ArrayList<Mascotas>();
        java.util.Set<Integer> adoptadas = new java.util.HashSet<Integer>();

        for (SolicitudesAdopcion s : solicitudes) {
            if ("aceptada".equalsIgnoreCase(s.getEstado())) {
                adoptadas.add(s.getMascotaId());
            }
        }

        for (Mascotas m : resultados) {
            if (!adoptadas.contains(m.getId())) {
                filtradas.add(m);
            }
        }

        this.resultados = filtradas;
    }

}
