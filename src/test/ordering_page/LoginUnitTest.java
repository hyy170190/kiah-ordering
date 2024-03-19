package ordering_page;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)

public class LoginUnitTest {
	Member m = new Member();
	
	@Test(expected = UserException.class)
	@Parameters({"admin, 123", "123, admin", "123, 123"})
	public void testLoginFail(String userName, String password) { //Test case #1
		m.login(userName, password);
	}
	
	@Test
	public void testLoginSuccess() { //Test case #2
		String[] actInfo = m.login("admin", "admin");
		String[] expInfo = {"Kiah", "0123456789", "1, Jalan 1, Taman ABC", "Ayer Keroh", "70000", "Melaka"};
		assertArrayEquals(expInfo, actInfo);
	}
}
