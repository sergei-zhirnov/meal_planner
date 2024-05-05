public class Main {
    public static void main(String[] args) {
        Dog.main();
    }
}

class Animal {
    public String speak() {
        return "Animal sound...";
    }
}

class Dog extends Animal {
   @Override
    public String speak() {
        return "Bark!";
    }

    static void main() {
       Dog fido = new Dog();
        System.out.println(fido.speak());
    }
}