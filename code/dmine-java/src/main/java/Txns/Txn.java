package Txns;

import Dat.City;

/**
 * @author rliu 2022-04
 */
public record Txn(int id, int person_id, City purchase_city, java.util.Date purchase_date, double purchase_amount, int num_items) {}
