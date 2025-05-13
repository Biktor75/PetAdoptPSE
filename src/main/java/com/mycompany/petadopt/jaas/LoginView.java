package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.jaas.UserEJB;
import com.mycompany.petadopt.entities.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named
@RequestScoped
public class LoginView {

    private String email;
    private String password;
    private Users user;

    @Inject
    private UserEJB userEJB;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();


        try {
            request.login(email,password);
            System.out.println("LOGIN OK - Usuario: " + email);

        String[] roles = { "cliente", "Cliente", "users", "admin", "refugio" };
        for (String r : roles) {
            System.out.println("¿" + r + "? " + request.isUserInRole(r));
        }

        System.out.println("Principal: " + request.getUserPrincipal());
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login incorrecto!", null));
            return "login";
        }

        this.user = userEJB.findByEmail(request.getUserPrincipal().getName());
        
        if (request.isUserInRole("admin")) {
            return "/admin/privatepage.xhtml?faces-redirect=true";
        } else if (request.isUserInRole("refugio")) {
            return "/refugio/inicio.xhtml?faces-redirect=true";
        } else if (request.isUserInRole("cliente")) {
            return "/clientes/privatepage.xhtml?faces-redirect=true";
        } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Rol no permitido.", null));

            return "login";
        }

    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            this.user = null;
            request.logout();
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return "/index?faces-redirect=true";
    }

    public Users getAuthenticatedUser() {
        return user;
    }

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
