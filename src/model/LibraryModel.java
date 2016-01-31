package model;

import collection.Collection;
import collection.LibraryCollection;

public class LibraryModel extends EntityModel {

    @Override
    public Collection getCollection() {
        return new LibraryCollection();
    }
    
}
