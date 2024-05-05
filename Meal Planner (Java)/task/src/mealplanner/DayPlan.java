package mealplanner;


import java.util.List;
import java.util.Objects;

public class DayPlan {
    DayOfWeek day;
    MealCategory mealCategory;
    String mealName;
    Meal meal;




    DayPlan(DayOfWeek day, MealCategory mealCategory, String mealName) {
        this.day = day;
        this.mealCategory = mealCategory;
        this.mealName = mealName;
    }

    static void printDayPlanArray(List<DayPlan> dayPlanList) {
        String printingDay = null;
        for (DayPlan dayPlan : dayPlanList) {
            if (!Objects.equals(printingDay, dayPlan.day.toString())) {
                System.out.println();
                System.out.println(dayPlan.day);
            }

            System.out.println(dayPlan.mealCategory.toString().substring(0, 1).toUpperCase() +
                    dayPlan.mealCategory.toString().substring(1).toLowerCase() + ": " + dayPlan.mealName);

            printingDay = dayPlan.day.toString();

        }

    }


    DayPlan(DayOfWeek day, Meal meal) {
        this.day = day;
        this.mealCategory = meal.category;
        this.mealName = meal.name;
        this.meal = meal;
    }

    public void printDayPlan() {
        System.out.println(this.day);
        System.out.println(mealCategory.toString().substring(0, 1).toUpperCase() +
                mealCategory.toString().substring(1).toLowerCase() + ": " + this.mealName);


    }


}
