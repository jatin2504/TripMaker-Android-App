package com.example.tripmaker.models;

import java.util.List;

public class Trip {
    private String createdByEmail;
    private String createdByName;
    private String title;
    private String coverPhotoUrl;
    private Location location;
    private String date;
    private List<String> members;

    public Trip(String createdByEmail, String createdByName, String title, String coverPhotoUrl, Location location, List<String> members) {
        this.createdByEmail = createdByEmail;
        this.createdByName = createdByName;
        this.title = title;
        this.coverPhotoUrl = coverPhotoUrl;
        this.location = location;
        this.members = members;
    }

    public Trip(String createdByName, String title, String date) {
        this.createdByName = createdByName;
        this.title = title;
        this.date = date;
    }

    public Trip() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
