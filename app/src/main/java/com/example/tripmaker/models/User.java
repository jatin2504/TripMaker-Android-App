package com.example.tripmaker.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Gender gender;
    private String imageUrl;
    private List<JoinedTrip> trips = new ArrayList<>();


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addTrip(JoinedTrip trip) {
        trips.add(trip);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String firstName, String lastName, String email, String password, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<JoinedTrip> getTrips() {
        return trips;
    }

    public void setTrips(List<JoinedTrip> trips) {
        this.trips = trips;
    }
}
