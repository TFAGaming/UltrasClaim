package veloxclaimprotection.utils.language;

import java.util.List;
import java.util.Map;

import veloxclaimprotection.utils.chat.ChatColorTranslator;

public class Language {
    public static Object get(String path){
        return LanguageLoader.loaded.get(path);
    }

    public static String getString(String path, boolean... prefix) {
        if (prefix.length > 0 && prefix[0] == false) {
            return (String) get(path);
        }

        String string_prefix = "";
        string_prefix += ChatColorTranslator.translate(Language.getString("commands.prefix", false));

        return string_prefix + (String) get(path);
    }

    public static int getInt(String path) {
        return (int) get(path);
    }

    public static boolean getBoolean(String path) {
        return (boolean) get(path);
    }

    @SuppressWarnings("unchecked")
    public static List<Map<?, ?>> getMapList(String path) {
        return (List<Map<?, ?>>) get(path);
    }

    @SuppressWarnings("unchecked")
    public static List<String> getListString(String path) {
        return (List<String>) get(path);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getListObject(String path) {
        return (List<Object>) get(path);
    }
}
