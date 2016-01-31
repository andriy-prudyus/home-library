package model;

import collection.BookCatalogOptionCollection;
import collection.Collection;

public class BookCatalogOptionModel extends EntityModel {

    @Override
    public Collection getCollection() {
        return new BookCatalogOptionCollection();
    }

}
