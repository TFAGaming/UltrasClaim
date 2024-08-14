package veloxclaimprotection.utils.console;

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

        String banner = " __     __   _            ____ _       _           \r\n" + //
                        " \\ \\   / /__| | _____  __/ ___| | __ _(_)_ __ ___  \r\n" + //
                        "  \\ \\ / / _ \\ |/ _ \\ \\/ / |   | |/ _` | | '_ ` _ \\ \r\n" + //
                        "   \\ V /  __/ | (_) >  <| |___| | (_| | | | | | | |\r\n" + //
                        "    \\_/ \\___|_|\\___/_/\\_\\\\____|_|\\__,_|_|_| |_| |_|\r\n" + //
                        "";

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
