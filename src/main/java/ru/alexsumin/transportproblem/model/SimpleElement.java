package ru.alexsumin.transportproblem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class SimpleElement {


    private SimpleIntegerProperty value;

    public SimpleElement(int value) {
        this.value = new SimpleIntegerProperty(value);
    }

    public SimpleElement() {

    }

    public int getValue() {
        if (value == null) {
            return 0;
        }
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
