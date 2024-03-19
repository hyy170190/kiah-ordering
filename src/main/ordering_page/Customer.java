package ordering_page;

import java.io.*;
import java.util.*;

public abstract class Customer {
	private String name;
	private String phone;
	private String address;
	private String district;
	private String postalCode;
	private String state;
	
	//getters
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddress() {
		return address;
	}
	public String getDistrict() {
		return district;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public String getState() {
		return state;
	}
	
	//setters
	public void setName(String aName) {
		if(!aName.matches("^[a-zA-Z]+((['.-/][a-zA-Z])?[ a-zA-Z]*)*$")) {
			throw new UserException("ERROR: Name should contain alphabets and symbols such as ['], [/], [.], [-] only. The symbol should not be the first or last character of a word.");
		}
		else {
			name=aName;
		}
	}
	public void setPhone(String aPhone) {
		if(!aPhone.matches("^[0-9]{9,11}$")) {
			throw new UserException("ERROR: Phone number should contains 9 - 11 numbers only.");
		}
		else {
			phone=aPhone;
		}	
	}
	public void setAddress(String newAddress) {
		address=newAddress;
	}
	public void setDistrict(String aDistrict) {
		String validDistrict = aDistrict.toLowerCase();
		try {
			boolean invalid = true;
			File dF = new File("Delivery.txt");
			
			Scanner inFile = new Scanner(dF);
			
			while(inFile.hasNext()) {
				String str = inFile.nextLine();
				String[] tempList = str.split("#");
				String district = tempList[0];
				String deliveryCharge = tempList[1];
				
				if(validDistrict.equals(district)) {
					invalid = false;
					break;
				}
			}
			inFile.close();
			
			if(invalid) {
				throw new UserException("MESSAGE: Sorry. Your district is not in our service area.");
			}
			else {
				district=aDistrict;
			}
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Delivery File error.");
		}
	}
	public void setPostalCode(String aPostalCode) {
		if(!aPostalCode.matches("^7[0-9]{4}$")) {
			throw new UserException("ERROR: Melaka postal code should contains 5 digits only starting with 7.");
		}
		else {
			postalCode=aPostalCode;
		}
	}
	public void setState(String aState) {
		String validState = aState.toLowerCase();
		if(!validState.equals("melaka")) {
			throw new UserException("MESSAGE: Sorry. Service is provided for Melaka area only.");
		}
		else {
			state=aState;
		}
	}
	
	//constructor
	public Customer(String aName,String aPhone,String aAddress,String aDistrict,String aPostalCode,String aState){
		setState(aState);
		setDistrict(aDistrict);
		setName(aName); 
		setPhone(aPhone);
		setPostalCode(aPostalCode);
		address=aAddress;
	}
	public Customer() {
		
	}
	
	public double findDeliveryCharge(String customerDistrict) {
		double aDeliveryCharge = 0.0;
		try {
			File dF = new File("Delivery.txt");
			Scanner inFile = new Scanner(dF);
			while(inFile.hasNext()) {
				String str = inFile.nextLine();
				String[] tempList = str.split("#");
				String district = tempList[0];
				String deliveryCharge = tempList[1];
				
				if(customerDistrict.toLowerCase().equals(district)) {
					aDeliveryCharge = Double.parseDouble(deliveryCharge);
					break;
				}
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Delivery File error.");
		}
		return aDeliveryCharge;
	}
	
	public abstract double[] computeItemsPrice(List<Item> cart);
}
