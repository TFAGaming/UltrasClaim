package veloxclaimprotection.utils.chat;

import org.bukkit.Chunk;
import org.bukkit.Location;

import veloxclaimprotection.VeloxClaimProtection;
import veloxclaimprotection.utils.language.Language;

public class Variables {
    public static final VeloxClaimProtection plugin = VeloxClaimProtection.getPlugin(VeloxClaimProtection.class);

    public static String getChunkDetail(Chunk chunk) {
        return ChatColorTranslator.translate(Language.getString("general.variables.chunk_details", false)
                .replace("%chunk_x%", "" + chunk.getX())
                .replace("%chunk_z%", "" + chunk.getZ())
                .replace("%chunk_world%", chunk.getWorld().getName()));
    }

    public static String getLocationDetail(Location location) {
        return ChatColorTranslator.translate(Language.getString("general.variables.location_details", false)
                .replace("%location_x%", getTwoDigitsAfterDecimal(location.getX()))
                .replace("%location_y%", getTwoDigitsAfterDecimal(location.getY()))
                .replace("%location_z%", getTwoDigitsAfterDecimal(location.getZ()))
                .replace("%location_world%", location.getWorld().getName()));
    }

    private static String getTwoDigitsAfterDecimal(double value) {
        return String.format("%.2f", value);
    }
}
