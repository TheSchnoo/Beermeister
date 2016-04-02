package web;

import java.util.ArrayList;

public class BeerInfo {

    String bname;
    String breweryName;
    String type;
    double abv;
    double ibu;
    double averageRating;
    String description;
    boolean brewed;
    ArrayList<Vendor> vendors;
    boolean stocked = false;

    public BeerInfo(String bname, String breweryName, String type, double abv, double ibu, String description,
                    double averageRating, boolean brewed) {
        this.bname = bname;
        this.breweryName = breweryName;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = 0;
        this.description = description;
        this.brewed = brewed;
        this.vendors = new ArrayList<Vendor>();
        this.averageRating = averageRating;
    }

    public BeerInfo(String bname, String breweryName, String type, double abv, double ibu, String description,
                    double averageRating, boolean brewed, boolean stocked) {
        this.bname = bname;
        this.breweryName = breweryName;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = 0;
        this.description = description;
        this.brewed = brewed;
        this.vendors = new ArrayList<Vendor>();
        this.stocked = stocked;
        this.averageRating = averageRating;
    }

    public BeerInfo(String bname, String breweryName, String type, double abv, double ibu, String description,
                    double averageRating, boolean brewed, ArrayList<Vendor> vendors) {
        this.bname = bname;
        this.breweryName = breweryName;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = 0;
        this.description = description;
        this.brewed = brewed;
        this.vendors = vendors;
        this.averageRating = averageRating;
    }

    public String getBName() { return bname; }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBreweryName() { return breweryName; }

    //I think we decided you can't change the brewery of a beer?

    public String getType() { return  type; }

    public void setType(String type) {
        this.type = type;
    }

    public double getAbv() { return abv; }

    public void setAbv(double abv)  {
        this.abv = abv;
    }

    public double getIbu() { return ibu; }

    public void setIbu(double ibu) {
        this.ibu = ibu;
    }

    public double getAverageRating() { return averageRating; }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBrewed() { return brewed; }

    public void addVendor(Vendor vendor){
        vendors.add(vendor);
    }

    public boolean getStocked(){
        return stocked;
    }

    public ArrayList<Vendor> getVendors(){
        return vendors;
    }

    public String toTupleValueString() {
        return "('" + this.bname + "', '" + this.type + "', " + this.ibu + ", " + this.abv + ", '" +
                this.description + "', '" + this.breweryName + "', " + this.brewed + ", " + this.averageRating + ")";
    }


}
