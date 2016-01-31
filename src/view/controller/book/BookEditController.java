package view.controller.book;

import collection.BookCollection;
import collection.CatalogCollection;
import collection.CatalogOptionCollection;
import collection.Collection;
import item.Book;
import item.Catalog;
import item.CatalogOption;
import item.Item;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.CatalogModel;
import model.CatalogOptionModel;
import view.controller.CoreController;
import view.helper.Author;
import view.helper.Helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class BookEditController extends CoreController implements Initializable {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private GridPane gpContent;

    @FXML
    private ProgressIndicator piProgress;

    @FXML
    private TextField tfBookName;

    @FXML
    private TextField tfYear;

    @FXML
    private TextField tfPublication;

    @FXML
    private TextArea taNotes;

    @FXML
    private GridPane gpAuthors;

    @FXML
    private VBox catalogContainer;

    private Book book;
    private Collection collection;
    private int libraryId = -1;
    private Set<Integer> internalRemovedRows = new HashSet<>();
    private int countAddedRowsAfterRemoved = 0;
    private Set<Integer> catalogOptionIds = new HashSet<>();
    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    @FXML
    private void onCancelClicked() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    @FXML
    private void onSaveClicked(ActionEvent actionEvent) {
        piProgress.setVisible(true);
        gpContent.setDisable(true);

        if (!validate()) {
            piProgress.setVisible(false);
            gpContent.setDisable(false);
            return;
        }

        final ServiceSave serviceSave = new ServiceSave();
        serviceSave.setOnSucceeded(event -> {
                if (serviceSave.getValue()) {
                    ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
                } else {
                    gpContent.setDisable(false);
                    piProgress.setVisible(false);
                }
        });
        serviceSave.setOnFailed((workerStateEvent) -> {
            // TODO serviceSave.setOnFailed()
        });
        serviceSave.restart();
    }

    private boolean validate() {
        boolean result = true;

        if (tfBookName.getText().isEmpty()) {
            tfBookName.pseudoClassStateChanged(errorClass, true);
            result = false;
        }

        return result;
    }

    private void prepareAuthorsToShow() {
        if (book.getId() < 1) {
            addRow("", "", true);
            return;
        }

        List<Author> authors = Helper.getAuthorListFromValue(book.getOriginalValueAuthor());
        int i = 0;
        int countAuthors = authors.size();
        boolean showAdd = false;

        for (Author author : authors) {
            if (++i == countAuthors) {
                showAdd = true;
            }

            addRow(author.getFirstName(), author.getLastName(), showAdd);
        }

        if (countAuthors == 0) {
            addRow("", "", true);
        }
    }

    private void addRow(String firstName, String lastName, boolean showAdd) {
        TextField textField;
        int countRows = getAuthorRowCount();
        gpAuthors.addRow(countRows);
        gpAuthors.getRowConstraints().set(0, new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_PREF_SIZE));

        if (!internalRemovedRows.isEmpty()) {
            countAddedRowsAfterRemoved++;
        }

        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    textField = new TextField(firstName);
                    textField.setId("firstName" + countRows);
                    gpAuthors.add(textField, i, countRows);
                    break;
                case 1:
                    textField = new TextField(lastName);
                    textField.setId("lastName" + countRows);
                    gpAuthors.add(textField, i, countRows);
                    break;
                case 2:
                    // Container of buttons
                    HBox buttonsContainer = new HBox(10);
                    buttonsContainer.setId("buttonsContainer" + countRows);

                    // Button 'Add new author'
                    final Button buttonAdd = new Button("+");
                    buttonAdd.setId("btnAdd" + countRows);
                    buttonAdd.setOnAction(event -> {
                        addRow("", "", true);
                        buttonAdd.setVisible(false);
                    });
                    buttonAdd.setVisible(showAdd);
                    buttonsContainer.getChildren().add(buttonAdd);

                    // Button 'Remove author'
                    if (countRows > 1) {
                        final Button buttonRemove = new Button("-");
                        buttonRemove.setOnAction(event -> removeAuthor(countRows));
                        buttonsContainer.getChildren().add(buttonRemove);
                    }

                    gpAuthors.add(buttonsContainer, i, countRows);
                    break;
            }
        }
    }

    private void removeAuthor(int rowIndex) {
        Node node;
        List<Node> nodesDelete = new ArrayList<>();
        int countRows = getAuthorRowCount() - 1;
        int countInternalRemovedRows = getCountRowsRemovedBefore(rowIndex);
        String buttonAdd = "btnAdd" + (rowIndex - countInternalRemovedRows + countAddedRowsAfterRemoved - 1);
        String buttonsContainer = "buttonsContainer" + (rowIndex - countInternalRemovedRows + countAddedRowsAfterRemoved - 1);

        if (rowIndex != countRows) {
            internalRemovedRows.add(rowIndex);
        }

        for (int i = 0; i < gpAuthors.getChildren().size(); i++) {
            node = gpAuthors.getChildren().get(i);
            Integer nodeRowIndex = GridPane.getRowIndex(node);

            if (nodeRowIndex != null && rowIndex == nodeRowIndex) {
                nodesDelete.add(node);
            }

            if (rowIndex == countRows && buttonsContainer.equals(node.getId())) {
                Object[] children = ((HBox) node).getChildren().toArray();

                for (Object child : children) {
                    node = (Node) child;

                    if (buttonAdd.equals(node.getId())) {
                        node.setVisible(true);
                    }
                }
            }
        }

        gpAuthors.getChildren().removeAll(nodesDelete);
    }

    private int getAuthorRowCount() {
        int countRows = gpAuthors.getRowConstraints().size();

        for (int i = 0; i < gpAuthors.getChildren().size(); i++) {
            Node child = gpAuthors.getChildren().get(i);

            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);

                if (rowIndex != null) {
                    countRows = Math.max(countRows, rowIndex + 1);
                }
            }
        }

        return countRows;
    }

    private int getCountRowsRemovedBefore(int currentRowIndex) {
        int result = 0;

        for (int i = currentRowIndex - 1; i > 0; i--) {
            if (internalRemovedRows.contains(i)) {
                result++;
            } else {
                break;
            }
        }

        return result;
    }

    private String getAuthors() {
        List<Author> authors = new ArrayList<>();
        Author author;

        for (int i = 0; i < getAuthorRowCount(); i++) {
            String firstNameId = "firstName" + i;
            String lastNameId = "lastName" + i;
            String firstName = "";
            String lastName = "";

            for (Node node : gpAuthors.getChildren()) {
                if (firstNameId.equals(node.getId())) {
                    firstName = ((TextField) node).getText().trim();
                }

                if (lastNameId.equals(node.getId())) {
                    lastName = ((TextField) node).getText().trim();
                }
            }

            if (!firstName.isEmpty() || !lastName.isEmpty()) {
                author = new Author();
                author.setFirstName(firstName);
                author.setLastName(lastName);
                authors.add(author);
            }
        }

        return Helper.getAuthorValueFromList(authors);
    }

    private void prepareCatalogsToShow() {
        GridPane gridCatalogs = new GridPane();
        gridCatalogs.setHgap(15);
        gridCatalogs.setVgap(15);
        List<Item> catalogs = ((CatalogCollection) new CatalogModel().getCollection()).loadForView();
        Catalog catalog;
        int i = 0;

        for (Item item : catalogs) {
            catalog = (Catalog) item;
            gridCatalogs.addRow(i);
            gridCatalogs.add(new Label(catalog.getName() + ":"), 0, i);

            gridCatalogs.add(getCatalogOptions(catalog), 1, i);
            i++;
        }

        catalogContainer.getChildren().add(gridCatalogs);
    }

    /**
     * Creates ComboBox of catalog options
     *
     * @param catalog Catalog
     * @return prepared ComboBox
     */
    private ComboBox<CatalogOption> getCatalogOptions(Catalog catalog) {
        ComboBox<CatalogOption> optionComboBox = new ComboBox<>();
        ObservableList<CatalogOption> catalogOptions = FXCollections.observableArrayList();

        CatalogOption catalogOption = new CatalogOption();
        catalogOption.setValue("Select genre...");
        catalogOptions.add(catalogOption);
        optionComboBox.setValue(catalogOption);

        for (Item item : catalog.getOptions()) {
            catalogOptions.add((CatalogOption) item);
        }

        optionComboBox.setItems(catalogOptions);
        optionComboBox.setCellFactory(param -> new CatalogRenderer());
        optionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.getId() > 0) {
                    catalogOptionIds.add(newValue.getId());
                }
        });
        optionComboBox.setConverter(new StringConverter<CatalogOption>() {
            @Override
            public String toString(CatalogOption object) {
                return object.getValue();
            }

            @Override
            public CatalogOption fromString(String string) {
                return null;
            }
        });

        if (book.getId() > 0) {
            catalogOption = ((CatalogOptionCollection) new CatalogOptionModel().getCollection())
                    .getOptionByBookIdAndCatalogId(book.getId(), catalog.getId());

            if (catalogOption.getId() > 0) {
                optionComboBox.setValue(catalogOption);
            }
        }

        return optionComboBox;
    }

    public void setBook(Book book) {
        this.book = book;
        prepareAuthorsToShow();
        prepareCatalogsToShow();

        if (book.getId() > 0) {
            int year = book.getOriginalValuePublicationYear();
            tfYear.setText(year > 0 ? String.valueOf(year) : "");
            tfBookName.setText(book.getOriginalValueBookName());
            tfPublication.setText(book.getOriginalValuePublication());
            taNotes.setText(book.getOriginalValueNotes());
        }
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfYear.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("[0-9]*") && tfYear.getText().length() < 5) {
                    tfYear.setText(newValue);
                } else {
                    tfYear.setText(oldValue);
                }
        });
    }

    private class ServiceSave extends Service<Boolean> {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    book.setNewValueLibraryId(libraryId);
                    book.setNewValueAuthor(getAuthors());
                    book.setNewValueBookName(tfBookName.getText().trim());
                    book.setNewValuePublication(tfPublication.getText().trim());
                    book.setNewValueNotes(taNotes.getText().trim());

                    // Prepare publication year
                    String year = tfYear.getText();
                    book.setNewValuePublicationYear(year.isEmpty() ? 0 : Integer.valueOf(year));

                    BookCollection collection = new BookCollection();
                    collection.addItem(book);
                    collection.setCatalogOptionIds(catalogOptionIds);

                    return collection.save();
                }
            };
        }
    }

    /*private class TitledBorderedGroup extends VBox {
        private TitledBorderedGroup(String title, Node... content) {
            Label labelTitle = new Label(title);
            labelTitle.getStyleClass().add("bordered-titled-title");
//            StackPane.setAlignment(labelTitle, Pos.TOP_CENTER);

            HBox contentPane = new HBox();
            contentPane.getChildren().addAll(content);
            catalogContainer.getStyleClass().add("bordered-titled-border");
            catalogContainer.getChildren().addAll(labelTitle, contentPane);
        }
    }*/

    private class CatalogRenderer extends ListCell<CatalogOption> {
        @Override
        protected void updateItem(CatalogOption item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "" : item.getValue());
        }
    }

}
