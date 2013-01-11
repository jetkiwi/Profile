/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ja731j.profile;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author ja731j
 */
public class Profile extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("The Profile plugin has been loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("The Profile plugin has been unloaded");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("profile")) {
            try {
                return (new Commands(this)).execCommand(sender, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
