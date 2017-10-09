package ru.alexsumin.transportproblem.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.alexsumin.transportproblem.model.Element;
import ru.alexsumin.transportproblem.model.ElementForCost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindowController {

    @FXML
    private Slider supplySlider = new Slider();
    @FXML
    private Label supplyLabel = new Label();
    @FXML
    private Slider consumerSlider = new Slider();
    @FXML
    private Label consumerLabel = new Label();

    @FXML
    private TableView<Element> suppliersTable = new TableView<>();
    @FXML
    private TableColumn suppliersColumn;
    @FXML
    private TableView<Element> consumersTable = new TableView<>();
    @FXML
    private TableColumn consumersColumn;
    @FXML
    TableView<ElementForCost> costTable = new TableView<>();

    private List<Element> suppliers;
    private ObservableList suppliersObserv;

    private List<Element> consumers;
    private ObservableList consumersObserv;

    private ObservableList<ElementForCost> costs;
    private int columnIndex;

    private int columnSupplyIndex;
    private ElementForCost selectedItem;

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


        List<ElementForCost> listCosts = new ArrayList();


        for (int i = 0; i < suppliersObserv.size(); i++) {
            ElementForCost temp = new ElementForCost();
            temp.setSize(consumersObserv.size());
            for (int j = 0; j < temp.getSize(); j++) {

                temp.setByIndex(j, 0);
            }
            listCosts.add(temp);
        }

        updateColumnsCostTable(3);

        costs = FXCollections.observableArrayList(listCosts);

        costTable.setItems(costs);
        editRowsCostTable(3);


        supplySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editRowsCostTable(newValue.intValue());
        });

        consumerSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editColumnsCostTable(newValue.intValue());
            updateColumnsCostTable(newValue.intValue());
        });


        costTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        costTable.setEditable(true);
        costTable.getSelectionModel().setCellSelectionEnabled(true);


        configureColumn(suppliersColumn);
        configureColumn(consumersColumn);
        suppliersTable.getSelectionModel().setCellSelectionEnabled(true);
        consumersTable.getSelectionModel().setCellSelectionEnabled(true);


        costTable.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
                columnIndex = newVal.getColumn();
            }
        });

        suppliersTable.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getTableColumn() != null) {
                columnSupplyIndex = newVal.getColumn();
            }
        });

    }

    private void configureColumn(TableColumn column) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Element, String>>) t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setValue(temporary(t.getNewValue()))
        );
    }

    private int temporary(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void editRowsCostTable(int size) {
        while (costs.size() != size) {
            if (costs.size() < size) {
                ElementForCost el = new ElementForCost();
                el.setSize(consumersObserv.size());
                for (int i = 0; i < consumersObserv.size(); i++) {
                    el.setByIndex(i, 0);
                }
                costs.add(el);
            } else {
                costs.remove(costs.size() - 1);
            }
        }

        costTable.refresh();

    }

    private void editColumnsCostTable(int size) {
        while (costs.get(0).getSize() != size) {
            for (ElementForCost e :
                    costs)
                e.setSize(size);
        }
        costTable.refresh();
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


    private void updateColumnsCostTable(int size) {

        costTable.getColumns().clear();
        List<TableColumn> columnsList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            TableColumn<ElementForCost, String> column = new TableColumn<>("Потребитель " + (i + 1));
            final int j = i;
            column.setCellValueFactory(cellData ->
                    new ReadOnlyStringWrapper(cellData.getValue().getByIndex(j) + ""));
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    t -> {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setByIndex(columnIndex, Integer.parseInt(t.getNewValue()));
                    });
            columnsList.add(column);
        }
        for (TableColumn c :
                columnsList) {
            costTable.getColumns().add(c);
        }
        costTable.refresh();
    }
}



