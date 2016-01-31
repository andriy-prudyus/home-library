package item;

public class WishList extends Item {
    
    private String name;
    private int sort;

    public String getName() {
        return name;
    }

    public int getSort() {
        return sort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
