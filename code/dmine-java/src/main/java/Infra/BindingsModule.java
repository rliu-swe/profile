package Infra;

import com.google.inject.AbstractModule;

/**
 * Author: Nurrachman Liu   2022-04
 */
public class BindingsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Connection.class).to(MariaDBConnection.class);
    }

}
