package domain.model;

class Farmland {
    private Vegetable vegetable;
    private double growth;
    private int water;
    private double quality;

    public Farmland() {
        this.vegetable = null;
        this.growth = 0;
        this.water = 0;
    }

    public Farmland(Vegetable vegetable, double growth, int water, double quality) {
        this.vegetable = vegetable;
        this.growth = growth;
        this.water = water;
        this.quality = quality;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public Vegetable getVegetable() {
        return vegetable;
    }

    public void setVegetable(Vegetable vegetable) {
        this.vegetable = vegetable;
    }

    public double getGrowth() {
        return growth;
    }

    public void setGrowth(double growth) {
        this.growth = growth;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }
}
