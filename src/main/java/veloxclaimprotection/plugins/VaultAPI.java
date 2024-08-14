package veloxclaimprotection.plugins;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import veloxclaimprotection.VeloxClaimProtection;

import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPI {
    private VeloxClaimProtection plugin;
    public Chat chat;
    public Permission permissions;

    public VaultAPI(VeloxClaimProtection plugin) {
        this.plugin = plugin;
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = this.plugin.getServer().getServicesManager().getRegistration(Chat.class);

        chat = rsp.getProvider();

        return chat != null;
    }
    
    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = this.plugin.getServer().getServicesManager().getRegistration(Permission.class);

        permissions = rsp.getProvider();

        return permissions != null;
    }

    public Chat getChat() {
        return chat;
    }

    public Permission getPermissions() {
        return permissions;
    }
}
