package mealplanner;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface MealsDao {
    List<Meal> findAll() throws SQLException;
    void createRelations() throws SQLException;
    int getId(String tableName);
    void addMeal(Meal meal) throws SQLException;
    List<Meal> findByCategory(MealCategory category, boolean orderByName) throws SQLException;
    List<DayPlan> findAllDayPlan() throws SQLException;
    void addDayPlan(DayPlan dayplan) throws SQLException;

    void clearDayPlan() throws SQLException;

    void dropViews() throws SQLException;
    void createViews() throws SQLException;

    HashMap<String, Integer> getList() throws SQLException;







}
