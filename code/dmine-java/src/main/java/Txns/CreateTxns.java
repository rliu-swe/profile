package Txns;

import Dat.City;
import Dat.Items;
import Dat.NameList;
import Dat.RandomUtils;
import Infra.BindingsModule;
import Infra.Connection;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Create test txns.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class CreateTxns {

    private final Infra.Connection db;

    private List<Person> people = new ArrayList<>();
    private List<Txn> txns = new ArrayList<>();
    private Map<Txn, List<Items>> txn_items = new LinkedHashMap<>();

    @Inject
    public CreateTxns(Connection conn) {
        this.db = conn;
    }

    public static final String SQL_CREATE_PPL_TBL =
        "CREATE TABLE if not exists ppl (" +
            "id int NOT NULL," +
            "first_name varchar(255)," +
            "last_name varchar(255)," +
            "birth_city varchar(255)," +
            "PRIMARY KEY (id)" +
            ")";

    public static final String SQL_CREATE_TXN_TBL =
        "CREATE TABLE if not exists txns (" +
            "id int NOT NULL," +
            "person_id int NOT NULL," +
            "purchase_city varchar(255)," +
            "purchase_date date," +
            "purchase_amount decimal," +
            "num_items int," +
            "PRIMARY KEY (id)," +
            "constraint fk_person\n" +
            "foreign key (person_id)\n" +
            "references ppl(id)" +
            ")";

    public static final String SQL_CREATE_ITEMS_TBL =
        "CREATE TABLE if not exists items (" +
            "id int NOT NULL," +
            "name varchar(255)," +
            "price decimal," +
            "PRIMARY KEY (id)" +
            ")";

    public static final String SQL_CREATE_TXN_ITEMS_TBL =
        "CREATE TABLE if not exists txn_items (" +
            "txn_id int," +
            "item_id int," +
            "constraint fk_txn\n" +
            "foreign key (txn_id)\n" +
            "references txns(id)," +
            "constraint fk_item\n" +
            "foreign key (item_id)\n" +
            "references items(id)\n" +
            ")";

    public static final String SQL_INSERT_PPL_TBL =
        "INSERT INTO ppl (id, first_name, last_name, birth_city) VALUES (?, ?, ?, ?)";

    public static final String SQL_INSERT_TXN_TBL =
        "INSERT INTO txns (id, person_id, purchase_city, purchase_date, purchase_amount, num_items) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String SQL_INSERT_ITEMS_TBL =
        "INSERT INTO items (id, name, price) VALUES (?, ?, ?)";

    public static final String SQL_INSERT_TXN_ITEMS_TBL =
        "INSERT INTO txn_items (txn_id, item_id) VALUES (?, ?)";

    public void generateRandomPeople(int size) {
        for (int i = 0; i < size; i++) {
            String first_name = NameList.FirstNames.INST.getRandom();
            String last_name = NameList.LastNames.INST.getRandom();
            City birth_city = City.getRandom();
            people.add(new Person(i+10, first_name, last_name, birth_city));
        }
    }

    public void createPplTable()
        throws SQLException
    {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(SQL_CREATE_PPL_TBL);
        }
    }

    public void populatePplTable()
        throws SQLException
    {
        try (PreparedStatement stmt = db.prepareStatement(SQL_INSERT_PPL_TBL)) {
            for (Person p : people) {
                int i = 1;
                stmt.setInt(i++, p.id());
                stmt.setString(i++, p.first_name());
                stmt.setString(i++, p.last_name());
                stmt.setString(i++, p.birth_city().getLongName());
                stmt.executeUpdate();
            }
            db.commit();
        }
    }

    public void generateRandomTxns(int size) {
        long aDay = TimeUnit.DAYS.toMillis(1);
        long now = new java.util.Date().getTime();
        java.util.Date twoYearsAgo = new java.util.Date(now - aDay * 365 * 2);
        java.util.Date tenDaysAgo = new java.util.Date(now - aDay * 10);

        for (int i = 0; i < size; i++) {
            Person person = people.get(RandomUtils.intBetween(0, people.size()));
            City purchase_city = City.getRandom();
            java.util.Date purchase_date = RandomUtils.dateBetween(twoYearsAgo, tenDaysAgo);

            int num_items = RandomUtils.intBetween(1, Items.values().length);
            List<Items> purchased_items = new ArrayList<>();
            for (int j = 0; j < num_items; j++)
                purchased_items.add(Items.getRandom());

            double purchase_amount = purchased_items.stream().map(it -> it.price).reduce(0.0, (sum, price) -> sum += price);

            Txn t = new Txn(i+100, person.id(), purchase_city, purchase_date, purchase_amount, num_items);

            this.txns.add(t);
            txn_items.put(t, purchased_items);
        }
    }

    public void createTxnTable()
        throws SQLException
    {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(SQL_CREATE_TXN_TBL);
        }
    }

    public void populateTxnTable()
        throws SQLException
    {
        try (PreparedStatement stmt = db.prepareStatement(SQL_INSERT_TXN_TBL)) {
            for (Txn t: txns) {
                int i = 1;
                stmt.setInt(i++, t.id());
                stmt.setInt(i++, t.person_id());
                stmt.setString(i++, t.purchase_city().getLongName());
                stmt.setDate(i++, new Date(t.purchase_date().getTime()));
                stmt.setBigDecimal(i++, new BigDecimal(t.purchase_amount()));
                stmt.setInt(i++, t.num_items());
                stmt.executeUpdate();
            }
            db.commit();
        }
    }

    public void createItemsTable()
        throws SQLException
    {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(SQL_CREATE_ITEMS_TBL);
        }
    }

    public void populateItemsTable()
        throws SQLException
    {
        try (PreparedStatement stmt = db.prepareStatement(SQL_INSERT_ITEMS_TBL)) {
            for (Items it : Items.values()) {
                int i = 1;
                stmt.setInt(i++, it.id);
                stmt.setString(i++, it.long_name);
                stmt.setBigDecimal(i++, new BigDecimal(it.price));
                stmt.executeUpdate();
            }
            db.commit();
        }
    }

    public void generateRandomTxnItems() {
        for (Txn t : txns) {
        }
    }

    public void createTxnItemsTable()
        throws SQLException
    {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(SQL_CREATE_TXN_ITEMS_TBL);
        }
    }

    public void populateTxnItemsTable()
        throws SQLException
    {
        try (PreparedStatement stmt = db.prepareStatement(SQL_INSERT_TXN_ITEMS_TBL)) {
            for (Map.Entry<Txn, List<Items>> ent : txn_items.entrySet()) {
                Txn txn = ent.getKey();
                List<Items> purchased_items = ent.getValue();
                for (Items it : purchased_items) {
                    stmt.setInt(1, txn.id());
                    stmt.setInt(2, it.id);
                    stmt.executeUpdate();
                }
            }
            db.commit();
        }
    }


    public void createSmallTbl()
        throws SQLException
    {
        createItemsTable();
        populateItemsTable();

        generateRandomPeople(3);
        createPplTable();
        populatePplTable();

        generateRandomTxns(8);
        createTxnTable();
        populateTxnTable();

        generateRandomTxnItems();
        createTxnItemsTable();
        populateTxnItemsTable();

    }


    public void test()
        throws SQLException
    {
        try (Statement stmt = db.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT 'Hello World..'")) {
                rs.first(); //position result to first
                System.out.println(rs.getString(1)); //result is "Hello World!"
            }
        }
    }

    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(new BindingsModule());
        CreateTxns txns = injector.getInstance(CreateTxns.class);

        try {
            //txns.test();
            txns.createSmallTbl();
        }
        catch (SQLException e) {}

    }

}
