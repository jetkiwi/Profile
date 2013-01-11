/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ja731j.profile;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 *
 * @author ja731j
 */
public class DB {

    private SqlJetDb db;

    DB(JavaPlugin plugin) throws Exception {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        //Open DB file and create it if it does not exist
        File dbFile = new File(plugin.getDataFolder(), "profile.db");
        if (!dbFile.exists()) {
            dbFile.createNewFile();
        }
        SqlJetDb db = SqlJetDb.open(dbFile, true);
        if (db.getOptions().getUserVersion() != 1) {
            db.getOptions().setAutovacuum(true);
            db.beginTransaction(SqlJetTransactionMode.WRITE);
            try {
                db.getOptions().setUserVersion(1);
                db.createTable("create table profile (player text not null unique, message text not null)");
                db.createIndex("create index player_index on profile(player)");
            } finally {
                db.commit();
            }
        }
        this.db = db;
    }

    public boolean add(String player, String message) {
        try {
            try {
                db.beginTransaction(SqlJetTransactionMode.WRITE);
                ISqlJetTable table = db.getTable("profile");
                if (exists(player)) {
                    table.lookup("player_index", player).update(player,message);
                }else{
                table.insert(player, message);
                }
            
            } finally {
                db.commit();
                return true;
            }
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remove(String player) {
        try {
            try {
                db.beginTransaction(SqlJetTransactionMode.WRITE);
                ISqlJetTable table = db.getTable("profile");
                if (!exists(player)) {
                    return false;
                }
                table.lookup("player_index", player).delete();

            } finally {
                db.commit();
                return true;
            }
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String player) {
        try {
            db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetTable table = db.getTable("profile");
            return !table.lookup("player_index", player).eof();
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String get(String player) {
        try {
            db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
            ISqlJetTable table = db.getTable("profile");
            ISqlJetCursor cur = table.lookup("player_index", player);
            if (!cur.eof()) {
                return cur.getString("message");
            }
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
