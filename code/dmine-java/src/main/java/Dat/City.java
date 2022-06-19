package Dat;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Cities
 *
 * Author: Nurrachman Liu   2022-04
 */
public enum City {
    ANN("Ann Arbor"),
    BOS("Boston"),
    DAL("Dallas"),
    HOU("Houston"),
    IRV("Irvine"),
    LA("Los Angeles"),
    LON("London"),
    NYC("New York"),
    PAR("Paris"),
    SD("San Diego"),
    SJ("San Jose"),
    SF("San Francisco"),
    SEA("Seattle"),
    TPE("Taipei"),
    TOK("Tokyo"),
    TOR("Toronto"),
    VAN("Vancouver"),
    ;

    private String long_name;

    City(String long_name) {
        this.long_name = long_name;
    }

    public String getLongName() {
        return long_name;
    }

    public static City getRandom() {
        int idx = ThreadLocalRandom.current().nextInt(0, City.values().length);
        return City.values()[idx];
    }

}
