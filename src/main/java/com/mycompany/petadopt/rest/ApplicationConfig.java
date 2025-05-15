package com.mycompany.petadopt.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("webresources")  // Ruta base para todos tus endpoints REST
public class ApplicationConfig extends Application {
    // Puedes registrar clases manualmente si lo necesitas
}
