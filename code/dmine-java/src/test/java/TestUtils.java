import org.junit.jupiter.api.Assertions;

import java.util.Collection;
import java.util.Set;


/**
 * Utils for testing.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class TestUtils {

    public static boolean GLOBAL_DEBUG = false;

    public static class Log {
        public static void test(String message) {
            test(message, false);
        }
        public static void test(String message, boolean local_debug) {
            System.out.print(message);
        }
        public static void testln(String message) {
            testln(message, false);
        }
        public static void testln(String message, boolean local_debug) {
            System.out.println(message);
        }
        public static void test_debug(String message) {
            test_debug(message, false);
        }
        public static void test_debug(String message, boolean local_debug) {
            if (GLOBAL_DEBUG || local_debug)
                System.out.println(message);
        }

    }

    public static void StringEquals(String test_name, String correct, String actual) {
        StringEquals(test_name, correct, actual, false);
    }

    public static void StringEquals(String test_name, String correct, String actual, boolean local_debug) {
        if (local_debug || GLOBAL_DEBUG)
            Log.testln(String.format("Testing %s: ", test_name));
        else
            Log.test(String.format("Testing %s: ", test_name));
        Log.test_debug(actual, local_debug);
        Assertions.assertEquals(correct, actual);
        Log.testln("PASSED");
    }


}
