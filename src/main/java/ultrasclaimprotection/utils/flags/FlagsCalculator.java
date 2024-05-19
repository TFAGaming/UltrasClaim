package ultrasclaimprotection.utils.flags;

public class FlagsCalculator {
    public static int calculate(int... flags) {
        int combined = 0;

        for (int flag : flags) {
            combined |= flag;
        }

        return combined;
    }

    public static boolean isFlagSet(int flags, int flag) {
        return (flags & flag) != 0;
    }

    public static int removeFlag(int flags, int flag) {
        return flags & ~flag;
    }
}
