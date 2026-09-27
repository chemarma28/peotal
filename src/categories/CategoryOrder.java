package categories;

import java.util.List;

public class CategoryOrder {

    public List<Item> items;

    public static class Item {

        public Long id;
        public Integer ordinal;

        public Item() {
        }

        public Item(Long id, Integer ordinal) {
            this.id = id;
            this.ordinal = ordinal;
        }
    }
}
