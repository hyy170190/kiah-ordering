package ordering_page;

import java.io.*;
import java.util.*;

public class NonMember extends Customer {
	public NonMember(String aName, String aPhone, String aAddress, String aDistrict, String aPostalCode, String aState) {
		super(aName, aPhone, aAddress, aDistrict, aPostalCode, aState);
	}
	public NonMember() {
		
	}
	
	public boolean verifyGuest() {
		boolean pass = true;
		try {
			File mF = new File("Member.txt");
			
			Scanner inFile = new Scanner(mF);
			boolean notMember = true;
			
			while(inFile.hasNext()) {
				String str = inFile.nextLine();
				String[] tempList = str.split("#");
				String name = tempList[0];
				String phone = tempList[1];
				String address = tempList[2];
				String district = tempList[3];
				String postalcode = tempList[4];
				String state = tempList[5];
				String username = tempList[6];
				String password = tempList[7];
				
				if(name.equals(getName()) && phone.equals(getPhone())) {
					System.out.println("MESSAGE: You already register before. Please login to order.");
					notMember = false;
					pass = false;
					break;
				}
			}
			if(notMember) {
				System.out.println("MESSAGE: Hello new friend. You can proceed to order as a guest.");
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Member File error.");
		}
		return pass;
	}
	public double[] computeItemsPrice(List<Item> cart) {
		double[] itemsPrice = new double[cart.size()];
		boolean isPromo;
		double nmPrice;
		for(int i = 0; i < cart.size(); i++) {
			Item cAL = cart.get(i);
			isPromo = cAL.getPromoStatus();
			if(isPromo) {
				nmPrice = cAL.getNMPrice() * 0.95;
			}
			else {
				nmPrice = cAL.getNMPrice();
			}
			itemsPrice[i] = nmPrice * cAL.getQuantity();
		}
		return itemsPrice;
	}
}
