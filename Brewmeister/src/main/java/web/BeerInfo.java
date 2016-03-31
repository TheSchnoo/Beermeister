package web;

public class BeerInfo {

    String bname;
    String breweryName;
    String type;
    float abv;
    float ibu;
    float averageRating;
    String description;

    public BeerInfo(String bname, String breweryName, String type, float abv, float ibu, String description) {
        this.bname = bname;
        this.breweryName = breweryName;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = 0;
        this.description = description;
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

    public float getAbv() { return abv; }

    public void setAbv(float abv)  {
        this.abv = abv;
    }

    public float getIbu() { return ibu; }

    public void setIbu(float ibu) {
        this.ibu = ibu;
    }

    public float getAverageRating() { return averageRating; }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }
}
