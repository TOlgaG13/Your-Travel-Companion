package com.yourtravelcompanion.your_travel_companion.models;

public enum UserRegisterType {
    FORM, GOOGLE;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}