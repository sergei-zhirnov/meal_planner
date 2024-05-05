import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] strArr = scanner.nextLine().split(" ");
        int aim = scanner.nextInt();
        ArrayList<Integer> arr = new ArrayList<>();
        int diff = Integer.MAX_VALUE;
        for (String s : strArr) {
            int num = Integer.parseInt(s);
            arr.add(num);
            diff = Math.min(diff, Math.abs(num - aim));
        }

        ArrayList<Integer> delArr = new ArrayList<>();

        for (Integer integer : arr) {
            if (Math.abs(integer - aim) > diff) {
                delArr.add(integer);
            }
        }
        arr.removeAll(delArr);
        Collections.sort(arr);

        for (Integer i : arr) {
            System.out.print(i + " ");
        }


    }


}
