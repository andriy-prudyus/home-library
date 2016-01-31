package view.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class contains functions useful for FX controllers
 */
public class Helper {

    private static final String DELIMITER_BETWEEN_AUTHORS = "<&>";
    private static final String DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME = "<#>";
    private Logger logger = Logger.getLogger(Log.class.getName());

    public void loadScene(String templatePath, Node node) {
        try {
            Scene sceneCurrent = node.getScene();
            Stage stage = (Stage) sceneCurrent.getWindow();
            Parent sceneNew = FXMLLoader.load(getClass().getResource(templatePath));
            stage.setScene(new Scene(sceneNew, sceneCurrent.getWidth(), sceneCurrent.getHeight()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading scene", e);
        }
    }

    /**
     * Forms List of authors from value getting from database
     *
     * @param value string of authors getting from database
     * @return List of authors
     */
    public static List<Author> getAuthorListFromValue(String value) {
        List<Author> authorList = new ArrayList<>();

        if (value == null || value.isEmpty()) {
            return authorList;
        }

        int index;
        Author author;
        String[] authors = value.split(DELIMITER_BETWEEN_AUTHORS);

        for (String authorValue : authors) {
            index = authorValue.indexOf(DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME);
            String firstName = "";
            String lastName = "";

            if (index == 0) {
                lastName = authorValue.replace(DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME, "");
            } else if (index == (authorValue.length() - DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME.length())) {
                firstName = authorValue.replace(DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME, "");
            } else {
                String[] authorParts = authorValue.split(DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME);
                firstName = authorParts[0];
                lastName = authorParts[1];
            }

            author = new Author();
            author.setFirstName(firstName);
            author.setLastName(lastName);

            authorList.add(author);
        }

        return authorList;
    }

    /**
     * Forms value with authors which can be saved in database
     *
     * @param authorList list of authors
     * @return value
     */
    public static String getAuthorValueFromList(List<Author> authorList) {
        StringBuilder result = new StringBuilder();
        boolean flag = false;

        if (authorList == null) {
            return "";
        }

        for (Author author : authorList) {
            if (flag) {
                result.append(DELIMITER_BETWEEN_AUTHORS);
            }

            result.append(author.getFirstName());
            result.append(DELIMITER_BETWEEN_AUTHOR_FIRSTNAME_LASTNAME);
            result.append(author.getLastName());

            flag = true;
        }

        return result.toString();
    }

    /**
     * Forms value with authors which can be represented in GUI table
     *
     * @param value data getting from database
     * @return value
     */
    public static String getAuthorsFromValue(String value) {
        StringBuilder result = new StringBuilder();
        List<Author> authorList = getAuthorListFromValue(value);
        boolean flag = false;

        for (Author author : authorList) {
            if (flag) {
                result.append(", ");
            }

            result.append(author.getFirstName());
            result.append(" ");
            result.append(author.getLastName());

            flag = true;
        }

        return result.toString();
    }

}
