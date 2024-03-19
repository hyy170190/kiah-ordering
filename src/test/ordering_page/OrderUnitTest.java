package ordering_page;

import java.io.File;
import java.io.IOException;
import java.util.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(JUnitParamsRunner.class)

public class OrderUnitTest {
	Order o = new Order();
	Member m = new Member();
	NonMember nm = new NonMember();
	Customer customer;
	
	@Test
	@Parameters(method = "getParamsForTestAddOrderedItem")
	public void testAddOrderedItem(String[] itemId, int[] qty, String[] expectedCart) { //Test case #1
		o.fillItemList();
		for(int i = 0; i < itemId.length; i++) {
			o.addOrderedItem(itemId[i], qty[i]);
		}
		List<Item> cart = o.getCart();
		String[] actualCart = new String[cart.size()];
		for(int i = 0; i < cart.size(); i++) {
			Item cAL = cart.get(i);
			actualCart[i] = cAL.getItemID();
		}
		assertArrayEquals(expectedCart, actualCart);
	}
	
	public Object getParamsForTestAddOrderedItem() {
		String[] cart1 = {"C1", "P4", "C11", "P5"};
		String[] cart2 = {"A1", "B2", "123", "Black Forest"};
		String[] cart3 = {"C4", "C14", "C7", "P8"};
		int[] qty = {-1, 0, 1, 2};
		String[] realCart1 = {"C11", "P5"};
		String[] realCart2 = {};
		String[] realCart3 = {"C7"};
		return new Object[] {
				new Object[] {cart1, qty, realCart1},
				new Object[] {cart2, qty, realCart2},
				new Object[] {cart3, qty, realCart3}
		};
	}
	
	@Test(expected = UserException.class)
	@Parameters(method = "getParamsForTestEmptyCart")
	public void testEmptyCart(Customer c) { //Test case #2
		o.getCart();
		System.out.println("testEmptyCart(): " + "Shopping cart contains " + o.getCart().size() + " item.");
		o.computeTotalPrice(c);
	}
	
	public Object getParamsForTestEmptyCart() {
		return new Object[] {
				new Object[] {new Member()},
				new Object[] {new NonMember()}
		};
	}
	
	@Test
	@Parameters(method = "getParamsForTestComputeItemsPrice")
	public void testComputeItemsPrice(String[] cart, int[] qty, double[] expectedDisMPrice, double[] expectedDisNMPrice) { //Test case #3
		o.fillItemList();
		for(int i = 0; i < 5; i++) {
			o.addOrderedItem(cart[i], qty[i]);
		}
		double[] actualItemsPriceM = m.computeItemsPrice(o.getCart());
		double[] actualItemsPriceNM = nm.computeItemsPrice(o.getCart());
		assertArrayEquals(expectedDisMPrice, actualItemsPriceM, 0.1);
		assertArrayEquals(expectedDisNMPrice, actualItemsPriceNM, 0.1);
	}
	
	public Object getParamsForTestComputeItemsPrice() { 
		String[] cart1 = {"C11", "C12", "C13", "P6", "P7"};
		String[] cart2 = {"C1", "C2", "C3", "P1", "P2"};
		int[] qty = {1, 2, 1, 3, 1};
		double[] discountPriceM = {128.25, 342.0, 90.25, 111.15, 28.5};
		double[] priceM = {46.0, 153.0, 120.0, 27.9, 8.2};
		double[] discountPriceNM = {133.0, 351.5, 93.1, 119.7, 30.4};
		double[] priceNM = {46.8, 154.4, 125.0, 29.7, 8.7};
		return new Object[] {
				new Object[] {cart1, qty, discountPriceM, discountPriceNM},
				new Object[] {cart2, qty,priceM, priceNM}
		};
	}
	
	@Test
	@Parameters(method = "getParamsForTestComputePrice")
	public void testComputePrice(Customer c, String district, String[] cart, double expectedTtlPrice, double expectedDelivery, 
			double expectedFinPrice) { //Test case #4
		o.fillItemList();
		for(int i = 0; i < 2; i++) {
			o.addOrderedItem(cart[i], 1);
		}
		double actualTtlPrice = o.computeTotalPrice(c);
		double actualDelivery = c.findDeliveryCharge(district);
		double actualFinPrice = o.computeFinalPrice(actualTtlPrice, actualDelivery);
		assertEquals(expectedTtlPrice, actualTtlPrice, 0.1);
		assertEquals(expectedDelivery, actualDelivery, 0.1);
		assertEquals(expectedFinPrice, actualFinPrice, 0.1);
	}
	
	public Object getParamsForTestComputePrice() {
		ArrayList<Object[]> params = new ArrayList<Object[]>();
		try {
			File dF = new File("OrderTest.txt");
			Scanner inFile = new Scanner(dF);
			while(inFile.hasNext()) {
				Object[] object = new Object[6];
				String str = inFile.nextLine();
				String[] tempList = str.split("#");
				String customerType = tempList[0];
				String district = tempList[1];
				String item1 = tempList[2];
				String item2 = tempList[3];
				String ttlPrice = tempList[4];
				String delivery = tempList[5];
				String finPrice = tempList[6];
				String[] cart = {item1, item2};
				if(customerType.equals("member")) {
					object[0] = new Member();
				}
				if(customerType.equals("nonmember")) {
					object[0] = new NonMember();
				}
				object[1] = district;
				object[2] = cart;
				object[3] = Double.parseDouble(ttlPrice);
				object[4] = Double.parseDouble(delivery);
				object[5] = Double.parseDouble(finPrice);
				params.add(object);
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: OrderTest File error.");
		}
		return params.toArray();
	}
	
	@Test
	@Parameters(method = "getParamsForTestProcessPayment")
	public void testProcessPayment(String actPayWay, String actPayStatus, String expPayWay, String expPayStatus) { //Test case #5
		Order mockOrder = mock(Order.class);
		Payment pay = new Payment(mockOrder);
		pay.processPayment(actPayWay, actPayStatus);
		verify(mockOrder, times(1)).setOrderPayMethod(expPayWay);
		verify(mockOrder, times(1)).setOrderPayStatus(expPayStatus);
	}
	
	public Object getParamsForTestProcessPayment() {
		return new Object[] {
				new Object[] {"null", "Pending For Payment", "null", "Pending For Payment"},
				new Object[] {"Online Banking", "Paid and Ready for Delivery", "Online Banking", "Paid and Ready for Delivery"}
		};
	}
}

