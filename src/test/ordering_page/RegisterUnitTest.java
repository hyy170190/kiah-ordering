package ordering_page;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)

public class RegisterUnitTest {
	private Object[] memberParameterV1() {

		return new Object[] {
				new Object[] { "Jordan", "0107654321", "22, Jalan 22, Taman 22", "asahan", "75000", "Johor", "Jordan",
						"jjdd" },
				new Object[] { "Jordan", "0107654321", "22, Jalan 22, Taman 22", "JB", "75000", "Melaka", "Jordan",
						"jjdd" },
				new Object[] { "Jordan", "0107654321", "22, Jalan 22, Taman 22", "JB", "75000", "Johor", "Jordan",
						"jjdd" }
		};
	}

	@Test(expected = UserException.class)
	@Parameters(method = "memberParameterV1")
	public void testMember(String name, String phone, String address, String district, String postalCode, String state,
			String userName, String password) { //Test case #1
		Member member = new Member(name, phone, address, district, postalCode, state, userName, password);
	}

	private Object[] memberParameterV2() {
		return new Object[] {
				new Object[] { "Jordan", "0107654321", "22, Jalan 22, Taman 22", "asahan", "75000", "Melaka", "admin",
						"jjdd" },
				new Object[] { "Kiah", "0123456789", "1, Jalan 1, Taman ABC", "Ayer Keroh", "70000", "Melaka", "Jordan",
						"jjdd" }
		};
	}

	@Test
	@Parameters(method = "memberParameterV2")
	public void testRegisterFalse(String name, String phone, String address, String district, String postalCode,
			String state, String userName, String password) { //Test case #2
		Member member = new Member(name, phone, address, district, postalCode, state, userName, password);
		boolean actual = member.register(userName, name, phone, address, district);
		assertFalse(actual);
	}

	private Object[] memberParameterV3() {

		return new Object[] {
				new Object[] { "Jordan", "0107654321", "1, Jalan 1, Taman ABC", "Asahan", "75000", "Melaka", "Jordan",
						"jjdd" },
				new Object[] { "Kiah", "0123456789", "1, jalan Melaka 1", "Asahan", "75000", "Melaka", "kiah", "1234" },
				new Object[] { "Karen", "0123456873", "22, Jalan 22, Taman 22", "Asahan", "75000", "Melaka", "kate99",
						"jkjk" } 
		};
	}

	@Test
	@Parameters(method = "memberParameterV3")
	public void testRegisterTrue(String name, String phone, String address, String district, String postalCode,
			String state, String userName, String password) { //Test case #3
		Member member = new Member(name, phone, address, district, postalCode, state, userName, password);
		boolean actual = member.register(userName, name, phone, address, district);
		assertTrue(actual);

	}
}
