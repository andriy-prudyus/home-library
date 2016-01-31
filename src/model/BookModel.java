package model;

import collection.BookCollection;
import collection.Collection;

public class BookModel extends EntityModel {

    @Override
    public Collection getCollection() {
        return new BookCollection();
    }

}
