package ordering_page;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)

public class PageIntegrationTest {
	Order o = new Order();
	Member m = new Member();

	@Test
	@Parameters(method = "getParamForTestLoginOrder")
	public void testLoginOrder(String userName, String password, String[] itemList, int[] qty, String payWay,
			String payStatus, String expName, double expDelivery, double expTtlPrice, double expFinalPrice) { // Test case #1
		String[] info = m.login(userName, password);
		Member memberLog = new Member(info[0], info[1], info[2], info[3], info[4], info[5], userName, password);
		o.fillItemList();
		for (int i = 0; i < itemList.length; i++) {
			o.addOrderedItem(itemList[i], qty[i]);
		}
		double delivery = memberLog.findDeliveryCharge(memberLog.getDistrict());
		double ttlPrice = o.computeTotalPrice(memberLog);
		double finalPrice = o.computeFinalPrice(ttlPrice, delivery);
		String orderId = o.generateOrderID();
		Order order = new Order(orderId, memberLog.getName(), ttlPrice, delivery, finalPrice, "null",
				"Pending For Payment");
		Payment p = new Payment(order);
		p.processPayment(payWay, payStatus);
		assertEquals(expName, order.getCustomerName());
		assertEquals(expDelivery, order.getDeliveryCharge(), 0.1);
		assertEquals(expTtlPrice, order.getTotalPrice(), 0.1);
		assertEquals(expFinalPrice, order.getFinalPrice(), 0.1);
		assertEquals(payWay, order.getOrderPayMethod());
		assertEquals(payStatus, order.getOrderPayStatus());
	}
	
	private Object[] getParamForTestLoginOrder() {
		String[] itemList1 = { "C1", "C2", "C3" };
		String[] itemList2 = { "P1", "P2", "P3" };
		int[] qtyList1 = { 2, 3, 4 };
		int[] qtyList2 = { 3, 4, 5 };
		return new Object[] {
				new Object[] { "admin", "admin", itemList1, qtyList1, "Online Banking", "Paid and Ready for Delivery",
						"Kiah", 5.0, 801.5, 806.5 },
				new Object[] { "kr", "990", itemList2, qtyList2, "Credit / Debit Card", "Paid and Ready for Delivery",
						"Karen", 5.0, 81.7, 86.7 } };
	}

	@Test
	@Parameters(method = "getParamForTestRegisterOrder")
	public void testRegisterOrder(String name, String phone, String address, String district, String postalCode,
			String state, String userName, String password, String[] itemList, int[] qty, String payWay,
			String payStatus, String expName, double expDelivery, double expTtlPrice, double expFinalPrice) { //Test case #2
		Member memberReg = new Member(name, phone, address, district, postalCode, state, userName, password);
		boolean isPass = memberReg.register(userName, name, phone, address, district);
		o.fillItemList();
		for (int i = 0; i < itemList.length; i++) {
			o.addOrderedItem(itemList[i], qty[i]);
		}
		double delivery = memberReg.findDeliveryCharge(memberReg.getDistrict());
		double ttlPrice = o.computeTotalPrice(memberReg);
		double finalPrice = o.computeFinalPrice(ttlPrice, delivery);
		String orderId = o.generateOrderID();
		Order order = new Order(orderId, memberReg.getName(), ttlPrice, delivery, finalPrice, "null",
				"Pending For Payment");
		Payment p = new Payment(order);
		p.processPayment(payWay, payStatus);
		assertTrue(isPass);
		assertEquals(expName, order.getCustomerName());
		assertEquals(expDelivery, order.getDeliveryCharge(), 0.1);
		assertEquals(expTtlPrice, order.getTotalPrice(), 0.1);
		assertEquals(expFinalPrice, order.getFinalPrice(), 0.1);
		assertEquals(payWay, order.getOrderPayMethod());
		assertEquals(payStatus, order.getOrderPayStatus());
	}
	
	private Object[] getParamForTestRegisterOrder() {
		String[] itemList1 = {"C1", "C2", "C3"};
		String[] itemList2 = {"P1", "P2", "P3"};
		int[] qtyList1 = {2, 3, 4};
		int[] qtyList2 = {3, 4, 5};
		return new Object[] {
				new Object[]{"jack","0107660573","No1,jalan hahaha,taman haha","asahan","70000","Melaka","jackson", "jk000", 
						itemList1, qtyList1, "Online Banking", "Paid and Ready for Delivery", "jack", 
						4.0, 801.5, 805.5},
				new Object[] {"jestina","0106667788","No12,jalan88,taman GG","bemban","73000","Melaka","jestina", "jt000", 
						itemList2, qtyList2, "Credit / Debit Card", "Paid and Ready for Delivery", "jestina", 
						4.0, 81.7, 85.7}
		};
	}

	@Test
	@Parameters(method = "parametersForTestGuestOrder")
	public void testGuestOrder(String aName, String aPhone, String aAddress, String aDistrict, String aPostalCode, String aState, 
			String[] optionItem, int[] quantityItem, double expTotalPrice, double expDeliveryCharge, double expFinalPrice, 
			String payWay, String payStatus) { //Test case #3
		NonMember nm = new NonMember(aName, aPhone, aAddress, aDistrict, aPostalCode, aState);
		boolean isPass = nm.verifyGuest();
		o.fillItemList();
		for(int i = 0; i < optionItem.length; i++) {
			o.addOrderedItem(optionItem[i], quantityItem[i]);
		}
		double totalPrice = o.computeTotalPrice(nm);
		double deliveryCharge = nm.findDeliveryCharge(nm.getDistrict());
		double finalPrice = o.computeFinalPrice(totalPrice, deliveryCharge);
		Order order = new Order(o.generateOrderID(), nm.getName(), totalPrice, deliveryCharge, finalPrice, "null", "Pending For Payment");
		Payment p = new Payment(order);
		p.processPayment(payWay, payStatus);
		assertTrue(isPass);
		assertEquals(expTotalPrice, totalPrice, 0.0);
		assertEquals(expDeliveryCharge, deliveryCharge, 0.0);
		assertEquals(expFinalPrice, finalPrice, 0.0);
		assertEquals(payWay, order.getOrderPayMethod());
		assertEquals(payStatus, order.getOrderPayStatus());
	}
	
	private Object[] parametersForTestGuestOrder() {
		return new Object[] {
				new Object[] {"a", "0198765432", "1, Jalan Satu", "alor gajah", "70000", "Melaka", new String[] {"C11"}, new int[] {10}, 
						1330, 2.50, 1332.50, "Credit / Debit Card", "Paid and Ready for Delivery"},
				new Object[] {"b", "0187654321", "2, Jalan Dua", "alor gajah", "70000", "Melaka", new String[] {"P1"}, new int[] {1},
						9.90, 2.50, 15.4, "Credit / Debit Card", "Paid and Ready for Delivery"},
		};
	}
}
