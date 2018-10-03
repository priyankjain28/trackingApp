package com.hora.priyank.trackingapp.data.model;

import com.hora.priyank.trackingapp.data.dao.UserInterface;

/**
 * Created by Priyank Jain on 27-09-2018.
 */
public class User implements UserInterface{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userRole;
    private String dateCreated;
    private String lastUpdate;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String userRole, String dateCreated,String lastUpdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.dateCreated = dateCreated;
        this.lastUpdate = lastUpdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userRole='" + userRole + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }
}
