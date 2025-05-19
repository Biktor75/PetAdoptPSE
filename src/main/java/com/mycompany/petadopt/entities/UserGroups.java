/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.petadopt.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "user_groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserGroups.findAll", query = "SELECT u FROM UserGroups u"),
    @NamedQuery(name = "UserGroups.findByEmail", query = "SELECT u FROM UserGroups u WHERE u.userGroupsPK.email = :email"),
    @NamedQuery(name = "UserGroups.findByGroupname", query = "SELECT u FROM UserGroups u WHERE u.userGroupsPK.groupname = :groupname")})
public class UserGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserGroupsPK userGroupsPK;

    public UserGroups() {
    }

    public UserGroups(UserGroupsPK userGroupsPK) {
        this.userGroupsPK = userGroupsPK;
    }

    public UserGroups(String email, String groupname) {
        this.userGroupsPK = new UserGroupsPK(email, groupname);
    }

    public UserGroupsPK getUserGroupsPK() {
        return userGroupsPK;
    }

    public void setUserGroupsPK(UserGroupsPK userGroupsPK) {
        this.userGroupsPK = userGroupsPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userGroupsPK != null ? userGroupsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserGroups)) {
            return false;
        }
        UserGroups other = (UserGroups) object;
        if ((this.userGroupsPK == null && other.userGroupsPK != null) || (this.userGroupsPK != null && !this.userGroupsPK.equals(other.userGroupsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.petadopt.entities.UserGroups[ userGroupsPK=" + userGroupsPK + " ]";
    }

    public void setEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setGroupname(String cliente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
