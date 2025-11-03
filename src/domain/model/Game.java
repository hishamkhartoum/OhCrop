package domain.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private Map<String, Vegetable> vegetables = new LinkedHashMap<>();
    private Farmland farmland = new Farmland();
    private int gold = 60;
    private boolean gameOver = false;
    private int week = 0;

    private final String[] seasons = {"SPRING", "SUMMER", "FALL", "WINTER"};
    private final String[] weathers = {"CLOUDY", "SHINY", "RAINY"};
    private String weather;
    private int weatherCounter = 0;

    private String getCurrentSeason() {
        return seasons[(week / 12) % 4];
    }

    private int getWeeksRemainingInSeason() {
        return 12 - (week % 12);
    }

    public Game() {}

    public void setupVegetables() {
        vegetables.put("lettuce", new Vegetable("Lettuce", 15, 3, 45,
                Arrays.asList("SPRING", "SUMMER", "FALL", "WINTER")));
        vegetables.put("carrot", new Vegetable("Carrot", 35, 5, 110,
                Arrays.asList("SPRING", "SUMMER", "WINTER")));
        vegetables.put("cabbage", new Vegetable("Cabbage", 100, 7, 220,
                Arrays.asList("SPRING", "SUMMER")));
        vegetables.put("tomato", new Vegetable("Tomato", 155, 8, 355,
                Arrays.asList("SPRING")));
    }

    public void start() {
        setupVegetables();
        updateWeather();
        printState();
        gameLoop();
    }

    private void gameLoop() {
        Scanner input = new Scanner(System.in);

        while (!gameOver) {
            System.out.print("What's your next command? ");
            String command = input.nextLine().trim().toLowerCase();

            if (command.equals("vegetables") || command.equals("v")) {
                showVegetables();
            } else if (command.startsWith("sow")) {
                sowVegetable(command);
            } else if (command.equals("wait") || command.isEmpty()) {
                waitWeek();
            } else if (command.equals("water") || command.equals("w")) {
                waterFarmland();
            } else {
                System.out.println("Unknown command.");
            }

            checkWinLose();
            printState();
        }

        input.close();
    }

    private void updateWeather() {
        if (weatherCounter % 3 == 0 || weather == null) {
            int index = (int)(Math.random() * weathers.length);
            weather = weathers[index];
            System.out.println("> The weather is " + weather + " now.");
        }
        weatherCounter++;
    }

    private void showVegetables() {
        System.out.println("Available vegetables are:");
        for (Vegetable v : vegetables.values()) {
            System.out.printf("%s (Cost: %dG, Growing time: %d weeks, Selling price: %dG)%n",
                    v.getName(), v.getCost(), v.getGrowingTime(), v.getSellingPrice());
        }
    }

    private void sowVegetable(String command) {
        if (farmland.getVegetable() != null) {
            System.out.println("Farmland is already occupied!");
            return;
        }

        String[] parts = command.split(" ");
        if (parts.length < 2) {
            System.out.println("Please specify a vegetable to sow.");
            return;
        }

        String name = parts[1];
        Vegetable v = vegetables.get(name);

        if (v == null) {
            System.out.println("Unknown vegetable!");
            return;
        }

        if (gold < v.getCost()) {
            System.out.println("Not enough gold to plant " + v.getName() + "!");
            return;
        }

        gold -= v.getCost();
        farmland.setVegetable(v);
        farmland.setGrowth(0);
        farmland.setQuality(1.0);
        System.out.println(v.getName() + " has been planted!");
    }

    private void waitWeek() {
        gold -= 1;
        week++;
        updateWeather();
        String currentSeason = getCurrentSeason();

        if (farmland.getVegetable() != null) {
            boolean hasWater = farmland.getWater() > 0;

            double growthMultiplier = 1.0;
            int waterConsumed = 1;
            if (weather.equals("SHINY")) {
                growthMultiplier = 1.5;
                waterConsumed = 2;
            } else if (weather.equals("RAINY")) {
                growthMultiplier = 0.5;
                waterConsumed = 0;
                farmland.setWater(farmland.getWater() + 2);
            }

            if (!hasWater && !weather.equals("RAINY")) {
                System.out.println(farmland.getVegetable().getName() + " cannot grow, no water!");
            } else {
                if (farmland.getVegetable().getPreferredSeasons().contains(currentSeason)) {
                    farmland.setGrowth(farmland.getGrowth() + growthMultiplier);
                } else {
                    farmland.setQuality(farmland.getQuality() - 0.15);
                    if (farmland.getQuality() <= 0) {
                        System.out.println(farmland.getVegetable().getName() + " died due to poor quality!");
                        farmland.setVegetable(null);
                        farmland.setGrowth(0);
                        farmland.setQuality(1.0);
                        return;
                    }
                }

                farmland.setWater(farmland.getWater() - waterConsumed);
            }

            if (farmland.getVegetable() != null && farmland.getGrowth() >= farmland.getVegetable().getGrowingTime()) {
                Vegetable v = farmland.getVegetable();
                int profit = (int)(v.getSellingPrice() * farmland.getQuality());
                gold += profit;
                System.out.println(v.getName() + " has been sold for " + profit + "G.");
                farmland.setVegetable(null);
                farmland.setGrowth(0);
                farmland.setQuality(1.0);
            }
        }
    }

    private void waterFarmland() {
        if (gold < 2) {
            System.out.println("Not enough gold to water!");
            return;
        }

        gold -= 2;
        farmland.setWater(farmland.getWater() + 7);
        System.out.println("Farmland watered! Water level is now " + farmland.getWater());
    }

    private void checkWinLose() {
        if (gold >= 1000) {
            System.out.println("You won! Your farm is thriving!");
            gameOver = true;
        } else if (gold <= -15) {
            System.out.println("You lost! Your farm went bankrupt.");
            gameOver = true;
        }
    }

    private void printState() {
        System.out.println("##########");
        String season = getCurrentSeason();
        int remaining = getWeeksRemainingInSeason();
        System.out.println("Season: " + season + " (remaining time: " + remaining + " weeks)");
        System.out.println("Weather: " + weather);
        System.out.println("Gold: " + gold);
        if (farmland.getVegetable() == null) {
            System.out.println("Farmland: (available)");
        } else {
            System.out.printf("Farmland: %s (Growth: %.1f / %d weeks, Water: %d, Quality: %.0f%%)%n",
                    farmland.getVegetable().getName(),
                    farmland.getGrowth(),
                    farmland.getVegetable().getGrowingTime(),
                    farmland.getWater(),
                    farmland.getQuality() * 100);
        }
        System.out.println("##########");
    }
}
