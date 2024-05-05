import java.util.Scanner;

// Abstract base class
abstract class Shape {
    protected String color;
    public Shape(String color) {
        this.color = color;
    }
    public abstract double getArea();
}

// Circle subclass
class Circle extends Shape {
    private double radius;
    final static double pi = 3.14;
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return this.radius * this.radius * pi;
    }
}

// Rectangle subclass
class Rectangle extends Shape {
    private double length;
    private double width;

    public Rectangle(String color, double length, double width) {
        super(color);
        this.length = length;
        this.width = width;
    }

    // Override getArea method
    @Override
    public double getArea() {
        return this.length * this.width;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create Circle instance
        double radius = scanner.nextDouble();
        String circleColor = scanner.next();
        Circle circle = new Circle(circleColor, radius);

        // Create Rectangle instance
        double length = scanner.nextDouble();
        double width = scanner.nextDouble();
        String rectangleColor = scanner.next();
        Rectangle rectangle = new Rectangle(rectangleColor, length, width);

        // Print the area and color of each shape
        System.out.println(circle.getArea());
        System.out.println(circle.color);
        System.out.println(rectangle.getArea());
        System.out.println(rectangle.color);

        scanner.close();
    }
}