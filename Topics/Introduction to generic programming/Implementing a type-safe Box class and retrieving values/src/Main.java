import java.util.*;

public class Main {
    static class Box<T> {
        T var;
        Box(T var) {
            this.var = var;
        }

        T get() {
            return this.var;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if(sc.hasNextInt()) {
            int num = sc.nextInt();
            Box<Integer> box = new Box(num);
            System.out.println(box.get());
        } else if (sc.hasNextFloat()) {
            float num = sc.nextFloat();
            Box<Integer> box = new Box(num);
            System.out.println(box.get());
        } else {
            String str = sc.next();
            Box<Integer> box = new Box(str);
            System.out.println(box.get());
        }
    }
}