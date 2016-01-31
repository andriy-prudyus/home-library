package item;

import java.util.ArrayList;
import java.util.List;

public class Catalog extends Item {

    private String name;
    private String code;
    private int sort;
    private boolean isSystem;
    private List<Item> options = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Item> getOptions() {
        return options;
    }

    public void setOptions(List<Item> options) {
        this.options = options;
    }
}
