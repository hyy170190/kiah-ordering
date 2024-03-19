package ordering_page;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value = Suite.class)

@SuiteClasses(value = { 
		LoginUnitTest.class, 
		RegisterUnitTest.class, 
		GuestUnitTest.class,
		OrderUnitTest.class})

public class OrderingPageTestSuite {
	
}
