package Dat;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates a random Date.
 * Full Credit / citation:  https://www.baeldung.com/java-random-dates
*/
public class RandomUtils {

    public static Date dateBetween(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
            .current()
            .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    public static double doubleBetween(double min, double max, double step_size) {
        Random r = new Random();
        return (r.nextInt((int)((max-min)*step_size+1))+min*step_size) / step_size;
    }

    public static int intBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);  // +1 for max to be inclusive
    }
}
