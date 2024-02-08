package ru.kata.spring.boot_security.demo.models;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    NOT_DEFINED("Not Defined");

    private final String text;

    Gender(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}