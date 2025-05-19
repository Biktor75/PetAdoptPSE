package com.mycompany.petadopt.servicios;

import com.mycompany.petadopt.entities.Refugios;
import com.mycompany.petadopt.jaas.UserEJB;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@Named
@RequestScoped
public class GestRefugios {

    @Inject
    private UserEJB userEJB;

    private List<Refugios> refugiosPendientes;

    @PostConstruct
    public void init() {
        try {
            refugiosPendientes = userEJB.findRefugiosNoAutorizados();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Refugios> getRefugiosPendientes() {
        return refugiosPendientes;
    }

    public void autorizar(String email) {
        userEJB.autorizarRefugio(email); 
        refugiosPendientes = userEJB.findRefugiosNoAutorizados(); 
    }


}
