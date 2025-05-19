package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Clientes;
import com.mycompany.petadopt.entities.Refugios;
import com.mycompany.petadopt.entities.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Named
@RequestScoped
public class RegisterView implements Serializable {

    private String tipoUsuario;

    // Comunes
    private String email;
    private String password;
    private String confirmPassword;
    private String domicilio;
    private String telefono;

    // Cliente
    private String nombre;
    private String apellidos;
    private String nif;
    private Date fechaNacimiento;

    // Refugio
    private String cif;

    @Inject
    private UserEJB userEJB;

    public String registrar() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (!password.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las contraseñas no coinciden", null));
            return null;
        }

        if ("cliente".equals(tipoUsuario)) {
            if (!esMayorEdad(fechaNacimiento)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes ser mayor de edad para registrarte", null));
                return null;
            }
        }

        // Crear usuario base
        Users user = new Users();
        user.setEmail(email);
        user.setNombre(nombre); // Para ambos tipos, aunque Refugio puede dejarlo nulo
        user.setPassword(password);

        Users creado = userEJB.createUser(user, tipoUsuario);
        if (creado == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al crear usuario", null));
            return null;
        }

        if ("cliente".equals(tipoUsuario)) {
            Clientes cliente = new Clientes();
            cliente.setEmail(email);
            cliente.setNif(nif);
            cliente.setDomicilio(domicilio);
            cliente.setTelefono(telefono);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setApellidos(apellidos);
            userEJB.persistCliente(cliente);

        } else if ("refugio".equals(tipoUsuario)) {
            Refugios refugio = new Refugios();
            refugio.setEmail(email);
            refugio.setCif(cif);
            refugio.setDomicilio(domicilio);
            refugio.setTelefono(telefono);
            refugio.setAutorizado(false);
            userEJB.persistRefugio(refugio);
        }

        return "login?faces-redirect=true";
    }

    private boolean esMayorEdad(Date fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }
        LocalDate fechaNac = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return fechaNac.isBefore(LocalDate.now().minusYears(18));
    }

    public Date getFechaMaxima() {
        return Date.from(LocalDate.now().minusYears(18).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Getters y setters
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }
}
