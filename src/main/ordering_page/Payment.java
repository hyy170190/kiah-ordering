package ordering_page;

import java.util.*;

public class Payment {
	private String payMethod;
	private String payStatus;
	Order order;
	OrderingPage op;
	
	public String getPayMethod() {
		return payMethod;
	}
	public String getPayStatus() {
		return payStatus;
	}
	
	public void setPayMethod(String newPayMethod) {
		payMethod = newPayMethod;
	}
	public void setPayStatus(String newStatus) {
		payStatus = newStatus;
	}
	
	public Payment(Order order) {
		this.order = order;
	}
	public Payment() {
		order = new Order();
	}
	
	public void processPayment(String payWay, String payStatus) {
		order.setOrderPayMethod(payWay);
		order.setOrderPayStatus(payStatus);
	}
	public void processPendingPayment(String aOrderId, String aPayMethod, String aPayStatus) {
		boolean notExist = true;
		order.fillOrderList();
		List<Order> orders = order.getOrder();
		for(int i = 0; i < orders.size(); i++) {
			Order oAL = orders.get(i);
			if(aOrderId.equals(oAL.getOrderID()) && oAL.getOrderPayStatus().equals("Paid and Ready for Delivery")) {
				throw new UserException("ERROR: Your order is already paid.");
			}
			if(aOrderId.equals(oAL.getOrderID()) && oAL.getOrderPayStatus().equals("Pending For Payment")) {
				oAL.setOrderPayMethod(aPayMethod);
				oAL.setOrderPayStatus(aPayStatus);
				System.out.println("MESSAGE: Payment is successful. Your order will be delivered soon.");
				notExist = false;
			}
		}
		if(notExist) {
			throw new UserException("ERROR: Your order ID is incorrect or not exist.");
		}
		order.saveOrderList();
	}
}

