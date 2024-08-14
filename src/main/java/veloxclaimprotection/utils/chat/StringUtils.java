package veloxclaimprotection.utils.chat;

public class StringUtils {
    public static boolean isAlphanumericString(String input) {
        String regex = "^[a-zA-Z0-9]+$";

        return input.matches(regex);
    }
}
