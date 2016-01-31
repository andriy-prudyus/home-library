package model;

import collection.CatalogOptionCollection;
import collection.Collection;

public class CatalogOptionModel extends EntityModel {

    @Override
    public Collection getCollection() {
        return new CatalogOptionCollection();
    }

}
