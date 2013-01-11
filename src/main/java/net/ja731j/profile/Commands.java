/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ja731j.profile;

import java.util.Arrays;
import net.syamn.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author ja731j
 */
public class Commands {

    private DB db;

    public Commands(JavaPlugin plugin) throws Exception {
        db = new DB(plugin);
    }

    public boolean execCommand(CommandSender sender, String args[]) {
        if (args.length < 1) {
            sender.sendMessage("Invalid argument!");
            help(sender);
            return false;
        }
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if (command.equalsIgnoreCase("set")) {
            return set(sender, args);
        } else if (command.equalsIgnoreCase("view")) {
            return view(sender, args);
        } else if (command.equalsIgnoreCase("remove")) {
            return remove(sender, args);
        } else if (command.equalsIgnoreCase("help")) {
            help(sender);
            return false;
        } else {
            sender.sendMessage("Invalid argument!");
            help(sender);
            return false;
        }
    }

    public boolean set(CommandSender sender, String args[]) {
        if (args.length < 2) {
            sender.sendMessage("Invalid argument!");
            return false;
        }
        if (!Util.isValidName(args[0])) {
            sender.sendMessage("Invalid player name!");
            return true;
        }
        if(sender instanceof Player){
            if(sender.getName().equalsIgnoreCase(args[0])){
                if(!((Player)sender).hasPermission("profile.set.own")){
                    sender.sendMessage("You do not have permission to do that!");
                    return true;
                }
            }else{
                if(!((Player)sender).hasPermission("profile.set.others")){
                    sender.sendMessage("You do not have permission to do that!");
                    return true;
                }
            }
        }
        
        
        if (args[1].length() > 16) {
            sender.sendMessage("Message too long!");
            return true;
        }
        if(db.add(args[0], args[1])){
            sender.sendMessage("Updated profile for "+args[0]);
            return true;
        }else{
            sender.sendMessage("Failed to update profile for "+args[0]);
            return true;
        }
    }

    public boolean view(CommandSender sender, String args[]) {
        if (args.length < 1) {
            sender.sendMessage("Invalid argument!");
            return false;
        }
        if (!Util.isValidName(args[0])) {
            sender.sendMessage("Invalid player name!");
            return true;
        }
        
        if(sender instanceof Player){
            if(sender.getName().equalsIgnoreCase(args[0])){
                if(!((Player)sender).hasPermission("profile.view.own")){
                    sender.sendMessage("You do not have permission to do that!");
                    return true;
                }
            }else{
                if(!((Player)sender).hasPermission("profile.view.others")){
                    sender.sendMessage("You do not have permission to do that!");
                    return true;
                }
            }
        }
        
        String message = db.get(args[0]);
        if (message == null) {
            sender.sendMessage("Profile for " + args[0] + " does not exist!");
            return true;
        } else {
            sender.sendMessage("Profile of " + args[0] + " : " + message);
            return true;
        }
    }

    public boolean remove(CommandSender sender, String args[]) {
        if (args.length < 1) {
            sender.sendMessage("Invalid argument!");
            return false;
        }
        if (!Util.isValidName(args[0])) {
            sender.sendMessage("Invalid player name!");
            return true;
        }
        
                if(sender instanceof Player){
            if(sender.getName().equalsIgnoreCase(args[0])){
                if(!((Player)sender).hasPermission("profile.remove.own")){
                    sender.sendMessage("You do not have permission to do that!");
                    return true;
                }
            }else{
                if(!((Player)sender).hasPermission("profile.remove.others")){
                    sender.sendMessage("You do not have permission to do that!");
                    return true;
                }
            }
        }
        
        if(db.remove(args[0])){
            sender.sendMessage("Removed profile for "+args[0]);
            return true;
        }else{
            sender.sendMessage("Failed to remove profile for "+args[0]);
            return true;
        }
    }

    public static void help(CommandSender sender) {
    }
}
