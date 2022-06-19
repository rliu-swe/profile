package Utils;

import java.util.Collections;
import java.util.List;

/**
 * Utilities for printing.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class PrintUtils {

    public static String printListsInColumns(List<List<?>> lsts) {
        // Track max widths
        List<Integer> max_widths = lsts.stream()
            .map(lst -> Collections.max(lst.stream().map(Object::toString).map(String::length).toList()))
            .toList();

        // Track max rows
        int max_rows = Collections.max(lsts.stream().map(lst -> lst.size()).toList());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < max_rows; i++) {
            for (int j = 0; j < lsts.size(); j++) {
                List<?> lst = lsts.get(j);
                sb.append(
                    String.format(
                        "%" + max_widths.get(j) + "s",
                        i < lst.size() ? lst.get(i).toString() : ""
                    )
                );
            }
        }

        return sb.toString();
    }
}
