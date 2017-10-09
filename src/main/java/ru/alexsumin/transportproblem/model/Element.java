package ru.alexsumin.transportproblem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Element {


    private SimpleIntegerProperty value;

    public Element(int value) {
        this.value = new SimpleIntegerProperty(value);
    }

    public Element() {

    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        this.value = new SimpleIntegerProperty(value);

    }

    public SimpleStringProperty valueProperty() {
        if (value == null) return new SimpleStringProperty("Введите значение");
        return new SimpleStringProperty(String.valueOf(value.intValue()));
    }
}
