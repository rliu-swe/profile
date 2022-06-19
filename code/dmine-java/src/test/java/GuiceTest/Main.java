package GuiceTest;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


/**
 * Class to test out Guice injection.
 *
 * Full Credit / Citation:   https://riptutorial.com/guice/example/11936/setup-of-a--hello--world---example
 */
public class Main {

    @Inject
    private HelloWorldService service;//hello service

    public static void main(String[] args) {

        Main main = new Main();

        Module module = new HelloWorldModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(main);//injects the implementation of the service

        main.testGuice();
    }

    public void testGuice()
    {
        service.sayHello();//usage of the service
    }
}

