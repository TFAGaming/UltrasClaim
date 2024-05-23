package ultrasclaimprotection;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import ultrasclaimprotection.commands.LandsCommand;
import ultrasclaimprotection.database.Database;
import ultrasclaimprotection.events.chunks.BlocksProtectionListener;
import ultrasclaimprotection.events.chunks.EntitiesProtectionListener;
import ultrasclaimprotection.events.chunks.EnvironmentProtectionListener;
import ultrasclaimprotection.events.gui.NormalGUIListener;
import ultrasclaimprotection.events.gui.PaginationGUIListener;
import ultrasclaimprotection.events.teleportation.PlayerMovedDelayedTP;
import ultrasclaimprotection.utils.console.Console;
import ultrasclaimprotection.utils.language.LanguageLoader;

public class UltrasClaimProtection extends JavaPlugin {
	public static Database database;
	public static LanguageLoader language;

	public void onEnable() {
		saveDefaultConfig();

		Console.printPluginBanner();

		try {
            LanguageLoader languageLoader = new LanguageLoader(this);

            UltrasClaimProtection.language = languageLoader;
        } catch (IOException error) {
            Console.error("Failed to load language file.");

            error.printStackTrace();

			disablePlugin();

            return;
        }

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		UltrasClaimProtection.database = new Database(
				getDataFolder().getAbsolutePath() + this.getConfig().getString("database.path"));

		try {
			UltrasClaimProtection.database.getTablesReady();
		} catch (SQLException error) {
			Console.error("Failed to connect to the database.");

			error.printStackTrace();

			disablePlugin();

			return;
		}

		getServer().getPluginManager().registerEvents(new PaginationGUIListener(), this);
		getServer().getPluginManager().registerEvents(new NormalGUIListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMovedDelayedTP(), this);
		getServer().getPluginManager().registerEvents(new BlocksProtectionListener(), this);
		getServer().getPluginManager().registerEvents(new EnvironmentProtectionListener(), this);
		getServer().getPluginManager().registerEvents(new EntitiesProtectionListener(), this);

		getCommand("lands").setExecutor(new LandsCommand());
        getCommand("land").setExecutor(new LandsCommand());

		Console.info("The plugin is ready to use.");
	}

	public void onDisable() {
		Console.info("The plugin is turned off.");
	}

	private void disablePlugin() {
        getServer().getPluginManager().disablePlugin(this);
    }
}
