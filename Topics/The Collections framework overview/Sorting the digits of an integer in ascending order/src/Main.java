
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        System.out.println(convertToList(number));


    }

    public static List<Integer> convertToList(int number) {

        final int ten = 10;

        int len = Integer.toString(number).length();
        ArrayList<Integer> arr = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            arr.add(number % ten);
            number = number / ten;
        }
        Collections.sort(arr);

        return arr;
    }
}