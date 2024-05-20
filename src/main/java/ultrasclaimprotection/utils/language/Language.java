package ultrasclaimprotection.utils.language;

import java.util.List;

import ultrasclaimprotection.utils.chat.ChatColorTranslator;

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
    public static List<String> getList(String path) {
        return (List<String>) get(path);
    }
}