package com.example.tripmaker.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trip implements Serializable {
    private String id;
    private String createdByEmail;
    private String createdByName;
    private String title;
    private String coverPhotoUrl;
    private String locationName;
    private Location location;
    transient private Timestamp date;
    private List<Member> members = new ArrayList<>();

    public Trip(String createdByEmail, String createdByName, String title, String coverPhotoUrl, Location location, List<Member> members) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addMembers(Member member) {
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
