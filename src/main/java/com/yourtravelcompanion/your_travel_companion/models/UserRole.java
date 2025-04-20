package com.yourtravelcompanion.your_travel_companion.models;

public enum UserRole {
    ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}