package ru.alexsumin.transportproblem.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.alexsumin.transportproblem.model.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindowController {

    @FXML
    Slider supplySlider = new Slider();
    @FXML
    Label supplyLabel = new Label();
    @FXML
    Slider consumerSlider = new Slider();
    @FXML
    Label consumerLabel = new Label();

    @FXML
    TableView<Element> suppliersTable = new TableView<>();
    @FXML
    TableColumn suppliersColumn;
    @FXML
    TableView<Element> consumersTable = new TableView<>();
    @FXML
    TableColumn consumersColumn;

    List<Element> suppliers;
    ObservableList suppliersObserv;

    List<Element> consumers;
    ObservableList consumersObserv;


    @FXML
    public void initialize() {


        suppliersTable.setEditable(true);
        consumersTable.setEditable(true);

        suppliersColumn = new TableColumn("Поставщики");
        suppliersColumn.setCellValueFactory(
                new PropertyValueFactory<>("value"));
        suppliersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        consumersColumn = new TableColumn("Потребители");
        consumersColumn.setCellValueFactory(
                new PropertyValueFactory<>("value"));
        consumersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        suppliers = new ArrayList<>(3);
        consumers = new ArrayList<>(3);


        Element e1 = new Element();
        Element e2 = new Element();
        Element e3 = new Element();
        e1.setValue(40);
        e2.setValue(30);
        e3.setValue(20);


        Element e4 = new Element();
        Element e5 = new Element();
        Element e6 = new Element();
        e4.setValue(20);
        e5.setValue(40);
        e6.setValue(30);

        suppliers.addAll(Arrays.asList(e1, e2, e3));
        consumers.addAll(Arrays.asList(e4, e5, e6));

        suppliersObserv = FXCollections.observableArrayList(suppliers);
        consumersObserv = FXCollections.observableArrayList(consumers);
        configureSlider(supplySlider, supplyLabel, suppliersObserv);
        configureSlider(consumerSlider, consumerLabel, consumersObserv);

        suppliersTable.setItems(suppliersObserv);
        suppliersTable.getColumns().add(suppliersColumn);

        consumersTable.setItems(consumersObserv);
        consumersTable.getColumns().add(consumersColumn);


    }

    private void configureSlider(Slider slider, Label label, List list) {
        slider.setMin(1);
        slider.setMax(9);
        slider.setValue(3);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int temp = newValue.intValue();
            label.setText(String.valueOf(temp));
            while (temp != list.size()) {
                if (temp > list.size()) {
                    list.add(new Element());
                } else {
                    list.remove(list.size() - 1);
                }
            }
        });
    }
}

