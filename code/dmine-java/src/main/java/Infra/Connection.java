package Infra;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A db connection.
 *
 * @author rliu 2022-04
 */
public interface Connection {

    public Statement createStatement() throws SQLException;

    public PreparedStatement prepareStatement(String sql) throws SQLException;

    public void commit() throws SQLException;

}
