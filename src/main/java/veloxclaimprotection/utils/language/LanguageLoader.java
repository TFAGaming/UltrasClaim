package veloxclaimprotection.utils.language;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import veloxclaimprotection.VeloxClaimProtection;

public class LanguageLoader {
    public static FileConfiguration loaded;

    public LanguageLoader(VeloxClaimProtection plugin) throws IOException {
        File directory = new File(plugin.getDataFolder(), "languages/");

        File defaultPath = new File(plugin.getDataFolder(), "languages/en_US.yml");

        if (!directory.isDirectory()) {
            directory.mkdir();

            try {
                InputStream stream = plugin.getResource("en_US.yml");
                FileUtils.copyInputStreamToFile(stream, defaultPath);
            } catch (IOException error) {
                error.printStackTrace();
            }
        }

        String locale = plugin.getConfig().getString("language");

        if (locale != null) {
            File localefile = new File(plugin.getDataFolder(), "languages/" + locale + (locale.endsWith(".yml") ? "" : ".yml"));

            FileConfiguration loaded = YamlConfiguration.loadConfiguration(localefile);

            LanguageLoader.loaded = loaded;
        } else {
            FileConfiguration loaded = YamlConfiguration.loadConfiguration(defaultPath);

            LanguageLoader.loaded = loaded;
        }
    }
}