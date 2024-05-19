package ultrasclaimprotection;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import ultrasclaimprotection.commands.LandsCommand;
import ultrasclaimprotection.database.Database;
import ultrasclaimprotection.utils.console.Console;

public class UltrasClaimProtection extends JavaPlugin {
	public static Database database;

	public void onEnable() {
		Console.printPluginBanner();

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		UltrasClaimProtection.database = new Database(
				getDataFolder().getAbsolutePath() + this.getConfig().getString("database.path"));

		try {
			UltrasClaimProtection.database.getTablesReady();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		getCommand("lands").setExecutor(new LandsCommand());
        getCommand("land").setExecutor(new LandsCommand());

		Console.info("The plugin is ready to use.");
	}

	public void onDisable() {
		Console.info("The plugin is turned off.");
	}
}
