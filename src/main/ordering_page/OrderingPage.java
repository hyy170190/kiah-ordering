package ordering_page;

import java.io.*;
import java.util.*;

public class OrderingPage {
	Scanner input = new Scanner(System.in);
	
	public void startMenu() {
		String name, phone, address, district, postalCode, state, userName, password, orderid;
		int option;
		boolean isPass; 
	
		System.out.println("---------------------------------");
		System.out.println("Welcome to Mrs Kiah Pastry & Cake");
		System.out.println("---------------------------------");
		System.out.println("1. Member login.");
		System.out.println("2. Register as member.");
		System.out.println("3. Buying as guest.");
		System.out.println("4. Pay for pending order payment.");
		System.out.println("5. Exit.");
		System.out.print("Enter a number(1-5) to select: ");
		
		try {
			option = input.nextInt();
			input.nextLine();
			
			if(option == 1) {
				
				System.out.print("Enter your username: ");
				userName = input.nextLine().trim();
				System.out.print("Enter your password: ");
				password = input.nextLine().trim();
				
				Member memberFake = new Member();
				String[] info = memberFake.login(userName, password);
				Member memberLog = new Member(info[0], info[1], info[2], info[3], info[4], info[5], userName, password);
				
				orderMenu(memberLog);
			}
			else if(option == 2) {
				System.out.print("Enter name: ");
				name = input.nextLine().trim();
				System.out.print("Enter phone no. : ");
				phone = input.nextLine().trim();
				System.out.print("Enter address: ");
				address = input.nextLine().trim();
				System.out.print("Enter district: ");
				district = input.nextLine().trim();
				System.out.print("Enter postal code: ");
				postalCode = input.nextLine().trim();
				System.out.print("Enter state: ");
				state = input.nextLine().trim();
				System.out.print("Create a username: ");
				userName = input.nextLine().trim();
				System.out.print("Create a password: ");
				password = input.nextLine().trim();
				
				Member memberReg = new Member(name, phone, address, district, postalCode, state, userName, password);
				isPass = memberReg.register(userName, name, phone, address, district);
				memberReg.saveMemberList();
				
				if(isPass) {
					orderMenu(memberReg);
				}
			}
			else if(option == 3) {
				System.out.print("Enter name: ");
				name = input.nextLine().trim();
				System.out.print("Enter phone no. : ");
				phone = input.nextLine().trim();
				System.out.print("Enter address: ");
				address = input.nextLine().trim();
				System.out.print("Enter district: ");
				district = input.nextLine().trim();
				System.out.print("Enter postal code: ");
				postalCode = input.nextLine().trim();
				System.out.print("Enter state: ");
				state = input.nextLine().trim();
				
				NonMember nonmember = new NonMember(name, phone, address, district, postalCode, state);
				isPass = nonmember.verifyGuest();	
				
				if(isPass) {
					orderMenu(nonmember);
				}
			}
			else if(option == 4){
				Payment payPending = new Payment();
				System.out.print("Enter your pending order ID: ");
				orderid = input.nextLine().trim();
				String[] payInfo = payMenu();
				payPending.processPendingPayment(orderid, payInfo[0], payInfo[1]);
			}
			else if(option == 5) {
				System.out.println("MESSAGE: Exit successfully.");
			}
			else {
				System.out.println("MESSAGE: Invalid choice.");
			}
			input.close();
		}
		catch(InputMismatchException imex) {
			System.out.println("ERROR: Invalid input. Order fails.");
		}
		catch(UserException uex) {
			System.out.println(uex.getMessage());
		}
		catch(FileException fex) {
			System.out.println(fex.getMessage());
		}
	}
	
