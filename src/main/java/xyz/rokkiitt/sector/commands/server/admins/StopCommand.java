package xyz.rokkiitt.sector.commands.server.admins;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.Task;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.combat.CombatManager;

public class StopCommand extends ServerCommand
{
    public StopCommand() {
        super("stop", "stop", "/stop", Perms.CMD_STOP.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        Main.isStop = true;
        Main.saveOnStop = true;
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            public void onRun(final int currentTick) {
                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                    p.getInventory().close(p);
                    Cooldown.getInstance().add(p, "restart", 20.0f);
                    p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 5s"));
                }
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(final int currentTick) {
                        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                            p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 4s"));
                        }
                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                            public void onRun(final int currentTick) {
                                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                    p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 3s"));
                                }
                                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                    public void onRun(final int currentTick) {
                                        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                            p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 2s"));
                                        }
                                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                            public void onRun(final int currentTick) {
                                                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                                    p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 1s"));
                                                }
                                                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                                    public void onRun(final int currentTick) {
                                                        CombatManager.clear();
                                                        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                                            Util.kickPlayer(p, "&cTRWA RESTART TWOJEGO SEKTORA");
                                                        }
                                                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                                            public void onRun(final int currentTick) {
                                                                Server.getInstance().shutdown();
                                                            }
                                                        }, 200);
                                                    }
                                                }, 40);
                                            }
                                        }, 40);
                                    }
                                }, 40);
                            }
                        }, 40);
                    }
                }, 40);
            }
        }, 20);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        Main.isStop = true;
        Main.saveOnStop = true;
//        Main.getNats().publish("serverstatus", Config.getInstance().Sector + "||stop");
        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
            public void onRun(final int currentTick) {
                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                    p.getInventory().close(p);
                    Cooldown.getInstance().add(p, "restart", 20.0f);
                    p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 5s"));
                }
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(final int currentTick) {
                        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                            p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 4s"));
                        }
                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                            public void onRun(final int currentTick) {
                                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                    p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 3s"));
                                }
                                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                    public void onRun(final int currentTick) {
                                        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                            p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 2s"));
                                        }
                                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                            public void onRun(final int currentTick) {
                                                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                                    p.sendTitle(Util.fixColor("&4RESTART"), Util.fixColor("&cRestart sektora odbedzie sie za 1s"));
                                                }
                                                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                                    public void onRun(final int currentTick) {
                                                        CombatManager.clear();
                                                        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
                                                            Util.kickPlayer(p, "&cTRWA RESTART TWOJEGO SEKTORA");
                                                        }
                                                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                                            public void onRun(final int currentTick) {
                                                                Server.getInstance().shutdown();
                                                            }
                                                        }, 200);
                                                    }
                                                }, 40);
                                            }
                                        }, 40);
                                    }
                                }, 40);
                            }
                        }, 40);
                    }
                }, 40);
            }
        }, 20);
        return false;
    }
}
