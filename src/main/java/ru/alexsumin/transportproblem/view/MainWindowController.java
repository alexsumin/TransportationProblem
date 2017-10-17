package ru.alexsumin.transportproblem.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ru.alexsumin.transportproblem.math.Solver;
import ru.alexsumin.transportproblem.model.Element;
import ru.alexsumin.transportproblem.model.SimpleElement;

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

    public static final int INIT_VALUE = 3;
    @FXML
    private TableColumn suppliersColumn;
    @FXML
    TableView<Element> costTable = new TableView<>();
    @FXML
    private TableColumn consumersColumn;
    @FXML
    TableView<Element> tableNW = new TableView<>();
    @FXML
    TableView<Element> tableOptim = new TableView<>();
    @FXML
    Label costNWLabel = new Label();
    @FXML
    Label optCostLabel = new Label();
    @FXML
    private TableView<SimpleElement> suppliersTable = new TableView<>();
    @FXML
    private TableView<SimpleElement> consumersTable = new TableView<>();
    private List<SimpleElement> suppliers;
    private ObservableList<SimpleElement> suppliersObserv;

    private List<SimpleElement> consumers;
    private ObservableList<SimpleElement> consumersObserv;

    private ObservableList<Element> costs;

    private int columnIndex;
    private int columnSupplyIndex;

    private Solver solver;
    private int[][] solution;

    private static List convertList(List<SimpleElement> toConvert) {
        List<Integer> result = new ArrayList<>();
        for (SimpleElement e : toConvert) {
            result.add(e.getValue());
        }
        return result;
    }

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

        setDefault();

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
                (EventHandler<TableColumn.CellEditEvent<SimpleElement, String>>) t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setValue(viewHelp(t.getNewValue()))
        );
    }

    private int viewHelp(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void editRowsCostTable(int size) {

        while (costs.size() != size) {
            if (costs.size() < size) {
                Element el = new Element();
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
            for (Element e :
                    costs)
                e.setSize(size);
        }
        costTable.refresh();
    }

    private void configureSlider(Slider slider, Label label, List list) {
        slider.setMin(1);
        slider.setMax(9);
        slider.setValue(INIT_VALUE);
        slider.setBlockIncrement(1);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int temp = newValue.intValue();
            label.setText(String.valueOf(temp));
            while (temp != list.size()) {
                if (temp > list.size()) {
                    list.add(new SimpleElement());
                } else {
                    list.remove(list.size() - 1);
                }
            }
        });
    }

    @FXML
    private void calculate() {
        solver = new Solver();
        solver.setStorageStock(convertList(suppliersObserv));
        solver.setShopNeeds(convertList(consumersObserv));
        solver.setCostTable(convertToInteger(costs));
        solution = solver.calcNW();
        setDataToTable(solution, tableNW);
        costNWLabel.setText(String.valueOf(solver.getCost()));

        List result;
        result = solver.solveTask(64);
        optCostLabel.setText(String.valueOf(result.get(0)));
        setDataToTable((int[][]) result.get(1), tableOptim);

    }

    @FXML
    private void setRandomCost() {
        for (int i = 0; i < costs.size(); i++) {
            for (int j = 0; j < consumersObserv.size(); j++) {
                int newValue = (int) (Math.random() * 10);
                costs.get(i).setByIndex(j, newValue);
            }
        }
        costTable.refresh();
    }

    @FXML
    private void setDefault() {
        suppliers = new ArrayList<>(INIT_VALUE);
        consumers = new ArrayList<>(INIT_VALUE);

        SimpleElement e1 = new SimpleElement();
        SimpleElement e2 = new SimpleElement();
        SimpleElement e3 = new SimpleElement();
        e1.setValue(40);
        e2.setValue(30);
        e3.setValue(20);

        SimpleElement e4 = new SimpleElement();
        SimpleElement e5 = new SimpleElement();
        SimpleElement e6 = new SimpleElement();
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


        List<Element> listCosts = new ArrayList();


        for (int i = 0; i < suppliersObserv.size(); i++) {
            Element temp = new Element();
            temp.setSize(consumersObserv.size());
            for (int j = 0; j < temp.getSize(); j++) {
                temp.setByIndex(j, 0);
            }
            listCosts.add(temp);
        }

        updateColumnsCostTable(INIT_VALUE);

        costs = FXCollections.observableArrayList(listCosts);

        costTable.setItems(costs);
        editRowsCostTable(INIT_VALUE);

        supplySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editRowsCostTable(newValue.intValue());
        });

        consumerSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            editColumnsCostTable(newValue.intValue());
            updateColumnsCostTable(newValue.intValue());
        });
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    private List convertToInteger(List<Element> list) {
        List forReturn = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            List<Integer> innerList = new ArrayList();
            for (int j = 0; j < consumersObserv.size(); j++) {
                innerList.add(list.get(i).getByIndex(j));
            }
            forReturn.add(innerList);
        }
        return forReturn;
    }


    private void setDataToTable(int[][] array, TableView table) {
        List<Element> list = new ArrayList<>();

        for (int i = 0; i < suppliersObserv.size(); i++) {
            Element el = new Element();
            el.setSize(consumersObserv.size());
            for (int j = 0; j < consumersObserv.size(); j++) {
                el.setByIndex(j, array[i][j]);
            }
            list.add(el);
        }
        ObservableList listForNW = FXCollections.observableArrayList(list);

        table.setItems(listForNW);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        createColumns(consumersObserv.size(), table);


    }

    private void createColumns(int size, TableView table) {

        table.getColumns().clear();
        List<TableColumn> columnsList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            TableColumn<Element, String> column = new TableColumn<>("Need " + (i + 1));
            final int j = i;
            column.setCellValueFactory(cellData ->
                    new ReadOnlyStringWrapper(cellData.getValue().getByIndex(j) + ""));
            column.setCellFactory(TextFieldTableCell.forTableColumn());

            columnsList.add(column);
        }
        for (TableColumn c :
                columnsList) {
            table.getColumns().add(c);
        }
    }


    private void updateColumnsCostTable(int size) {

        costTable.getColumns().clear();
        List<TableColumn> columnsList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            TableColumn<Element, String> column = new TableColumn<>("Need " + (i + 1));
            final int j = i;
            column.setCellValueFactory(cellData ->
                    new ReadOnlyStringWrapper(cellData.getValue().getByIndex(j) + ""));
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(
                    t -> t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setByIndex(columnIndex, Integer.parseInt(t.getNewValue())));
            columnsList.add(column);
        }
        for (TableColumn c :
                columnsList) {
            costTable.getColumns().add(c);
        }
        costTable.refresh();
    }

    @FXML
    private void helpWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Как работать с программой");
        alert.setHeaderText("Программа предназначена для решения транспортной задачи.\n" +
                "\n\n" +
                "1. Задайте количество поставщиков и потребителей.\n" +
                "2. Установите значения для потребителей и поставщиков.\n" +
                "3. Установите стоиомсть перевозки(двойной клик по ячейке таблицы).\n" +
                "4. Нажмите \"Рассчитать\" - программа выполнит рассчёт и оптимизацию решения.\n" +
                "5. Переключитесь на вкладку \"Результаты\", чтобы увидеть результат решения.");
        alert.showAndWait();
    }

    @FXML
    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("\tПрограмма позволяет получить решение транспортной задачи методом\n" +
                "\tсеверо-западного угла, а также выполнить оптимизацию решения.\n" +
                "\tАвторы: студенты группы 444 Кривобокова А., Шарипова М., Сумин А.\n" +
                "\t\t\t\t\t\t СПбГТИ(ТУ) 2017");
        alert.showAndWait();
    }
}