	public void orderMenu(Customer c) {
		String optionItem;
		int quantityItem;
		String shopMore = "y";
		String update = "y";
		double theTotalPrice, theDeliveryCharge, theFinalPrice;
		String theOrderID;
		double[] theItemsPrice;
		
		Order orderFake = new Order();
		
		System.out.println("----------");
		System.out.println("Order Page");
		System.out.println("----------");
		System.out.printf("%-10s %-35s %-10s %-15s %-18s %-14s %n" , "Item ID", "Name", "Type", "Member Price", "Non Member Price",
				"Promotion");
		System.out.println("------------------------------------------------------------------------------------------------------");
		orderFake.fillItemList();
		orderFake.displayItemList();
		
		do {
			System.out.println("WARNING: Invalid format of input will cause application to exit.");
			System.out.print("Enter item ID to select a cake or pastry: ");
			optionItem = input.nextLine().toUpperCase();
			System.out.print("Enter quantity you want: ");
			quantityItem = input.nextInt();
			input.nextLine();
			
			orderFake.addOrderedItem(optionItem, quantityItem);
			orderFake.displayCart();
			
			System.out.print("Add more cake or pastry? (Enter y to add items / n to continue to next step): ");
			shopMore = input.nextLine().trim().toLowerCase();
		}while(shopMore.equals("y"));
		
		System.out.print("Update quantity of items? (Enter y to update / n to check out): ");
		update = input.nextLine().trim().toLowerCase();
		if(update.equals("y")) {
			do {
				System.out.print("Enter item ID to be updated: ");
				optionItem = input.nextLine().toUpperCase();
				System.out.print("Enter new quantity you want (enter 0 to remove item in cart) : ");
				quantityItem = input.nextInt();
				input.nextLine();
				
				orderFake.updateQuantity(optionItem, quantityItem);
				orderFake.displayCart();
				
				System.out.print("Update quantity of items? (Enter y to update / n to check out): ");
				update = input.nextLine().trim().toLowerCase();
			}while(update.equals("y"));
		}
		List<Item> customerCart = orderFake.getCart();
		theItemsPrice = c.computeItemsPrice(customerCart);
		theTotalPrice = orderFake.computeTotalPrice(c);
		theDeliveryCharge = c.findDeliveryCharge(c.getDistrict());
		theFinalPrice = orderFake.computeFinalPrice(theTotalPrice, theDeliveryCharge);
		theOrderID = orderFake.generateOrderID();
		Order orderReal = new Order(theOrderID, c.getName(), theTotalPrice, theDeliveryCharge, theFinalPrice, "null", "Pending For Payment");
		
		try {
			File dir = new File("Receipt");
			dir.mkdir();
			String hash = orderReal.getOrderID();
			String filename = hash + ".txt";
			File file = new File(dir, filename);
			PrintWriter outFile = new PrintWriter(file);
			outFile.println("-------");
			outFile.println("Receipt");
			outFile.println("-------");
			outFile.println("Order ID: " + orderReal.getOrderID());
			outFile.println("Name: " + c.getName());
			outFile.println("Phone no. : " + c.getPhone());
			outFile.println("Address: " + c.getAddress() + ", " + c.getDistrict() + ", " + c.getPostalCode() + ", " + c.getState());
			if(c instanceof Member) {
				outFile.printf("%-10s %-35s %-10s %-15s %-11s %-16s %-14s %n" , "Item ID", "Name", "Type", "Member Price", "Quantity", 
						"Items Price", "Promotion");
				outFile.println("----------------------------------------------------------------------------------------------------------------");
				for(int i = 0; i < customerCart.size(); i++) {
					Item ccL = customerCart.get(i);
					outFile.printf("%-10s %-35s %-10s %-15s %-11s %-16s %-14s %n" , ccL.getItemID(), ccL.getItemName(), ccL.getItemType(), 
							"RM "+ ccL.getMPrice(), ccL.getQuantity(), "RM "+ theItemsPrice[i], ccL.getPromoStatus());
				}
			}
			else {
				outFile.printf("%-10s %-35s %-10s %-18s %-11s %-16s %-14s %n" , "Item ID", "Name", "Type", "NonMember Price", "Quantity", 
						"Items Price", "Promotion");
				outFile.println("------------------------------------------------------------------------------------------------------------------");
				for(int i = 0; i < customerCart.size(); i++) {
					Item ccL = customerCart.get(i);
					outFile.printf("%-10s %-35s %-10s %-18s %-11s %-16s %-14s %n" , ccL.getItemID(), ccL.getItemName(), ccL.getItemType(), 
							"RM "+ ccL.getNMPrice(), ccL.getQuantity(), "RM "+ theItemsPrice[i], ccL.getPromoStatus());
				}
			}
			outFile.printf("%-17s %-3s %-10s %n", "Sub-total: ", "RM", orderReal.getTotalPrice());
			outFile.printf("%-17s %-3s %-10s %n", "Delivery charge: ", "RM", orderReal.getDeliveryCharge());
			if(theTotalPrice < 25.0) {
				outFile.printf("%-17s %-3s %-10s %n", "Extra charge: ", "RM", "3.0");
			}
			outFile.printf("%-17s %-3s %-10s %n", "Total: ", "RM", orderReal.getFinalPrice());
			outFile.close();
		}
		catch(IOException ioe) {
			System.out.println("File error.");
		}
		orderFake.displayReceipt(orderReal.getOrderID());
		Payment pay = new Payment(orderReal);
		String[] payInfo = payMenu();
		pay.processPayment(payInfo[0], payInfo[1]);
		orderFake.addOrder(orderReal);
		orderFake.saveOrderList();
	}
	
	public String[] payMenu() {
		int option2;
		String payConfirm, payWay, orderStatus;
		String[] payInfo = new String[2];
		
		System.out.println("------------");
		System.out.println("Payment Page");
		System.out.println("------------");
		System.out.println("Payment Method: ");
		System.out.println("1. Credit / Debit Card");
		System.out.println("2. Online Banking");
		System.out.print("Enter number to choose: ");
		option2 = input.nextInt();
		input.nextLine();
		System.out.print("Do you want to pay now? (Enter y for yes / n for no): ");
		payConfirm = input.nextLine().toLowerCase();
		
		if(option2 == 1 && payConfirm.equals("y")) {
			payWay = "Credit / Debit Card";
			orderStatus = "Paid and Ready for Delivery";
			payInfo[0] = payWay;
			payInfo[1] = orderStatus;
			System.out.println("MESSAGE: Payment is successful. Your order will be delivered soon.");
		}
		else if(option2 == 2 && payConfirm.equals("y")) {
			payWay = "Online Banking";
			orderStatus = "Paid and Ready for Delivery";
			payInfo[0] = payWay;
			payInfo[1] = orderStatus;
			System.out.println("MESSAGE: Payment is successful. Your order will be delivered soon.");
		}
		else {
			payWay = "null";
			orderStatus = "Pending For Payment";
			payInfo[0] = payWay;
			payInfo[1] = orderStatus;
			System.out.println("MESSAGE: Payment is not successful. Please pay again later.");
		}
		return payInfo;
	}
	
	public static void main(String[] args) {
		OrderingPage op = new OrderingPage();
		op.startMenu();
	}
}

