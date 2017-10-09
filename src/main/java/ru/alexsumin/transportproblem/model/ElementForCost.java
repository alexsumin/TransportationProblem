package ru.alexsumin.transportproblem.model;

import java.util.ArrayList;
import java.util.List;

public class ElementForCost {

    private List<Element> data = new ArrayList<>();

    public int getSize() {
        return data.size();
    }

    public void setSize(int size) {
        while (data.size() != size) {
            if (data.size() < size) {
                Element element = new Element();
                element.setValue(0);
                data.add(element);
            } else data.remove(data.size() - 1);
        }
    }

    public int getByIndex(int index) {
        return data.get(index).getValue();
    }


    public void setByIndex(int index, int value) {
        data.get(index).setValue(value);
    }
}
