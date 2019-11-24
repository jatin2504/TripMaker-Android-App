package com.example.tripmaker.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private String createdByEmail;
    private String createdByName;
    private String title;
    private String coverPhotoUrl;
    private String locationName;
    private Location location;
    private Timestamp date;
    private List<String> members = new ArrayList<>();

    public Trip(String createdByEmail, String createdByName, String title, String coverPhotoUrl, Location location, List<String> members) {
        this.createdByEmail = createdByEmail;
        this.createdByName = createdByName;
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.location = location;
        this.members = members;
    }

    public Trip(String createdByName, String title, Timestamp date) {
        this.createdByName = createdByName;
        this.title = title;
        this.date = date;

    }

    public Trip() {
    }

    public void addMembers(String member) {
        this.members.add(member);
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
