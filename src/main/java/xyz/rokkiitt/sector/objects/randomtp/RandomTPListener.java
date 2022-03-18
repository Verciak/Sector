package xyz.rokkiitt.sector.objects.randomtp;

import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.level.*;
import java.util.*;
import cn.nukkit.event.*;

public class RandomTPListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onInteract(final PlayerInteractEvent e) {
        final Block b = e.getBlock();
        final Player p = e.getPlayer();
        if (e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && Config.isSpawn(p.getLocation()) && b instanceof BlockButton) {
            final RandomTP rtp = RandomTPManager.getTeleport(b.getLocation());
            if (rtp != null) {
                final RandomTPStatus d = RandomTPManager.getStatus(p.getUniqueId());
                if (d != null && d.equals(RandomTPStatus.DELETE)) {
                    RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.STOP);
                    RandomTPManager.delete(rtp);
                    final Location l = b.getLocation();
                    Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpdelete").replace("{XYZ}", l.getFloorX() + ":" + l.getFloorY() + ":" + l.getFloorZ()));
                    return;
                }
                if (rtp.getStatus().equals(RandomTPStatus.SOLO)) {

                }
                else if (rtp.getStatus().equals(RandomTPStatus.MAX2)) {
                    int s = 0;
                    final List<String> players = new ArrayList<String>();
                    players.add(p.getName());
                    for (final Player en : Util.getNearbyPlayers(rtp.getLocation(), 5)) {
                        if (s == 2) {
                            break;
                        }
                        if (en == p) {
                            continue;
                        }
                        else {
                            if (!(en.getLocation().getLevelBlock() instanceof BlockPressurePlateBase)) {
                                continue;
                            }
                            ++s;
                            players.add(en.getName());
                        }
                    }
                    if (players.size() > 1) {
//                        final PacketRandomTeleport pa2 = new PacketRandomTeleport();
//                        pa2.nickname = p.getName();
//                        pa2.currentsector = Config.getInstance().Sector;
//                        pa2.type = 1;
//                        pa2.x = 0;
//                        pa2.z = 0;
//                        pa2.destinationsector = "";
//                        pa2.players = players;
//                        pa2.succes = false;
//                        pa2.reason = "";
//                        Main.getNats().publish("hubguildpanel", "randomtp||" + JsonStream.serialize(pa2));
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpcantsolo"));
                    }
                }
                else if (rtp.getStatus().equals(RandomTPStatus.GROUP)) {
                    final List<String> players2 = new ArrayList<String>();
                    players2.add(p.getName());
                    for (final Player en2 : Util.getNearbyPlayers(rtp.getLocation(), 5)) {
                        if (en2 != p) {
                                if (!(en2.getLocation().getLevelBlock() instanceof BlockPressurePlateBase)) {
                                    continue;
                                }
                                players2.add(en2.getName());
                        }
                    }
                    if (players2.size() > 1) {
//                        final PacketRandomTeleport pa3 = new PacketRandomTeleport();
//                        pa3.nickname = p.getName();
//                        pa3.currentsector = Config.getInstance().Sector;
//                        pa3.type = 1;
//                        pa3.x = 0;
//                        pa3.z = 0;
//                        pa3.destinationsector = "";
//                        pa3.players = players2;
//                        pa3.succes = false;
//                        pa3.reason = "";
//                        Main.getNats().publish("hubguildpanel", "randomtp||" + JsonStream.serialize(pa3));
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpcantsolo"));
                    }
                }
                else {
                    RandomTPManager.delete(rtp);
                }
            }
            else {
                final RandomTPStatus d = RandomTPManager.getStatus(p.getUniqueId());
                if (d != null) {
                    final Location l = b.getLocation();
                    if (d.equals(RandomTPStatus.SOLO)) {
                        RandomTPManager.create(b.getLocation(), RandomTPStatus.SOLO);
                        RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.STOP);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpsolo").replace("{XYZ}", l.getFloorX() + ":" + l.getFloorY() + ":" + l.getFloorZ()));
                    }
                    else if (d.equals(RandomTPStatus.MAX2)) {
                        RandomTPManager.create(b.getLocation(), RandomTPStatus.MAX2);
                        RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.STOP);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpmax2").replace("{XYZ}", l.getFloorX() + ":" + l.getFloorY() + ":" + l.getFloorZ()));
                    }
                    else if (d.equals(RandomTPStatus.GROUP)) {
                        RandomTPManager.create(b.getLocation(), RandomTPStatus.GROUP);
                        RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.STOP);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpgroup").replace("{XYZ}", l.getFloorX() + ":" + l.getFloorY() + ":" + l.getFloorZ()));
                    }
                }
            }
        }
    }
}
