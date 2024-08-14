package veloxclaimprotection;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import veloxclaimprotection.commands.LandsCommand;
import veloxclaimprotection.database.Database;
import veloxclaimprotection.events.chunks.BlocksProtectionListener;
import veloxclaimprotection.events.chunks.EntitiesProtectionListener;
import veloxclaimprotection.events.chunks.EnvironmentProtectionListener;
import veloxclaimprotection.events.chunks.PlayerMovementChunkEntry;
import veloxclaimprotection.events.gui.NormalGUIListener;
import veloxclaimprotection.events.gui.PaginationGUIListener;
import veloxclaimprotection.events.teleportation.PlayerMovedDelayedTP;
import veloxclaimprotection.plugins.VaultAPI;
import veloxclaimprotection.utils.console.Console;
import veloxclaimprotection.utils.language.LanguageLoader;

public class VeloxClaimProtection extends JavaPlugin {
	public static Database database;
	public static LanguageLoader language;
	public static VaultAPI vaultapi;

	public void onEnable() {
		saveDefaultConfig();

		Console.printPluginBanner();

		try {
            LanguageLoader languageLoader = new LanguageLoader(this);

            VeloxClaimProtection.language = languageLoader;

			Console.info("Successfully loaded language file.");
        } catch (IOException error) {
            Console.error("Failed to load language file.");

            error.printStackTrace();

			disablePlugin();

            return;
        }

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		VeloxClaimProtection.database = new Database(
				getDataFolder().getAbsolutePath() + this.getConfig().getString("database.path"));

		try {
			VeloxClaimProtection.database.getTablesReady();
		} catch (SQLException error) {
			Console.error("Failed to connect to the database.");

			error.printStackTrace();

			disablePlugin();

			return;
		}

		VeloxClaimProtection.vaultapi = new VaultAPI(this);

		if (!VeloxClaimProtection.vaultapi.setupPermissions()) {
			Console.error("Failed to load VaultAPI Permissions.");

			disablePlugin();

			return;
		} else {
			Console.info("Successfully loaded VaultAPI Permissions.");
		}

		getServer().getPluginManager().registerEvents(new PaginationGUIListener(), this);
		getServer().getPluginManager().registerEvents(new NormalGUIListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMovedDelayedTP(), this);
		getServer().getPluginManager().registerEvents(new BlocksProtectionListener(), this);
		getServer().getPluginManager().registerEvents(new EnvironmentProtectionListener(), this);
		getServer().getPluginManager().registerEvents(new EntitiesProtectionListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerMovementChunkEntry(), this);

		getCommand("lands").setExecutor(new LandsCommand());
        getCommand("land").setExecutor(new LandsCommand());

		Console.info("The plugin is ready to use.");
	}

	public void onDisable() {
		Console.info("The plugin is turned off.");

		try {
            VeloxClaimProtection.database.closeConnection();

            Console.info("Successfully closed database connection.");
        } catch (SQLException error) {
            Console.error("Failed to close database connection.");

            error.printStackTrace();
        }
	}

	private void disablePlugin() {
        getServer().getPluginManager().disablePlugin(this);
    }
}
