package ru.kata.spring.boot_security.demo.models;

import java.io.Serializable;

public enum Gender implements Serializable {
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