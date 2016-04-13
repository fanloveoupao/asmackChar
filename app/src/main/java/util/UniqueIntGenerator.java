package util;

/**
 * Created by bruse on 16/3/8.
 */
public class UniqueIntGenerator {
    private static int i = 0;

    public static int next() {
        if (i >= Integer.MAX_VALUE) {
            i = 0;
        }
        return i++;
    }
}
