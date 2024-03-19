package ordering_page;

import java.util.*;
import java.io.*;

public class Order {
	private String orderID;
	private double totalPrice;
	private double deliveryCharge; 
	private double finalPrice;
	private String orderPayMethod;
	private String orderPayStatus;
	private String customerName;
	private List<Item> cart = new ArrayList<Item>();
	private List<Order> orders = new ArrayList<Order>();
	ArrayList<Item> itemList = new ArrayList<Item>();
	Member member;
	NonMember nonmember;
	Customer customer;
	
	public String getOrderID() {
		return orderID;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public double getDeliveryCharge() {
		return deliveryCharge;
	}
	public double getFinalPrice() {
		return finalPrice;
	}
	public String getOrderPayMethod() {
		return orderPayMethod;
	}
	public String getOrderPayStatus() {
		return orderPayStatus;
	}
	public String getCustomerName() {
		return customerName;
	}

	public void setOrderID(String newOrderID) {
		orderID = newOrderID;
	}
	public void setTotalPrice(double newTotalPrice) {
		totalPrice = newTotalPrice;
	}
	public void setDeliveryCharge(double newDeliveryCharge) {
		deliveryCharge = newDeliveryCharge;
	}
	public void setFinalPrice(double newFinalPrice) {
		finalPrice = newFinalPrice;
	}
	public void setOrderPayMethod(String newWay) {
		orderPayMethod = newWay;
	}
	public void setOrderPayStatus(String newStatus) {
		orderPayStatus = newStatus;
	}
	
	public Order(String aOrderID, String aCName, double aTotalPrice, double aDeliveryCharge, double aFinalPrice, String aMethod, 
			String aStatus) {
		orderID = aOrderID;
		customerName = aCName;
		totalPrice = aTotalPrice;
		deliveryCharge = aDeliveryCharge;
		finalPrice = aFinalPrice;
		orderPayMethod = aMethod;
		orderPayStatus = aStatus;
	}
	
	public Order() {
		
	}
	
	public void fillItemList() {
		try {
			File iF = new File("Item.txt");
			
			Scanner inFile = new Scanner(iF);
			while(inFile.hasNext()) {
				String str = inFile.nextLine();
				String[] tempList = str.split("#");
				String iID = tempList[0];
				String iName = tempList[1];
				String iType = tempList[2];
				String mPrice = tempList[3];
				String nmPrice = tempList[4];
				String promoStatus = tempList[5];
				double imPrice = Double.parseDouble(mPrice);
				double inmPrice = Double.parseDouble(nmPrice);
				boolean iPromo = Boolean.parseBoolean(promoStatus);
				itemList.add(new Item(iID, iName, iType, imPrice, inmPrice, iPromo));
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Item File error.");
		}
	}
	public void displayItemList() {
		for(int i = 0; i < itemList.size(); i++) {
			Item iAL = itemList.get(i);
			System.out.printf("%-10s %-35s %-10s %-15s %-18s %-14s %n" , iAL.getItemID(), iAL.getItemName(), iAL.getItemType(), 
					"RM "+iAL.getMPrice(), "RM "+iAL.getNMPrice(), iAL.getPromoStatus());
		}
	}
	public void addOrderedItem(String theItemID, int theQuantity) {
		boolean isAdd = true;
		if(theQuantity < 1) {
			isAdd = false;
		}
		for(int j = 0; j < cart.size(); j++) {
			Item cAL = cart.get(j);
			if(theItemID.equals(cAL.getItemID())) {
				cAL.setQuantity(cAL.getQuantity()+theQuantity);
				isAdd = false;
				break;
			}
		}
		if(isAdd) {
			for(int i = 0; i < itemList.size(); i++) {
				Item iAL = itemList.get(i);
				if(theItemID.equals(iAL.getItemID())) {
					cart.add(new Item(iAL.getItemID(), iAL.getItemName(), iAL.getItemType(), iAL.getMPrice(), iAL.getNMPrice(),
							iAL.getPromoStatus(), theQuantity));
				}
			}
		}
	}
	public List<Item> getCart(){
		return cart;
	}
	public void displayCart() {
		System.out.println("-------------");
		System.out.println("Shopping Cart");
		System.out.println("-------------");
		System.out.printf("%-10s %-35s %-8s %n" , "Item ID", "Name", "Quantity");
		System.out.println("-------------------------------------------------------");
		for(int i = 0; i < cart.size(); i++) {
			Item cAL = cart.get(i);
			System.out.printf("%-10s %-35s %-8s %n" , cAL.getItemID(), cAL.getItemName(), cAL.getQuantity());
		}
	}
	public void updateQuantity(String theItemID, int otherQty) {
		if(otherQty < 0) {
			throw new UserException("ERROR: Item quantity cannot be less than 0. Order not success.");
		}
		for(int i = 0; i < cart.size(); i++) {
			Item cAL = cart.get(i);
			if(theItemID.equals(cAL.getItemID())) {
				if(otherQty == 0) {
					cart.remove(i);
				}
				cAL.setQuantity(otherQty);
			}
		}
	}
	public double computeTotalPrice(Customer c) {
		double[] theItemsPrice = c.computeItemsPrice(cart);
		double totalMP = 0.0;
		if(cart.size() == 0) {
			throw new UserException("ERROR: Your cart cannot be empty.");
		}
		for(int i = 0; i < cart.size(); i++) {
			totalMP += theItemsPrice[i];
		}
		return totalMP;
	}
	public double computeFinalPrice(double theTotalPrice, double theDeliveryCharge) {
		double finalP;
		if(theTotalPrice < 25.0) {
			theTotalPrice += 3.0;
		}
		finalP = theTotalPrice + theDeliveryCharge;
		return finalP;
	}
	public String generateOrderID() {
		String idString;
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(10);
		for(int i = 0; i < 10; i++) {
			int index = (int)(alphaNumeric.length() * Math.random()); 
			sb.append(alphaNumeric.charAt(index)); 
		}
		idString = sb.toString();
		return idString;
	}
	public void fillOrderList() {
		try {
			File oF = new File("Orders.txt");
			
			Scanner inFile = new Scanner(oF);
			while(inFile.hasNext()) {
				String str = inFile.nextLine();
				String[] tempList = str.split("#");
				String oID = tempList[0];
				String oName = tempList[1];
				String TtlPrice = tempList[2];
				String Delivery = tempList[3];
				String FinPrice = tempList[4];
				String oPayMethod = tempList[5];
				String oPayStatus = tempList[6];
				double oTtlPrice = Double.parseDouble(TtlPrice);
				double oDelivery = Double.parseDouble(Delivery);
				double oFinPrice = Double.parseDouble(FinPrice);
				orders.add(new Order(oID, oName, oTtlPrice, oDelivery, oFinPrice, oPayMethod, oPayStatus));
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Orders File error.");
		}
	}
	public void saveOrderList() {
		try {
			File oF = new File("Orders.txt");
			
			FileWriter fw = new FileWriter(oF, false);
			PrintWriter outFile = new PrintWriter(fw);
			
			for(int i = 0; i < orders.size(); i++) {
				Order oAL = orders.get(i);
				String tp = Double.toString(oAL.getTotalPrice());
				String dc = Double.toString(oAL.getDeliveryCharge());
				String fp = Double.toString(oAL.getFinalPrice());
				outFile.println(oAL.getOrderID()+"#"+oAL.getCustomerName()+"#"+tp+"#"+dc+"#"+fp+"#"+oAL.getOrderPayMethod()+"#"+
						oAL.getOrderPayStatus());
			}
			outFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Orders File error.");
		}
	}
	public List<Order> getOrder(){
		return orders;
	}
	public void addOrder(Order order) {
		fillOrderList();
		orders.add(order);
	}
	public void displayReceipt(String aOrderId) {
		String filename = aOrderId + ".txt";
		String a ="Receipt\\" + filename;
		try {
			File file = new File(a);
			
			Scanner inFile = new Scanner(file);
			while(inFile.hasNext()) {
				String str = inFile.nextLine();
				System.out.println(str);
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Receipt File error.");
		}
	}
}

