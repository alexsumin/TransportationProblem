package ru.alexsumin.transportproblem.model;

import java.util.ArrayList;
import java.util.List;

public class Element {

    private List<SimpleElement> data = new ArrayList<>();

    public int getSize() {
        return data.size();
    }

    public void setSize(int size) {
        while (data.size() != size) {
            if (data.size() < size) {
                SimpleElement simpleElement = new SimpleElement();
                simpleElement.setValue(0);
                data.add(simpleElement);
            } else data.remove(data.size() - 1);
        }
    }

    public int getByIndex(int index) {
        if (data.get(index).getValue() == -1) {
            return 0;
        }
        return data.get(index).getValue();
    }


    public void setByIndex(int index, int value) {
        data.get(index).setValue(value);
    }
}
