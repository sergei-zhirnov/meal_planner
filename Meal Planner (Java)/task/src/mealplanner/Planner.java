package mealplanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

class Planner {
    PlannerState state;
    Collection<Meal> meals;

    DbMealsDao con;

    int mealSerial;
    int ingredientSerial;

    public Planner(DbMealsDao con) throws SQLException {
        this.state = PlannerState.SHOW_MENU;
        this.meals = new ArrayList<>();
        this.con = con;

        this.mealSerial = con.getId("meals");
        this.ingredientSerial = con.getId("ingredients");


        this.refreshMeals();
    }

    public void refreshMeals() throws SQLException {
        this.meals = con.findAll();
    }
    public void setState(PlannerState state) {
        this.state = state;
    }

    public void addMeal(Meal meal) throws SQLException {
        con.addMeal(meal);
        this.refreshMeals();
    }

    public void printMenu() {
        System.out.println("What would you like to do (add, show, plan, save, exit)?");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
            case "add" -> this.state = PlannerState.ADD_MEAL;
            case "show" -> this.state = PlannerState.SHOW_MEALS;
            case "plan" -> this.state = PlannerState.CREATE_PLAN;
            case "printplan" -> this.state = PlannerState.SHOW_PLAN;
            case "save" -> this.state = PlannerState.SAVE_LIST;
            case "exit" -> this.state = PlannerState.EXIT;
            default -> this.state = PlannerState.SHOW_MENU;
        }
    }

    public void inputNewMeal() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");

        String preCategory = scanner.nextLine();
        if (!Objects.equals(preCategory, "breakfast") && !Objects.equals(preCategory, "lunch")
                && !Objects.equals(preCategory, "dinner")) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            return;
        }
        MealCategory category = MealCategory.valueOf(preCategory.toUpperCase());
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();

        while (!name.matches("^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$")) {
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }


        System.out.println("Input the ingredients:");
        String mealIngredients;
        do {
            mealIngredients = scanner.nextLine();
            String[] ingredients = mealIngredients.split(",");
            boolean isValidIngredients = Meal.verifyIngredients(ingredients);

            if (!isValidIngredients) {
                System.out.println("Wrong format. Use letters only!");
            }
        } while (!Meal.verifyIngredients(mealIngredients.split(",")));


        ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(mealIngredients.split(",")));

        Meal meal = new Meal(name, category, ingredients);
        this.addMeal(meal);
        System.out.println("The meal has been added!");
        this.state = PlannerState.SHOW_MENU;


    }

    public void printMeals(MealCategory selectedCategory) throws SQLException {
        List<Meal> selectedMeals = con.findByCategory(selectedCategory, false);
        if (selectedMeals.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.println("Category: " + selectedCategory.toString().toLowerCase());
            for (Meal meal : selectedMeals) {
                meal.printMeal();
            }
        }

    }

    public void selectCategoryForPrinting() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");

        String preCategory = scanner.nextLine();
        if (!Objects.equals(preCategory, "breakfast") && !Objects.equals(preCategory, "lunch")
                && !Objects.equals(preCategory, "dinner")) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            return;
        }
        MealCategory category = MealCategory.valueOf(preCategory.toUpperCase());

        this.printMeals(category);

        this.state = PlannerState.SHOW_MENU;


    }

    public void createPlan() throws SQLException {
        con.clearDayPlan();
        Scanner scanner = new Scanner(System.in);

        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day);
            for (MealCategory mealCategory : MealCategory.values()) {

                List<Meal> selectedMeals = con.findByCategory(mealCategory, true);
                for (Meal meal : selectedMeals) {
                    System.out.println(meal.getName());
                }

                System.out.println("Choose the " + mealCategory.toString().toLowerCase()
                        + " for " + day + " from the list above:");

                boolean exitLoop = false;
                String input = null;

                while (!exitLoop) {
                    input = scanner.nextLine();
                    for (Meal m : meals) {
                        if (Objects.equals(m.name, input)) {
                            exitLoop = true;
                            break;
                        }
                    }

                    if (!exitLoop) {
                        System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                    }

                }

                String mealName = input;

                Optional<Meal> optionalMeal = this.meals.stream().filter(m -> Objects.equals(m.name, mealName)).findFirst();

                Meal meal = optionalMeal.get();

                DayPlan dayPlan = new DayPlan(day, meal);
                con.addDayPlan(dayPlan);

            }
            System.out.println("Yeah! We planned the meals for " + day + ".");
        }
        this.state = PlannerState.SHOW_PLAN;
    }

    public void printPlan() throws SQLException {
        List<DayPlan> dayPlanList = con.findAllDayPlan();
        DayPlan.printDayPlanArray(dayPlanList);
        this.state = PlannerState.SHOW_MENU;
    }

    public void saveList() throws SQLException, IOException {

        HashMap<String, Integer> result = con.getList();
        if (result.isEmpty()) {
            System.out.println("Unable to save. Plan your meals first.");
        } else {
            System.out.println("Input a filename:");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.nextLine();
            File file = new File("./" + filename);
            FileWriter fileWriter = new FileWriter(file, true);


            for (String key : result.keySet()) {
                String postfix = result.get(key) > 1 ? " x" + result.get(key) : "";
                fileWriter.write(key + postfix + "\n");
            }

            fileWriter.close();
            System.out.println("Saved!");
        }

        this.state = PlannerState.SHOW_MENU;

    }

    public void action() throws SQLException, IOException {
        switch (this.state) {
            case SHOW_MENU -> printMenu();
            case ADD_MEAL -> this.inputNewMeal();
            case SHOW_MEALS -> this.selectCategoryForPrinting();
            case CREATE_PLAN -> this.createPlan();
            case SHOW_PLAN -> this.printPlan();
            case SAVE_LIST -> this.saveList();
            case EXIT -> this.setState(PlannerState.EXIT);
        }
    }

}

enum PlannerState {
    SHOW_MENU, ADD_MEAL, SHOW_MEALS, CREATE_PLAN, SHOW_PLAN, SAVE_LIST, EXIT
}