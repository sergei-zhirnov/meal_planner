package mealplanner;

import java.sql.*;
import java.util.*;

public class DbMealsDao implements MealsDao {

    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/meals_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1111";
    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public DbMealsDao() throws SQLException {
    }


    @Override
    public List<Meal> findAll() throws SQLException {
        ArrayList<Meal> meals = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT meal_id, meal_category, meal_name, ingredients FROM meals_view;");

        while (rs.next()) {
            Meal meal = new Meal(
                    rs.getInt("meal_id"),
                    rs.getString("meal_name"),
                    MealCategory.valueOf(rs.getString("meal_category")),
                    new ArrayList<>(Arrays.asList((String[]) rs.getArray("ingredients").getArray()))

            );
            meals.add(meal);
        }
        return meals;
    }

    @Override
    public void createRelations() throws SQLException {
       Statement statement = connection.createStatement();


//        statement.executeUpdate("DROP TABLE IF EXISTS ingredients;");
//        statement.executeUpdate("DROP TABLE IF EXISTS plan;");
//        statement.executeUpdate("DROP TABLE IF EXISTS meals;");
        this.dropViews();


        statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals (" +
                "meal_id integer," +
                "category varchar," +
                "meal varchar, " +
                "PRIMARY KEY(meal_id)" +
                ");");


        statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients (" +
                "ingredient_id integer," +
                "ingredient varchar," +
                "meal_id integer, " +
                "PRIMARY KEY(ingredient_id), " +
                "CONSTRAINT fk_meal FOREIGN KEY(meal_id) REFERENCES meals(meal_id)" +
                ");");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS plan (" +
                "day varchar," +
                "category varchar," +
                "meal_id integer, " +
                "PRIMARY KEY(day, category), " +
                "CONSTRAINT fk_meal FOREIGN KEY(meal_id) REFERENCES meals(meal_id)" +
                ");");




    }



    @Override
    public int getId(String tableName) {
        int result;
        String entityName = Objects.equals(tableName, "meals") ? "meal_id" : "ingredient_id";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT MAX(" + entityName + ") as max_id FROM " + tableName + ";");
            rs.next();
            result = rs.getInt("max_id") + 1;
        } catch (Exception e) {
            result = 1;
        }

        return result;
    }

    @Override
    public void addMeal(Meal meal) throws SQLException {
        int mealId = getId("meals");
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO meals (meal_id, category, meal) VALUES ("
                + mealId + ", '"
                + meal.category + "', '" +
                meal.name + "');");
        ResultSet rs = statement.executeQuery("SELECT meal_id FROM meals WHERE meal = '" + meal.name + "';");
        rs.next();

        for (String s : meal.ingredients) {
            statement.executeUpdate("INSERT INTO ingredients (ingredient_id, ingredient, meal_id) VALUES ("
                    + getId("ingredients") + ", '"
                    + s + "',"
                    + mealId + ");");

        }

    }

    @Override
    public List<Meal> findByCategory(MealCategory category, boolean orderByName) throws SQLException {
        ArrayList<Meal> meals = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sorting = orderByName ? "ORDER BY meal_name ASC" : "ORDER BY meal_id ASC";
        ResultSet rs = statement.executeQuery("SELECT meal_id, meal_category, meal_name, ingredients " +
                "FROM meals_view " +
                "WHERE meal_category = '" + category + "' " +
                sorting + ";");

        while (rs.next()) {
            Meal meal = new Meal(
                    rs.getString("meal_name"),
                    MealCategory.valueOf(rs.getString("meal_category")),
                    new ArrayList<>(Arrays.asList((String[]) rs.getArray("ingredients").getArray()))

            );
            meals.add(meal);
        }
        return meals;
    }

    @Override
    public List<DayPlan> findAllDayPlan() throws SQLException {
        List<DayPlan> dayplans = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT day, meal_category, meal_name FROM plan_view;");
        while (rs.next()) {
            DayOfWeek day = DayOfWeek.valueOf(rs.getString("day"));
            MealCategory mealCategory = MealCategory.valueOf(rs.getString("meal_category"));
            String mealName = rs.getString("meal_name");

            DayPlan dayPlan = new DayPlan(day, mealCategory, mealName);
            dayplans.add(dayPlan);
        }

        return dayplans;
    }

    @Override
    public void addDayPlan(DayPlan dayplan) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO plan VALUES ('"
                + dayplan.day + "', '" + dayplan.mealCategory + "', " + dayplan.meal.id +
                ");");

    }
    @Override
    public void clearDayPlan() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM plan;");
    }

    @Override
    public void dropViews() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP VIEW IF EXISTS meals_view;");
        statement.executeUpdate("DROP VIEW IF EXISTS plan_view;");

    }

    @Override
    public void createViews() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE VIEW meals_view AS " +
                "SELECT " +
                "m.meal_id, " +
                "m.category AS meal_category, " +
                "m.meal AS meal_name, " +
                "ARRAY_AGG(i.ingredient) as ingredients " +
                "FROM meals m " +
                "LEFT JOIN ingredients i ON i.meal_id = m.meal_id " +
                "GROUP BY 1, 2, 3 " +
                "ORDER BY 1");

        statement.executeUpdate("CREATE VIEW plan_view AS " +
                "SELECT " +
                "p.day, " +
                "p.category AS meal_category, " +
                "m.meal AS meal_name " +
                "FROM plan p " +
                "JOIN meals m ON p.meal_id = m.meal_id");



    }

    @Override
    public HashMap<String, Integer> getList() throws SQLException {
        HashMap<String, Integer> result = new HashMap<>();
        Statement statement = connection.createStatement();

       ResultSet rs = statement.executeQuery("""
                WITH t1 AS (
                SELECT p.day, p.category, p.meal_id, i.ingredient
                FROM plan p
                LEFT JOIN ingredients i ON p.meal_id = i.meal_id
                )
                SELECT ingredient, COUNT(ingredient) AS count
                FROM t1
                GROUP BY ingredient""");

       while (rs.next()) {
           String ingredient = rs.getString("ingredient");
           Integer count = rs.getInt("count");
           if (result.containsKey(ingredient) ) {
               result.put(ingredient, count + result.get(ingredient));
           } else {
               result.put(ingredient, count);
           }
       }

       return result;

    }
}
