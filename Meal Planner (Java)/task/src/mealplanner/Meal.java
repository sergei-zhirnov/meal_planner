package mealplanner;

import java.util.ArrayList;

public class Meal {
    String name;
    MealCategory category;
    ArrayList<String> ingredients;
    int id;

    Meal(String name, MealCategory category, ArrayList<String> ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;

    }

    Meal(int id, String name, MealCategory category, ArrayList<String> ingredients) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;

    }

    public void printMeal() {
        System.out.println();
        System.out.printf("Name: %s\n", this.name);
        System.out.println("Ingredients:");
        for (String s : this.ingredients) {
            System.out.println(s);
        }
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    private static final String regexIngredients = "^(?!\\s*$)([\\p{L}\\s]+)+$";

    public static boolean verifyIngredients(String[] ingredients) {
        boolean isValid = true;
        for (String ingredient: ingredients) {
            if (!ingredient.matches(regexIngredients)) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }


}


