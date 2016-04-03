package web;

public class Vendor {
    int storeID;
    String storeName;
    String address; // Just street address: 123 This St.  We can add city, etc. as we get big time.

    public Vendor(int storeID, String storeName, String address){ //String address){
        this.storeID = storeID;
        this.storeName = storeName;
        this.address = address;
    }

    public int getStoreID(){
        return storeID;
    }

    public String getStoreName(){
        return storeName;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String newAddress){
        this.address = newAddress;
    }

}
