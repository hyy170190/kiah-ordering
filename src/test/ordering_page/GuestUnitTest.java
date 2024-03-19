package ordering_page;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)

public class GuestUnitTest {
	private Object[] nonMemberParameterV1() {

		return new Object[] {
				new Object[] { "Janice", "0102233444", "99, Jalan 99, Taman 99", "durian tunggal", "75000", "kedah" },
				new Object[] { "Janice", "0102233444", "99, Jalan 99, Taman 99", "lunas", "75000", "melaka" },
				new Object[] { "Janice", "0102233444", "99, Jalan 99, Taman 99", "lunas", "75000", "kedah" }

		};
	}

	@Test(expected = UserException.class)
	@Parameters(method = "nonMemberParameterV1")
	public void testNonMember(String name, String phone, String address, String district, String postalCode,
			String state) { //Test case #1

		NonMember nonMember = new NonMember(name, phone, address, district, postalCode, state);

	}

	private Object[] nonMemberParameterV2() {

		return new Object[] {
				new Object[] { "Kiah", "0123456789", "99, Jalan 99, Taman 99", "durian tunggal", "75000", "Melaka" } };
	}

	@Test
	@Parameters(method = "nonMemberParameterV2")
	public void testNonMemberVerifyFalse(String name, String phone, String address, String district, String postalCode,
			String state) { //Test case #2
		
		NonMember nonMember = new NonMember(name, phone, address, district, postalCode, state);
		boolean actual = nonMember.verifyGuest();
		assertFalse(actual);

	}

	private Object[] nonMemberParameterV3() {

		return new Object[] { new Object[] { "Kris Wu", "0102233444", "99, Jalan 99, Taman 99", "durian tunggal",
				"75000", "Melaka" } };
	}

	@Test
	@Parameters(method = "nonMemberParameterV3")
	public void testNonMemberVerifyTrue(String name, String phone, String address, String district, String postalCode,
			String state) { //Test case #3
		NonMember nonMember = new NonMember(name, phone, address, district, postalCode, state);
		boolean actual = nonMember.verifyGuest();
		assertTrue(actual);

	}
}
