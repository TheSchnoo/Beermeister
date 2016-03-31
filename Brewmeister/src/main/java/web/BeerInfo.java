package web;

public class BeerInfo {

    String bname;
    String breweryName;
    String type;
    double abv;
    double ibu;
    String averageRating;
    String description;
    boolean brewed;

    public BeerInfo(String name, String brewery, String type, double abv, double ibu, String description, boolean brewed) {
        this.bname = name;
        this.breweryName = brewery;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = null;
        this.description = description;
        this.brewed = brewed;
    }

    public String getName() { return bname; }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBrewery() { return breweryName; }

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

    public String getAverageRating() { return averageRating; }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBrewed() { return brewed; }

    public String toTupleValueString() {
        return "(" + this.bname + ", " + this.type + ", " + this.ibu + ", " + this.abv + ", " + this.description + ", " +
                this.breweryName + ", " + this.averageRating + ")";
    }


}
