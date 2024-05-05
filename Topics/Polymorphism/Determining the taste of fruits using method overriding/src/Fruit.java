import java.util.Objects;
import java.util.Scanner;

class Fruit {
    public String taste;

    public String taste() {
        return this.taste;

    }
}


class Apple extends Fruit {
    @Override
    public String taste() {
        return "Sweet";
    }
}

// Class for Orange
class Orange extends Fruit {
    @Override
    public String taste() {
        return "Sour";
    }
}

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String fruitType = in.nextLine();

        Fruit fruit = Objects.equals(fruitType, "Apple") ? new Apple() : new Orange();
        System.out.println(fruit.taste());

    }
}