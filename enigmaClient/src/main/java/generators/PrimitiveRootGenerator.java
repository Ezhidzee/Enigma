package generators;

import java.util.HashSet;
import java.util.Set;

public class PrimitiveRootGenerator {
    private static long GetPRoot(long p) {
        for (long i = 0; i < p; i++)
            if (IsPRoot(p, i)) return i;
        return 0;
    }

    private static boolean IsPRoot(long p, long a) {
        if (a == 0 || a == 1) return false;
        long last = 1;

        Set<Long> set = new HashSet<>();
        for (long i = 0; i < p - 1; i++) {
            last = (last * a) % p;
            if (set.contains(last)) return false;
            set.add(last);
        }
        return true;
    }

    public static Long getRoot(Long p) {
        return GetPRoot(p);
    }
}
