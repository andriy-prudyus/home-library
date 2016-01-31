package model;

import collection.CatalogCollection;
import collection.Collection;

public class CatalogModel extends EntityModel {

    @Override
    public Collection getCollection() {
        return new CatalogCollection();
    }

}
