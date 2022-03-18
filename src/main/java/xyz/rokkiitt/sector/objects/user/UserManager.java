package xyz.rokkiitt.sector.objects.user;

import cn.nukkit.Player;
import xyz.rokkiitt.sector.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class UserManager
{
    public static Set<User> users;

    public static void createUser(Player player) {
        User value = new User(player);
        UserManager.users.add(value);
    }


    public static void loadUsers() {
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS `users` (" +
                "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "`nickname` varchar(100) NOT NULL, " +
                "`wings` varchar(100) NOT NULL, " +
                "`rank` varchar(100) NOT NULL, " +
                "`tag` varchar(100) NOT NULL, " +
                "`allypvp` int(100) NOT NULL, " +
                "`pvp` int(100) NOT NULL, " +
                "`isGod` int(100) NOT NULL, " +
                "`kills` int(100) NOT NULL, " +
                "`deaths` int(100) NOT NULL, " +
                "`assist` int(100) NOT NULL, " +
                "`points` int(100) NOT NULL, " +
                "`isVanish` int(100) NOT NULL, " +
                "`protection` varchar(100) NOT NULL, " +
                "`turbodrop` varchar(100) NOT NULL, " +
                "`egapple` int(100) NOT NULL, " +
                "`gapple` int(100) NOT NULL, " +
                "`arrows` int(100) NOT NULL, " +
                "`primedtnt` int(100) NOT NULL, " +
                "`snowballs` int(100) NOT NULL, " +
                "`pearls` int(100) NOT NULL, " +
                "`viptime` bigint(64) NOT NULL, " +
                "`sviptime` bigint(64) NOT NULL, " +
                "`sponsortime` bigint(22) NOT NULL, " +
                "`foodtime` bigint(22) NOT NULL, " +
                "`endertime` bigint(22) NOT NULL, " +
                "`playertime` bigint(22) NOT NULL, " +
                "`onlineoverall` int(100) NOT NULL, " +
                "`onlinetime` int(100) NOT NULL, " +
                "`onlinesession` int(100) NOT NULL," +
                "`firstLogin` int(100) NOT NULL," +
                "`mutetime` varchar(255) NOT NULL," +
                "`alertsenabled` int(100) NOT NULL," +
                "`goldblocks` int(100) NOT NULL," +
                "`placed` int(100) NOT NULL, " +
                "`broken` int(100) NOT NULL, " +
                "`homes` varchar(255) NOT NULL, " +
                "`waypoints` varchar(255) NOT NULL, " +
                "`isIncognito` int(100) NOT NULL, " +
                "`incognito` varchar(255) NOT NULL, " +
                "`IncognitoAlliance` int(100) NOT NULL, " +
                "`IncognitoDead` int(100) NOT NULL," +
                "`IncognitoKill` int(100) NOT NULL," +
                "`chapel` varchar(255) NOT NULL," +
                "`location` varchar(255) NOT NULL," +
                "`guildperms` varchar(255) NOT NULL);");

        try {
            ResultSet query = Main.getProvider().query("SELECT * FROM `users`");
            while (query.next()) {
                User value = new User(query);
                UserManager.users.add(value);
            }
            query.close();
            Main.getPlugin().getLogger().info("Loaded " + users.size() + " players from 'users'");
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli users");
            ex.printStackTrace();
        }
    }

    public static void deleteUser(final User u) {
        UserManager.users.remove(u);
    }
    
    public static User getUser(final String id) {
        for (final User u : UserManager.users) {
            if (u.getNickname().equalsIgnoreCase(id)) {
                return u;
            }
        }
        return null;
    }
    
    static {
        UserManager.users = ConcurrentHashMap.newKeySet();
    }
}
