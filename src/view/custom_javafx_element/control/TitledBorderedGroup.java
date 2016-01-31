package view.custom_javafx_element.control;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TitledBorderedGroup extends StackPane {

    private Label labelTitle = new Label();
    private StackPane contentPane = new StackPane();
    private Node content;

    public TitledBorderedGroup() {
        labelTitle.setText("default title");
//        labelTitle.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(labelTitle, Pos.TOP_CENTER);

//        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(labelTitle, contentPane);
    }

    public void setContent(Node content) {
        contentPane.getChildren().add(content);
    }

    public Node getContent() {
        return content;
    }

    public void setTitle(String title) {
        labelTitle.setText(title);
    }

    public String getTitle() {
        return labelTitle.getText();
    }
}
