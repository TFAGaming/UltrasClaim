package ultrasclaimprotection.utils.console;

import java.util.logging.Logger;

public class Console {
    private static Logger logger = Logger.getLogger("UltrasClaimProtection");

    private static String getPluginPrefix() {
        return ConsoleColors.BLUE + "[UlrasClaim]" + ConsoleColors.RESET;
    };

    public static void printPluginBanner() {
        String line_splitter = "";

        for (int i = 0; i < 56; i++) {
            line_splitter += "-";
        }

        String banner = " _   _ _ _                 _____ _       _           \r\n" + //
                        "| | | | | |               /  __ \\ |     (_)          \r\n" + //
                        "| | | | | |_ _ __ __ _ ___| /  \\/ | __ _ _ _ __ ___  \r\n" + //
                        "| | | | | __| '__/ _` / __| |   | |/ _` | | '_ ` _ \\ \r\n" + //
                        "| |_| | | |_| | | (_| \\__ \\ \\__/\\ | (_| | | | | | | |\r\n" + //
                        " \\___/|_|\\__|_|  \\__,_|___/\\____/_|\\__,_|_|_| |_| |_|";

        logger.info(line_splitter);

        for (String line : banner.split("\n")) {
            logger.info(line);
        }

        logger.info(line_splitter);
    }

    public static void info(String message) {
        logger.info(getPluginPrefix() + " " + ConsoleColors.GREEN + message + ConsoleColors.RESET);

        return;
    }

    public static void warning(String message) {
        logger.warning(getPluginPrefix() + " " + ConsoleColors.YELLOW + message + ConsoleColors.RESET);
        
        return;
    }

    public static void error(String message) {
        logger.severe(getPluginPrefix() + " " + ConsoleColors.RED + message + ConsoleColors.RESET);
        
        return;
    }
}
