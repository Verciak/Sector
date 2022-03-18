package xyz.rokkiitt.sector.objects;

import java.util.*;
import cn.nukkit.permission.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.user.User;
import cn.nukkit.plugin.*;
import java.util.concurrent.*;

public class Permissions
{
    private static final Map<String, PermissionAttachment> permissions;
    
    private static void setPerm(final PermissionAttachment a, final String rank) {
        if (rank.equalsIgnoreCase("vip")) {
            a.setPermission("vip", true);
            a.setPermission("cmd.repair", true);
            a.setPermission("cmd.enderchest", true);
            a.setPermission("cmd.workbench", true);
            a.setPermission("vip.kit", true);
        }
        else if (rank.equalsIgnoreCase("svip")) {
            a.setPermission("svip", true);
            a.setPermission("cmd.repair", true);
            a.setPermission("cmd.repairall", true);
            a.setPermission("cmd.enderchest", true);
            a.setPermission("cmd.workbench", true);
            a.setPermission("svip.kit", true);
        }
        else if (rank.equalsIgnoreCase("sponsor")) {
            a.setPermission("sponsor", true);
            a.setPermission("cmd.repair", true);
            a.setPermission("cmd.repairall", true);
            a.setPermission("cmd.enderchest", true);
            a.setPermission("cmd.workbench", true);
            a.setPermission("sponsor.kit", true);
        }
    }
    
    public static void removePermissions(final Player p) {
        Permissions.permissions.remove(p.getName());
    }
    
    public static void addPermision(final Player p, final String perm) {
        final PermissionAttachment att = Permissions.permissions.get(p.getName());
        att.setPermission(perm, true);
        Permissions.permissions.replace(p.getName(), att);
    }
    
    public static void refreshPermissions(final Player p, final User u) {
        final PermissionAttachment att = Permissions.permissions.get(p.getName());
        att.getPermissions().clear();
        setPerm(att, u.getRank());
        Permissions.permissions.replace(p.getName(), att);
    }
    
    public static void removePermision(final Player p, final String perm) {
        final PermissionAttachment att = Permissions.permissions.get(p.getName());
        att.unsetPermission(perm, true);
        Permissions.permissions.replace(p.getName(), att);
    }
    
    public static void setupPermissions(final Player p, final User u) {
        final PermissionAttachment att = p.addAttachment((Plugin) Main.getPlugin());
        att.getPermissions().clear();
        setPerm(att, u.getRank());
        Permissions.permissions.put(p.getName(), att);
    }
    
    public static void setupPermissions(final Player p, final String u) {
        final PermissionAttachment att = p.addAttachment((Plugin)Main.getPlugin());
        att.getPermissions().clear();
        setPerm(att, u);
        Permissions.permissions.put(p.getName(), att);
    }
    
    static {
        permissions = new ConcurrentHashMap<String, PermissionAttachment>();
    }
}
