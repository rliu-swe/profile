import Infra.BindingsModule;
import Txns.CreateTxns;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


/**
 * Author: Nurrachman Liu   2022-04
 */
class TestTxnsCreate {

    @Test
    void test()
        throws SQLException
    {
        Injector injector = Guice.createInjector(new BindingsModule());
        CreateTxns txns = injector.getInstance(CreateTxns.class);

        txns.test();


    }

}