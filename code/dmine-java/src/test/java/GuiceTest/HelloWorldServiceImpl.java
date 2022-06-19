package GuiceTest;


/**
 * Class to test out Guice injection.
 *
 * Full Credit / Citation:   https://riptutorial.com/guice/example/11936/setup-of-a--hello--world---example
 */
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public void sayHello() {
        System.out.println("Hello, world!");
    }
}
