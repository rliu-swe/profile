package GuiceTest;

import com.google.inject.AbstractModule;


/**
 * Class to test out Guice injection.
 *
 * Full Credit / Citation:   https://riptutorial.com/guice/example/11936/setup-of-a--hello--world---example
 */
public class HelloWorldModule extends AbstractModule {
    protected void configure() {
        bind(HelloWorldService.class).to(HelloWorldServiceImpl.class);
    }
}
