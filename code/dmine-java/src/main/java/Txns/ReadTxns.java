package Txns;

import Infra.BindingsModule;
import Infra.Connection;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/*
 * Reads Transactions into the appropriate objects.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class ReadTxns {

    private Connection db;

    public static final String SQL_READ_TXN_ITEMS = "SELECT txn_id, item_id FROM txn_items";

    @Inject
    public ReadTxns(Connection db) {
        this.db = db;
    }

    public Map<Integer, List<Integer>> readTxnItems()
        throws SQLException
    {
        Map<Integer, List<Integer>> txn_items = new LinkedHashMap<>();
        try (PreparedStatement stmt = db.prepareStatement(SQL_READ_TXN_ITEMS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int txn_id = rs.getInt(1);
                int item_id = rs.getInt(2);
                List<Integer> items = txn_items.getOrDefault(txn_id, new ArrayList<>());
                items.add(item_id);
                txn_items.put(txn_id, items);
            }
        }
        for (List<Integer> items : txn_items.values())
            Collections.sort(items);
        return txn_items;
    }


    public static void main(String[] args)
        throws SQLException
    {
        Injector injector = Guice.createInjector(new BindingsModule());
        ReadTxns txns_reader = injector.getInstance(ReadTxns.class);

        Map<Integer, List<Integer>> txn_items = new LinkedHashMap<>();

        txn_items = txns_reader.readTxnItems();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<Integer>> ent : txn_items.entrySet()) {
            sb.append(ent.getKey()).append(": ").append(ent.getValue())
                .append("\n");
        }

        System.out.println(sb.toString());
    }

}
