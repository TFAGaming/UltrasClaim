package ultrasclaimprotection.plugins;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import ultrasclaimprotection.UltrasClaimProtection;

import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPI {
    private UltrasClaimProtection plugin;
    public Chat chat;
    public Permission permissions;

    public VaultAPI(UltrasClaimProtection plugin) {
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
