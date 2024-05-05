package mealplanner;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {
  public static void main(String[] args) throws SQLException, IOException {

    DbMealsDao con = new DbMealsDao();
    con.createRelations();

    con.dropViews();
    con.createViews();
    Planner planner = new Planner(con);
    con.dropViews();
    while (!Objects.equals(planner.state, PlannerState.EXIT)) {

      con.createViews();
      planner.action();
      con.dropViews();
    }
    System.out.println("Bye!");

  }
}




