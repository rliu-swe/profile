package Dat;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Items.
 *
 * @author rliu 2022-04
 */
public enum Items {
    Apple("apple", 0.50),
    Bicycle("bicycle", 125.25),
    Cat("cat", 399.99),
    Dog("dog", 255.50),
    Elephant("elephant", 1000.00),
    ;

    public final int id;
    public final String long_name;
    public final double price;

    Items(String long_name, double price) {
        this.id = this.ordinal();
        this.long_name = long_name;
        this.price = price;
    }

    public static Items getRandom() {
        int idx = ThreadLocalRandom.current().nextInt(0, Items.values().length);
        return Items.values()[idx];
    }

}
