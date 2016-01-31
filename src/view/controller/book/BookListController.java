package view.controller.book;

import collection.BookCollection;
import collection.CatalogOptionCollection;
import item.Book;
import item.CatalogOption;
import item.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import view.controller.CoreController;
import view.controller.catalog.CatalogListController;
import view.helper.Helper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class BookListController extends CoreController implements Initializable {

    @FXML
    private TreeView<CatalogOption> tvCatalogs;

    @FXML
    private TableView<Book> tvBooks;

    @FXML
    private TableColumn<Book, String> tcAuthor;

    @FXML
    private TableColumn<Book, String> tcBookName;

    @FXML
    private TableColumn<Book, String> tcPublicationYear;

    @FXML
    private TableColumn<Book, String> tcPublication;

    @FXML
    private TableColumn<Book, String> tcNotes;

    @FXML
    private Button btnEdit;

    private int libraryId = -1;
    private int catalogId = -1;
    private ObservableList<Book> booksCollection = FXCollections.observableArrayList();

    @FXML
    private void onNewClicked(ActionEvent event) {
        startBookEditWindow(new Book(), event);
    }

    @FXML
    private void onEditClicked(ActionEvent event) {
        Book book = tvBooks.getSelectionModel().getSelectedItem();
        startBookEditWindow(book, event);
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        try {
            Scene sceneCurrent = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) sceneCurrent.getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/template/catalog/CatalogList.fxml"));
            Parent sceneNew = fxmlLoader.load();

            CatalogListController controller = fxmlLoader.<CatalogListController>getController();
            controller.setLibraryId(libraryId);
            controller.loadCatalogs();

            stage.setScene(new Scene(sceneNew, sceneCurrent.getWidth(), sceneCurrent.getHeight()));
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error opening Catalog List window", e);
        }
    }

    private void startBookEditWindow(Book book, ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/template/book/BookEdit.fxml"));
            Parent root = fxmlLoader.load();

            BookEditController controller = fxmlLoader.<BookEditController>getController();
            controller.setLibraryId(libraryId);
            controller.setBook(book);

            stage.setScene(new Scene(root));
            stage.setTitle("New Book");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error opening New Book window", e);
        }
    }

    private void prepareCatalogs() {
        TreeItem<CatalogOption> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        List<Item> catalogOptions = new CatalogOptionCollection().loadByCatalogId(catalogId);

        for (Item item : catalogOptions) {
            rootItem.getChildren().add(new TreeItem<>((CatalogOption) item));
        }

        tvCatalogs.setRoot(rootItem);
        tvCatalogs.setCellFactory((param) -> new CatalogRenderer());
    }

    private void prepareBooksCollection() {
        List<Item> items = (new BookCollection()).loadByLibraryId(libraryId);

        for (Item item : items) {
            booksCollection.add((Book) item);
        }
    }

    private void prepareBookTableColumns() {
        tcAuthor.setCellValueFactory(param -> {
//                String author = ((Book) ((TableColumn.CellDataFeatures) param).getValue()).getAuthor();
                String author = param.getValue().getOriginalValueAuthor();
                return new SimpleStringProperty(Helper.getAuthorsFromValue(author));
        });
        tcPublicationYear.setCellValueFactory(param -> {
            int year = param.getValue().getOriginalValuePublicationYear();
            return new SimpleStringProperty(year > 0 ? String.valueOf(year) : "");
        });
        tcBookName.setCellValueFactory(new PropertyValueFactory<>("originalValueBookName"));
        tcPublication.setCellValueFactory(new PropertyValueFactory<>("originalValuePublication"));
        tcNotes.setCellValueFactory(new PropertyValueFactory<>("originalValueNotes"));

        // Set width of columns
        tcAuthor.prefWidthProperty().bind(tvBooks.widthProperty().divide(4));
        tcBookName.prefWidthProperty().bind(tvBooks.widthProperty().divide(2));
        tcPublicationYear.prefWidthProperty().bind(tvBooks.widthProperty().divide(20));
        tcPublication.prefWidthProperty().bind(tvBooks.widthProperty().divide(10));
        tcNotes.prefWidthProperty().bind(tvBooks.widthProperty().divide(10.3));
    }

    private void prepareBookTableData() {
        tvBooks.setItems(booksCollection);
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public void prepareData() {
        prepareCatalogs();
        prepareBooksCollection();
        prepareBookTableColumns();
        prepareBookTableData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tvBooks.setRowFactory(param -> {
                final TableRow<Book> row = new TableRow<>();
                row.setOnMouseClicked(event -> btnEdit.setDisable(false));
                return row;
        });
    }

    private class CatalogRenderer extends TreeCell<CatalogOption> {

        @Override
        protected void updateItem(CatalogOption item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "" : item.getValue());
        }

    }

}
