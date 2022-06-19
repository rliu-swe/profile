/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package inf.app;

import inf.list.LinkedList;

import static inf.utilities.StringUtils.join;
import static inf.utilities.StringUtils.split;
import static inf.app.MessageUtils.getMessage;

import org.apache.commons.text.WordUtils;

public class App {
    public static void main(String[] args) {
        LinkedList tokens;
        tokens = split(getMessage());
        String result = join(tokens);
        System.out.println(WordUtils.capitalize(result));
    }
}
