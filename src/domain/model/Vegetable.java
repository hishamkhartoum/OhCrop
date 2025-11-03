package domain.model;

import java.util.List;

class Vegetable {
    String name;
    int cost;
    int growingTime;
    int sellingPrice;
    List<String> preferredSeasons;

    public Vegetable() {
    }

    public Vegetable(String name, int cost, int growingTime, int sellingPrice, List<String> preferredSeasons) {
        this.name = name;
        this.cost = cost;
        this.growingTime = growingTime;
        this.sellingPrice = sellingPrice;
        this.preferredSeasons = preferredSeasons;
    }

    public List<String> getPreferredSeasons() {
        return preferredSeasons;
    }



    public void setPreferredSeasons(List<String> preferredSeasons) {
        this.preferredSeasons = preferredSeasons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getGrowingTime() {
        return growingTime;
    }

    public void setGrowingTime(int growingTime) {
        this.growingTime = growingTime;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
