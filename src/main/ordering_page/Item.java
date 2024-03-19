package ordering_page;

public class Item {
	private String itemID, itemName, itemType;
	private double memberPrice, nonMemberPrice, itemsPrice;
	private int quantity;
	
	private boolean promoStatus;
	
	public String getItemID() {
		return itemID;
	}
	public String getItemName() {
		return itemName;
	}
	public String getItemType() {
		return itemType;
	}
	public double getMPrice() {
		return memberPrice;
	}
	public double getNMPrice() {
		return nonMemberPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public double getItemsPrice() {
		return itemsPrice;
	}
	public boolean getPromoStatus() {
		return promoStatus;
	}
	
	public void setProductID(String newItemID) {
		itemID = newItemID;
	}
	public void setItemName(String newItemName) {
		itemName = newItemName;
	}
	public void setItemType(String newItemType) {
		itemName = newItemType;
	}
	public void setMPrice(double newMPrice) {
		memberPrice = newMPrice;
	}
	public void setNMPrice(double newNMPrice) {
		nonMemberPrice = newNMPrice;
	}
	public void setQuantity(int newQuantity) {
		quantity = newQuantity;
	}
	public void setItemsPrice(double newItemsPrice) {
		itemsPrice = newItemsPrice;
	}
	public void setPromoStatus(boolean newPromoStatus) {
		promoStatus = newPromoStatus;
	}
	
	public Item(String anItemID, String anItemName, String anItemType, double anMPrice, double anNMPrice, boolean anPromoStatus) {
		itemID = anItemID;
		itemName = anItemName;
		itemType = anItemType;
		memberPrice = anMPrice;
		nonMemberPrice = anNMPrice;
		promoStatus = anPromoStatus;
	}
	public Item(String aItemID, String aItemName, String aItemType, double aMPrice, double aNMPrice, boolean aPromoStatus, int aQuantity) {
		itemID = aItemID;
		itemName = aItemName;
		itemType = aItemType;
		memberPrice = aMPrice;
		nonMemberPrice = aNMPrice;
		promoStatus = aPromoStatus;
		quantity = aQuantity;
	}
}

