package Txns;

import Dat.City;


/**
 * @author rliu 2022-04
 */
public record Person(int id, String first_name, String last_name, City birth_city) {}
