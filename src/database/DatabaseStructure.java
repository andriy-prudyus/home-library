package database;

import constants.ConstantsDb;
import log.Log;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseStructure extends Database {

    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private static final String LIBRARY_NAME = "My Library";
    private static final String[] genres = {
            "Fiction",
            "Nonfiction"
    };

    // Structure of tables
    private static final String TABLE_CREATE_LIBRARIES = "CREATE TABLE " + ConstantsDb.TABLE_LIBRARIES + " (" +
            ConstantsDb.TABLE_LIBRARIES_FIELD_ID + " INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY " +
            "(START WITH 1, INCREMENT BY 1)," +
            ConstantsDb.TABLE_LIBRARIES_FIELD_NAME + " VARCHAR(255) NOT NULL," +
            ConstantsDb.TABLE_LIBRARIES_FIELD_SORT + " SMALLINT)";
    private static final String TABLE_CREATE_WISH_LIST = "CREATE TABLE " + ConstantsDb.TABLE_WISH_LIST + " (" +
            ConstantsDb.TABLE_WISH_LIST_FIELD_ID + " INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY " +
            "(START WITH 1, INCREMENT BY 1)," +
            ConstantsDb.TABLE_WISH_LIST_FIELD_NAME + " VARCHAR(255) NOT NULL," +
            ConstantsDb.TABLE_WISH_LIST_FIELD_SORT + " SMALLINT)";
    private static final String TABLE_CREATE_CATALOGS = "CREATE TABLE " + ConstantsDb.TABLE_CATALOGS + " (" +
            ConstantsDb.TABLE_CATALOGS_FIELD_ID + " INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY " +
            "(START WITH 1, INCREMENT BY 1)," +
            ConstantsDb.TABLE_CATALOGS_FIELD_NAME + " VARCHAR(255) NOT NULL," +
            ConstantsDb.TABLE_CATALOGS_FIELD_CODE + " VARCHAR(255)," +
            ConstantsDb.TABLE_CATALOGS_FIELD_IS_SYSTEM + " SMALLINT)";
    private static final String TABLE_CREATE_CATALOG_TO_LIBRARIES = "CREATE TABLE " +
            ConstantsDb.TABLE_CATALOG_TO_LIBRARIES + " (" +
            ConstantsDb.TABLE_CATALOG_TO_LIBRARIES_FIELD_CATALOG_ID + " INTEGER," +
            ConstantsDb.TABLE_CATALOG_TO_LIBRARIES_FIELD_LIBRARY_ID + " INTEGER," +
            ConstantsDb.TABLE_CATALOG_TO_LIBRARIES_FIELD_SORT + " SMALLINT)";
    private static final String TABLE_CREATE_BOOKS = "CREATE TABLE " + ConstantsDb.TABLE_BOOKS + " (" +
            ConstantsDb.TABLE_BOOKS_FIELD_ID + " INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY " +
            "(START WITH 1, INCREMENT BY 1)," + ConstantsDb.TABLE_BOOKS_FIELD_BOOK_NAME + " VARCHAR(255)," +
            ConstantsDb.TABLE_BOOKS_FIELD_AUTHOR + " VARCHAR(255)," +
            ConstantsDb.TABLE_BOOKS_FIELD_PUBLICATION + " VARCHAR(255)," +
            ConstantsDb.TABLE_BOOKS_FIELD_PUBLICATION_YEAR + " SMALLINT," +
            ConstantsDb.TABLE_BOOKS_FIELD_NOTES + " VARCHAR(500)," +
            ConstantsDb.TABLE_BOOKS_FIELD_LIBRARY_ID + " INTEGER)";
    private static final String TABLE_CREATE_CATALOG_OPTIONS = "CREATE TABLE " + ConstantsDb.TABLE_CATALOG_OPTIONS +
            " (" + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID +
            " INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY " + "(START WITH 1, INCREMENT BY 1)," +
            ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID + " INTEGER," +
            ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_VALUE + " VARCHAR(500))";
    private static final String TABLE_CREATE_BOOK_TO_CATALOG_OPTIONS = "CREATE TABLE " +
            ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS + " (" + ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_ID +
            " INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_BOOK_ID + " INTEGER," +
            ConstantsDb.TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID + " INTEGER)";
    
    // Initial data of tables
    private static final String TABLE_LIBRARIES_INITIAL_DATA = "INSERT INTO " + ConstantsDb.TABLE_LIBRARIES
            + " (" + ConstantsDb.TABLE_LIBRARIES_FIELD_NAME + ", " + ConstantsDb.TABLE_LIBRARIES_FIELD_SORT +
            ") VALUES ('" + LIBRARY_NAME + "', 0)";
    private static final String TABLE_WISH_LIST_INITIAL_DATA = "INSERT INTO " + ConstantsDb.TABLE_WISH_LIST
            + " (" + ConstantsDb.TABLE_WISH_LIST_FIELD_NAME + ", " + ConstantsDb.TABLE_WISH_LIST_FIELD_SORT +
            ") VALUES ('My Wish List', 0)";
    private static final String TABLE_CATALOGS_INITIAL_DATA = "INSERT INTO " + ConstantsDb.TABLE_CATALOGS + " (" +
            ConstantsDb.TABLE_CATALOGS_FIELD_NAME + ", " + ConstantsDb.TABLE_CATALOGS_FIELD_IS_SYSTEM + ", " +
            ConstantsDb.TABLE_CATALOGS_FIELD_CODE + ") VALUES ('Author', 1, '" + ConstantsDb.CATALOG_CODE_AUTHOR +
            "'), ('Book Name', 1, '" + ConstantsDb.CATALOG_CODE_BOOK_NAME + "'), ('Genres', 1, '" +
            ConstantsDb.CATALOG_CODE_GENRES + "'), ('Publication Year', 1, '" +
            ConstantsDb.CATALOG_CODE_PUBLICATION_YEAR + "')";

    /**
     * Create DB structure if it is necessary
     */
    public void initialize() {
        boolean isDbExists = false;

        try {
            connection = DriverManager.getConnection(ConstantsDb.CONNECTION_URL);
            isDbExists = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error connecting to DB", e);
        } finally {
            close();
        }
        
        if (!isDbExists) {
            createDatabaseStructure();
        }
    }

    private void createDatabaseStructure() {
        try {
            connection = DriverManager.getConnection(ConstantsDb.CONNECTION_URL + ";create=true");
            statement = connection.createStatement();
            
            // Create tables
            statement.execute(TABLE_CREATE_LIBRARIES);
            statement.execute(TABLE_CREATE_WISH_LIST);
            statement.execute(TABLE_CREATE_CATALOGS);
            statement.execute(TABLE_CREATE_CATALOG_TO_LIBRARIES);
            statement.execute(TABLE_CREATE_BOOKS);
            statement.execute(TABLE_CREATE_CATALOG_OPTIONS);
            statement.execute(TABLE_CREATE_BOOK_TO_CATALOG_OPTIONS);

            // Add table data
            statement.execute(TABLE_LIBRARIES_INITIAL_DATA);
            statement.execute(TABLE_WISH_LIST_INITIAL_DATA);
            statement.execute(TABLE_CATALOGS_INITIAL_DATA);
            initTableCatalogToLibraries();
            initTableCatalogOptions();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating DB structure", e);
        } finally {
            close();
        }
    }

    private void initTableCatalogToLibraries() {
        int libraryId = 0;

        try {
            resultSet = statement.executeQuery(
                    "SELECT ID FROM " + ConstantsDb.TABLE_LIBRARIES + " WHERE " +
                    ConstantsDb.TABLE_LIBRARIES_FIELD_NAME + " = '" + LIBRARY_NAME + "'"
            );

            while (resultSet.next()) {
                libraryId = resultSet.getInt(1);
            }

            resultSet = statement.executeQuery("SELECT ID FROM " + ConstantsDb.TABLE_CATALOGS);
            Set<Integer> catalogIds = new HashSet<>();

            while (resultSet.next()) {
                catalogIds.add(resultSet.getInt(1));
            }

            int countCatalogs = catalogIds.size();
            String[] insertParts = new String[countCatalogs];
            int i = 0;

            for (Integer id : catalogIds) {
                insertParts[i++] = "(" + id + ", " + libraryId + ")";
            }

            statement.execute(
                    "INSERT INTO " + ConstantsDb.TABLE_CATALOG_TO_LIBRARIES + " (" +
                    ConstantsDb.TABLE_CATALOG_TO_LIBRARIES_FIELD_CATALOG_ID + ", " +
                    ConstantsDb.TABLE_CATALOG_TO_LIBRARIES_FIELD_LIBRARY_ID + ") VALUES " +
                    String.join(", ", insertParts)
            );
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error initializing table catalog_to_libraries");
        }
    }

    private void initTableCatalogOptions() {
        try {
            int idCatalogGenres = 0;
            resultSet = statement.executeQuery(
                    "SELECT ID FROM " + ConstantsDb.TABLE_CATALOGS + " WHERE " +
                    ConstantsDb.TABLE_CATALOGS_FIELD_CODE + " = '" + ConstantsDb.CATALOG_CODE_GENRES + "'"
            );

            while (resultSet.next()) {
                idCatalogGenres = resultSet.getInt(1);
            }

            String[] insertParts = new String[genres.length];
            int i = 0;

            for (String genre : genres) {
                insertParts[i++] = "(" + idCatalogGenres + ", '" + genre + "')";
            }

            statement.execute(
                    "INSERT INTO " + ConstantsDb.TABLE_CATALOG_OPTIONS + " (" +
                    ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID + ", " +
                    ConstantsDb.TABLE_CATALOG_OPTIONS_FIELD_VALUE + ") VALUES " + String.join(", ", insertParts)
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
