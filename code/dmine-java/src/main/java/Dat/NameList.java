package Dat;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Author: Nurrachman Liu   2022-04
 */
public abstract class NameList {

    public abstract int getSize();

    public abstract List<String> getNames();

    public String getRandom() {
        int idx = ThreadLocalRandom.current().nextInt(0, this.getSize());  // nextInt(inclusive, exclusive)
        return this.getNames().get(idx);
    }

    /**
     * First names.
     */
    public static class FirstNames extends NameList {

        public static final FirstNames INST = new FirstNames();

        public static List<String> names = List.of(
            "Adam",
            "Anna",
            "Bob",
            "Bonnie",
            "Cara",
            "Cory",
            "Corina",
            "Elizabeth",
            "Francis",
            "Francine",
            "Frank",
            "Gabe",
            "Gabby",
            "Gavin",
            "Happy",
            "Hammy",
            "Horatio",
            "Icarus",
            "Idrina",
            "John",
            "Jane",
            "Jack",
            "Joe",
            "Jenny",
            "Johnny",
            "Karen",
            "Karina",
            "Korine",
            "Laura",
            "Lee",
            "Lorie",
            "Mandy",
            "Mary",
            "Norman",
            "Oliver",
            "Olivia",
            "Peter",
            "Petra",
            "Patricia",
            "Patty",
            "Queenie",
            "Ria",
            "Robert",
            "Sandy",
            "Sebastian",
            "Sora",
            "Tae",
            "Terry",
            "Tammy",
            "Tom",
            "Tricia",
            "Trish",
            "Uriah",
            "Van",
            "Wilson",
            "Xara",
            "Yves",
            "Zack"
        );

        @Override
        public int getSize() {
            return names.size();
        }

        @Override
        public List<String> getNames() {
            return names;
        }
    }

    public static class LastNames extends NameList {

        public static final LastNames INST = new LastNames();

        public static List<String> names = List.of(
            "Abraham",
            "Beach",
            "Daniels",
            "Dobbs",
            "Doe",
            "Fitzgerald",
            "Houghton",
            "Irving",
            "Johnson",
            "Jacks",
            "Jeffreys",
            "Lau",
            "Lee",
            "Lous",
            "Musk",
            "Newell",
            "Newman",
            "Olivers",
            "Owens",
            "Porter",
            "Quest",
            "Roberts",
            "Smith",
            "Star",
            "Terries",
            "Unders",
            "Van Ders",
            "Wells",
            "Wong",
            "Zhou"
        );

        @Override
        public int getSize() {
            return names.size();
        }

        @Override
        public List<String> getNames() {
            return names;
        }
    }

}
