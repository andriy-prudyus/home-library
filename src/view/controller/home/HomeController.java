package view.controller.home;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import item.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.LibraryModel;
import item.Library;
import item.WishList;
import model.WishListModel;
import view.controller.CoreController;
import view.controller.catalog.CatalogListController;

public class HomeController extends CoreController implements Initializable {
    
    @FXML
    private AnchorPane apTop;
    
    @FXML
    private AnchorPane apBottom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apTop.getChildren().addAll(getLibraries());
        apBottom.getChildren().addAll(getWishLists());
    }
    
    private List<Button> getLibraries() {
        Button button;
        Library library;
        List<Button> buttons = new ArrayList<>();
        List<Item> items = (new LibraryModel()).getCollection().load();

        for (Item item : items) {
            library = (Library) item;
            final int libraryId = library.getId();
            button = new Button(library.getName());
            button.setId(String.valueOf(libraryId));
            button.getStyleClass().add("cake");
            button.setOnAction((ActionEvent event) -> {
                try {
                    Scene sceneCurrent = apTop.getScene();
                    Stage stage = (Stage) sceneCurrent.getWindow();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/template/catalog/CatalogList.fxml"));
                    Parent sceneNew = fxmlLoader.load();

                    CatalogListController controller = fxmlLoader.<CatalogListController>getController();
                    controller.setLibraryId(libraryId);
                    controller.loadCatalogs();

                    stage.setScene(new Scene(sceneNew, sceneCurrent.getWidth(), sceneCurrent.getHeight()));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error configuring entity.library buttons", e);
                }
            });

            buttons.add(button);
        }

        return buttons;
    }
    
    private List<Button> getWishLists() {
        List<Button> buttons = new ArrayList<>();
        List<Item> items = (new WishListModel()).getCollection().load();
        
        for (Item item : items) {
            WishList wishList = (WishList) item;
            Button button = new Button(wishList.getName());
            button.getStyleClass().add("cake");
            
            buttons.add(button);
        }
        
        return buttons;
    }
    
}
