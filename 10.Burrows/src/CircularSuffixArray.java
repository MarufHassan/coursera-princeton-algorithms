import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private Integer[] index;
    private int len;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();

        this.len = s.length();
        index = new Integer[len];

        for (int i = 0; i < len; i++) {
            index[i] = i;
        }

        Arrays.sort(index, new CustomComparator(s));
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= len)
            throw new IllegalArgumentException();
        return index[i];
    }

    private class CustomComparator implements Comparator<Integer> {
        private String s;
        private int len;

        public CustomComparator(String s) {
            this.s = s;
            this.len = s.length();
        }

        @Override
        public int compare(Integer n1, Integer n2) {
            for (int i = 0; i < len; i++) {
                int pos1 = (n1 + i) % len;
                int pos2 = (n2 + i) % len;
                if (s.charAt(pos1) != s.charAt(pos2))
                    return s.charAt(pos1) - s.charAt(pos2);
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray arr = new CircularSuffixArray(s);

        for (int i = 0; i < arr.length(); i++) {
            System.out.println(arr.index(i));
        }
    }
}