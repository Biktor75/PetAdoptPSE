package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.jaas.UserEJB;
import com.mycompany.petadopt.entities.Users;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class RegisterView {

    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String role = "cliente"; // cliente o refugio

    @Inject
    private UserEJB userEJB;

    public void validatePassword(ComponentSystemEvent event) {
    FacesContext context = FacesContext.getCurrentInstance();

    if (password == null || confirmPassword == null) {
        return;
    }

    if (!password.equals(confirmPassword)) {
        FacesMessage msg = new FacesMessage("Las contraseñas no coinciden");
        context.addMessage(null, msg);
        context.renderResponse();
        return;
    }

    if (email != null && userEJB.findByEmail(email) != null) {
        FacesMessage msg = new FacesMessage("Ya existe un usuario con ese email");
        context.addMessage(null, msg);
        context.renderResponse();
    }
}


    private String getValueFromComponent(UIComponent parent, String id) {
        UIInput input = (UIInput) parent.findComponent(id);
        return input.getLocalValue() != null ? input.getLocalValue().toString() : "";
    }

    public String register() {
        Users user = new Users(email, password, name);
        userEJB.createUser(user, role);
        return "login?faces-redirect=true";
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
