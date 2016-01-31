package model;

import collection.Collection;
import log.Log;

import java.util.logging.Logger;

public abstract class EntityModel {

    protected static final Logger logger = Logger.getLogger(Log.class.getName());

//    protected Collection collection;

    public abstract Collection getCollection();

//    public void setCollection(Collection collection) {
//        this.collection = collection;
//    }
}
