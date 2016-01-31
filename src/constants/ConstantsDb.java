package constants;

public final class ConstantsDb {
    
    // Database connection
    public static final String DB_NAME = ".db/home_library";
    public static final String CONNECTION_URL = "jdbc:derby:" + DB_NAME;
    
    // Table libraries
    public static final String TABLE_LIBRARIES = "libraries";
    public static final String TABLE_LIBRARIES_FIELD_ID = "ID";
    public static final String TABLE_LIBRARIES_FIELD_NAME = "NAME";
    public static final String TABLE_LIBRARIES_FIELD_SORT = "SORT";

    // Table wish_list
    public static final String TABLE_WISH_LIST = "wish_list";
    public static final String TABLE_WISH_LIST_FIELD_ID = "ID";
    public static final String TABLE_WISH_LIST_FIELD_NAME = "NAME";
    public static final String TABLE_WISH_LIST_FIELD_SORT = "SORT";

    // Table catalogs
    public static final String TABLE_CATALOGS = "catalogs";
    public static final String TABLE_CATALOGS_FIELD_ID = "ID";
    public static final String TABLE_CATALOGS_FIELD_NAME = "NAME";
    public static final String TABLE_CATALOGS_FIELD_CODE = "CODE";
    public static final String TABLE_CATALOGS_FIELD_IS_SYSTEM = "IS_SYSTEM";
    // Catalog codes
    public static final String CATALOG_CODE_AUTHOR = "author";
    public static final String CATALOG_CODE_BOOK_NAME = "book_name";
    public static final String CATALOG_CODE_GENRES = "genres";
    public static final String CATALOG_CODE_PUBLICATION_YEAR = "publication_year";

    // Table catalog_options
    public static final String TABLE_CATALOG_OPTIONS = "catalog_options";
    public static final String TABLE_CATALOG_OPTIONS_FIELD_ID = "ID";
    public static final String TABLE_CATALOG_OPTIONS_FIELD_CATALOG_ID = "CATALOG_ID";
    public static final String TABLE_CATALOG_OPTIONS_FIELD_VALUE = "VALUE";

    // Table book_to_catalog_options
    public static final String TABLE_BOOK_TO_CATALOG_OPTIONS = "book_to_catalog_options";
    public static final String TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_ID = "ID";
    public static final String TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_BOOK_ID = "BOOK_ID";
    public static final String TABLE_BOOK_TO_CATALOG_OPTIONS_FIELD_OPTION_ID = "OPTION_ID";

    // Table catalog_to_libraries
    public static final String TABLE_CATALOG_TO_LIBRARIES = "catalog_to_libraries";
    public static final String TABLE_CATALOG_TO_LIBRARIES_FIELD_CATALOG_ID = "CATALOG_ID";
    public static final String TABLE_CATALOG_TO_LIBRARIES_FIELD_LIBRARY_ID = "LIBRARY_ID";
    public static final String TABLE_CATALOG_TO_LIBRARIES_FIELD_SORT = "SORT";

    // Table books
    public static final String TABLE_BOOKS = "books";
    public static final String TABLE_BOOKS_FIELD_ID = "ID";
    public static final String TABLE_BOOKS_FIELD_BOOK_NAME = "BOOK_NAME";
    public static final String TABLE_BOOKS_FIELD_AUTHOR = "AUTHOR";
    public static final String TABLE_BOOKS_FIELD_PUBLICATION = "PUBLICATION";
    public static final String TABLE_BOOKS_FIELD_PUBLICATION_YEAR = "PUBLICATION_YEAR";
    public static final String TABLE_BOOKS_FIELD_NOTES = "NOTES";
    public static final String TABLE_BOOKS_FIELD_LIBRARY_ID = "LIBRARY_ID";

    // Types
    public static final String TYPE_INT = "int";
    public static final String TYPE_STRING = "string";

}
