package com.example.tripmaker.models;

import com.google.firebase.Timestamp;

public class JoinedTrip {
    private String tripId;
    private Timestamp joinedDate;

    public JoinedTrip() {
    }

    public JoinedTrip(String tripId, Timestamp joinedDate) {
        this.tripId = tripId;
        this.joinedDate = joinedDate;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Timestamp getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Timestamp joinedDate) {
        this.joinedDate = joinedDate;
    }
}

