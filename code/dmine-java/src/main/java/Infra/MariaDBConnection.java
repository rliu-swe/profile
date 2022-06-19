package Infra;

import Infra.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Author: Nurrachman Liu   2022-04
 */
public class MariaDBConnection implements Connection {

    private final java.sql.Connection db;

    public MariaDBConnection()
        throws SQLException
    {
        this.db = DriverManager.getConnection("jdbc:mariadb://localhost/testdb", "root", "mypass");
        db.setAutoCommit(false);
    }

    public Statement createStatement()
        throws SQLException
    {
        return db.createStatement();
    }

    public PreparedStatement prepareStatement(String sql)
        throws SQLException
    {
        return db.prepareStatement(sql);
    }

    public void commit()
        throws SQLException
    {
        db.commit();
    }

}
