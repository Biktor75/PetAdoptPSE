package com.mycompany.petadopt.jaas;

import com.mycompany.petadopt.entities.Refugios;
import com.mycompany.petadopt.entities.Users;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Named
@SessionScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private Users user;

    @Inject
    private UserEJB userEJB;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(email, password);
            System.out.println("LOGIN OK - Usuario: " + email);

            this.user = userEJB.findByEmail(request.getUserPrincipal().getName());

            if (request.isUserInRole("admin")) {
                return "/admin/privatepage.xhtml?faces-redirect=true";
            } else if (request.isUserInRole("refugio")) {
                return "/refugios/privatepage.xhtml?faces-redirect=true";
            } else if (request.isUserInRole("cliente")) {
                return "/clientes/privatepage.xhtml?faces-redirect=true";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Rol no permitido.", null));
                return "login";
            }

        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login incorrecto!", null));
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

    public String eliminarCuenta() {
        try {
            String email = user.getEmail();

            Client client = ClientBuilder.newClient();
            client.target("http://localhost:8080/PetAdopt/webresources/com.mycompany.petadopt.entities.users/email/" + email)
                    .request()
                    .delete();

            logout(); // Cierra sesión
            return "/index.xhtml?faces-redirect=true";

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar cuenta:");
            e.printStackTrace();
            return null;
        }
    }

    public boolean isRefugioAutorizado() {
        if (user == null) {
            return false;
        }
        Refugios refugio = userEJB.findRefugioByEmail(user.getEmail());
        return refugio != null && Boolean.TRUE.equals(refugio.getAutorizado());
    }

    public boolean isRefugioAutorizadoYConRol() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        return request.isUserInRole("refugio") && isRefugioAutorizado();
    }

    public Users getAuthenticatedUser() {
        return user;
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
}
