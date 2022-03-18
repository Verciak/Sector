package xyz.rokkiitt.sector.listeners.guild;

import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.entity.blockentity.ChestTile;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.guild.logblock.LogblockInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.math.*;
import cn.nukkit.block.*;
import cn.nukkit.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.event.*;

public class InteractListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Block b = e.getBlock();
        final Player p = e.getPlayer();
        if (e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && b != null) {
            if (Config.isSpawn(p.getLocation())) {
                return;
            }
            final Guild g = GuildManager.getGuild(b.getLocation());
            if (g != null) {
                final User u = UserManager.getUser(p.getName());
                if (u != null) {
                    if (u.getTag().equalsIgnoreCase(g.getTag())) {
                        if (!u.hasPermission("7")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "interakcji"));
                        }
                        else if ((b.getId() == 54 || b.getId() == 146) && !u.hasPermission("3")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "otwierania skrzynek"));
                        }
                        else if (b.getId() == 54 || b.getId() == 146) {
                            if (p.isSneaking()) {
                                return;
                            }
                            final BlockEntity tile = b.getLevel().getBlockEntity((Vector3)b);
                            if (!(tile instanceof ChestTile)) {
                                if (e.getItem() != null && e.getItem().getId() == 270) {
                                    if (u.hasPermission("24")) {
                                        if (!Cooldown.getInstance().has(p, "logblock")) {
                                            e.setCancelled();
                                            new LogblockInventory(p, g, b.getLocationHash());
                                            Cooldown.getInstance().add(p, "logblock", 10.0f);
                                        }
                                        else if (!Cooldown.getInstance().has(p, "logblockmsg")) {
                                            Util.sendMessage((CommandSender)p, Settings.getMessage("logblockcooldown").replace("{CD}", Util.formatTime(Cooldown.getInstance().get(p, "logblock") - System.currentTimeMillis())));
                                            Cooldown.getInstance().add(p, "logblockmsg", 1.0f);
                                        }
                                    }
                                    else {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "sprawdzania logblocka"));
                                    }
                                }
                                return;
                            }
                            if (!((ChestTile)tile).isLocked()) {
                                if (e.getItem() != null && e.getItem().getId() == 270) {
                                    if (u.hasPermission("24")) {
                                        if (!Cooldown.getInstance().has(p, "logblock")) {
                                            e.setCancelled();
                                            new LogblockInventory(p, g, b.getLocationHash());
                                            Cooldown.getInstance().add(p, "logblock", 10.0f);
                                        }
                                        else if (!Cooldown.getInstance().has(p, "logblockmsg")) {
                                            Util.sendMessage((CommandSender)p, Settings.getMessage("logblockcooldown").replace("{CD}", Util.formatTime(Cooldown.getInstance().get(p, "logblock") - System.currentTimeMillis())));
                                            Cooldown.getInstance().add(p, "logblockmsg", 1.0f);
                                        }
                                    }
                                    else {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "sprawdzania logblocka"));
                                    }
                                }
                                return;
                            }
                            if (((ChestTile)tile).getOwner().equalsIgnoreCase(p.getName())) {
                                if (e.getItem() != null && e.getItem().getId() == 270) {
                                    if (u.hasPermission("24")) {
                                        if (!Cooldown.getInstance().has(p, "logblock")) {
                                            e.setCancelled();
                                            new LogblockInventory(p, g, b.getLocationHash());
                                            Cooldown.getInstance().add(p, "logblock", 10.0f);
                                        }
                                        else if (!Cooldown.getInstance().has(p, "logblockmsg")) {
                                            Util.sendMessage((CommandSender)p, Settings.getMessage("logblockcooldown").replace("{CD}", Util.formatTime(Cooldown.getInstance().get(p, "logblock") - System.currentTimeMillis())));
                                            Cooldown.getInstance().add(p, "logblockmsg", 1.0f);
                                        }
                                    }
                                    else {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "sprawdzania logblocka"));
                                    }
                                }
                                return;
                            }
                            if (u.hasPermission("16")) {
                                if (e.getItem() != null && e.getItem().getId() == 270) {
                                    if (u.hasPermission("24")) {
                                        if (!Cooldown.getInstance().has(p, "logblock")) {
                                            e.setCancelled();
                                            new LogblockInventory(p, g, b.getLocationHash());
                                            Cooldown.getInstance().add(p, "logblock", 10.0f);
                                        }
                                        else if (!Cooldown.getInstance().has(p, "logblockmsg")) {
                                            Util.sendMessage((CommandSender)p, Settings.getMessage("logblockcooldown").replace("{CD}", Util.formatTime(Cooldown.getInstance().get(p, "logblock") - System.currentTimeMillis())));
                                            Cooldown.getInstance().add(p, "logblockmsg", 1.0f);
                                        }
                                    }
                                    else {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "sprawdzania logblocka"));
                                    }
                                }
                                return;
                            }
                            e.setCancelled();
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "otwierania zabezpieczonych skrzynek"));
                        }
                        else if (e.getItem() != null && e.getItem().getId() == 270) {
                            if (u.hasPermission("24")) {
                                if (!Cooldown.getInstance().has(p, "logblock")) {
                                    e.setCancelled();
                                    new LogblockInventory(p, g, b.getLocationHash());
                                    Cooldown.getInstance().add(p, "logblock", 10.0f);
                                }
                                else if (!Cooldown.getInstance().has(p, "logblockmsg")) {
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("logblockcooldown").replace("{CD}", Util.formatTime(Cooldown.getInstance().get(p, "logblock") - System.currentTimeMillis())));
                                    Cooldown.getInstance().add(p, "logblockmsg", 1.0f);
                                }
                            }
                            else {
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "sprawdzania logblocka"));
                            }
                        }
                    }
                }
                else {
                    e.setCancelled(true);
                    p.kick(Util.fixColor("&9not properly loaded user data - Interact"));
                }
            }
        }
    }
}
