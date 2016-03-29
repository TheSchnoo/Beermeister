package web;

public class BeerInfo {

    String name;
    String brewery;
    String type;
    float abv;
    float ibu;
    String averageRating;

    public BeerInfo(String name, String brewery, String type, float abv, float ibu) {
        this.name = name;
        this.brewery = brewery;
        this.type = type;
        this.abv = abv;
        this.ibu = ibu;
        this.averageRating = null;
    }

    public String getName() { return name; }

    public String getBrewery() { return brewery; }

    public String getType() { return  type; }

    public float getAbv() { return abv; }

    public float getIbu() { return ibu; }

    public String getAverageRating() { return averageRating; }
}
