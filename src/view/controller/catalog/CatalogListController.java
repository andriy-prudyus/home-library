package view.controller.catalog;

import collection.CatalogCollection;
import item.Catalog;
import item.Item;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.CatalogModel;
import view.controller.CoreController;
import view.controller.book.BookListController;
import view.helper.Helper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class CatalogListController extends CoreController implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private GridPane catalogContainer;

    @FXML
    private void onBackClicked(Event event) {
        (new Helper()).loadScene("/view/template/home/Home.fxml", btnBack);
    }

    private int libraryId = -1;

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public void loadCatalogs() {
        Button button;
        Catalog catalog;
        List<Item> items = ((CatalogCollection) (new CatalogModel()).getCollection()).loadByLibraryId(libraryId);
        int i = 1;

        for (Item item : items) {
            catalog = (Catalog) item;
            button = new Button(catalog.getName());
            int catalogId = catalog.getId();
            button.setId(String.valueOf(catalogId));
            button.getStyleClass().add("cake");
            button.setOnAction((ActionEvent event) -> {
                try {
                    Scene sceneCurrent = btnBack.getScene();
                    Stage stage = (Stage) sceneCurrent.getWindow();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/template/book/BookList.fxml"));
                    Parent sceneNew = fxmlLoader.load();

                    BookListController controller = fxmlLoader.<BookListController>getController();
                    controller.setCatalogId(catalogId);
                    controller.setLibraryId(libraryId);
                    controller.prepareData();

                    stage.setScene(new Scene(sceneNew, sceneCurrent.getWidth(), sceneCurrent.getHeight()));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error configuring catalog buttons", e);
                }
            });
            catalogContainer.add(button, i++, 1);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

}
