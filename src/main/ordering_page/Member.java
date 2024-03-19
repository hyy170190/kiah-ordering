package ordering_page;

import java.util.*;
import java.io.*;

public class Member extends Customer{
	private String userName, userPassword;
	ArrayList<Member> memberList = new ArrayList<Member>();
	
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return userPassword;
	}
	
	public void setUserName(String aUserName) {
		if(!aUserName.matches("^[a-zA-Z0-9]{2,10}$")) {
			throw new UserException("ERROR: Your username should contains 2 - 10 letters or numbers only.");
		}
		else {
			userName = aUserName;
		}
	}
	public void setPassword(String aPassword) {
		if(!aPassword.matches("^[a-zA-Z0-9]{2,10}$")) {
			throw new UserException("ERROR: Your password should contains 3 - 10 letters or numbers only.");
		}
		else {
			userPassword = aPassword;
		}
	}
	
	public Member(String aName, String aPhone, String aAddress, String aDistrict, String aPostalCode, String aState, String aUserName,
			String aPassword) {
		super(aName, aPhone, aAddress, aDistrict, aPostalCode, aState);
		setUserName(aUserName);
		setPassword(aPassword);
	}
	public Member() {
		
	}
	
	public void fillMemberList() {
		try {
			File mF = new File("Member.txt");
			
			Scanner inFile = new Scanner(mF);
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
				memberList.add(new Member(name, phone, address, district, postalcode, state, username, password));
			}
			inFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Member File error.");
		}
	}
	public void saveMemberList() {
		try {
			File mF = new File("Member.txt");
			
			FileWriter fw = new FileWriter(mF, false);
			PrintWriter outFile = new PrintWriter(fw);
			
			for(int i = 0; i < memberList.size(); i++) {
				Member mAL = memberList.get(i);
				outFile.println(mAL.getName()+"#"+mAL.getPhone()+"#"+mAL.getAddress()+"#"+mAL.getDistrict()+"#"+
						mAL.getPostalCode()+"#"+mAL.getState()+"#"+mAL.getUserName()+"#"+mAL.getPassword());
			}
			outFile.close();
		}
		catch(IOException ioe) {
			throw new FileException("ERROR: Member File error.");
		}
	}
	public boolean register(String theUserName, String theName, String thePhone, String theAddress, String theDistrict) {
		boolean pass = true;
		
		fillMemberList();
		for(int i = 0; i < memberList.size(); i++) {
			Member mAL = memberList.get(i);
			if(mAL.getUserName().equals(theUserName)) {
				System.out.println("MESSAGE: The username you entered had been used. Please choose another username.");
				pass = false;
				break;
			}
			if(mAL.getName().equals(theName) && mAL.getPhone().equals(thePhone) && mAL.getAddress().equals(theAddress) 
					&& mAL.getDistrict().equals(theDistrict)) {
				System.out.println("MESSAGE: Your already registered before. Please login to order.");
				pass = false;
				break;
			}
		}
		if(pass) {
			memberList.add(new Member(getName(), getPhone(), getAddress(), getDistrict(), getPostalCode(), getState(), getUserName(),
					getPassword()));
			System.out.println("MESSAGE: Register successfully. You can proceed to order.");
		}
		return pass;
	}
	public String[] login(String theUserName, String thePassword) {
		boolean memberNotExist = true;
		String[] info = new String[6];
		
		fillMemberList();
		for(int i = 0; i < memberList.size(); i++) {
			Member mAL = memberList.get(i);
			if(mAL.getUserName().equals(theUserName) && mAL.getPassword().equals(thePassword)) {
				info[0] = mAL.getName();
				info[1] = mAL.getPhone();
				info[2] = mAL.getAddress();
				info[3] = mAL.getDistrict();
				info[4] = mAL.getPostalCode();
				info[5] = mAL.getState();
				System.out.println("MESSAGE: Login successfully. You can proceed to order.");
				memberNotExist = false;
				break;
			}
		}
		if(memberNotExist) {
			throw new UserException("MESSAGE: Your username and pasword is incorrect. Please try again.");
		}
		return info;
	}
	public double[] computeItemsPrice(List<Item> cart) {
		double[] itemsPrice = new double[cart.size()];
		boolean isPromo;
		double mPrice;
		for(int i = 0; i < cart.size(); i++) {
			Item cAL = cart.get(i);
			isPromo = cAL.getPromoStatus();
			if(isPromo) {
				mPrice = cAL.getMPrice() * 0.95;
			}
			else {
				mPrice = cAL.getMPrice();
			}
			itemsPrice[i] = mPrice * cAL.getQuantity();
		}
		return itemsPrice;
	}
}

