package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import collection.Collection;
import collection.WishListCollection;
import constants.ConstantsDb;
import item.WishList;

public class WishListModel extends EntityModel {

    @Override
    public Collection getCollection() {
        return new WishListCollection();
    }

}
