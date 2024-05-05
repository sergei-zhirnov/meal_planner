import java.util.List;
import java.util.Objects;

class Counter {

    public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
        int counter1 = 0;
        int counter2 = 0;

        for (Integer el : list1) {
            if (Objects.equals(el, elem)) {
                counter1++;
            }
        }

        for (Integer el : list2) {
            if (Objects.equals(el, elem)) {
                counter2++;
            }
        }
        return counter1 == counter2;
    }
}