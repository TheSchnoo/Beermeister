package web;

public class BeerInfo {

    String name;
    String brewery;
    String type;
    double abv;
    double ibu;
    String averageRating;
    String description;
    boolean brewed;

    public BeerInfo(String name, String brewery, String type, double abv, double ibu, String description, boolean brewed) {
        this.name = name;
        this.brewery = brewery;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = null;
        this.description = description;
        this.brewed = brewed;
    }

    public String getName() { return name; }

    public String getBrewery() { return brewery; }

    public String getType() { return  type; }

    public double getAbv() { return abv; }

    public double getIbu() { return ibu; }

    public String getAverageRating() { return averageRating; }

    public String getDescription() { return description; }

    public boolean isBrewed() { return brewed; }

    public String toTupleValueString() {
        return "(" + this.name + ", " + this.type + ", " + this.ibu + ", " + this.abv + ", " + this.description + ", " +
                this.brewery + ", " + this.averageRating + ")";
    }

}
