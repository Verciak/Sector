package xyz.rokkiitt.sector.commands;

import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.objects.Top;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.commands.PacketToprankCommand;

import java.util.*;

import com.jsoniter.*;
import cn.nukkit.*;

public class ToprankCommand extends SectorCommand
{
    public ToprankCommand() {
        super("topka", "pokazuje topki graczy", "/list", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
//        MusicControllerApi.play(p, "song.waitgame");
        final PacketToprankCommand pa = new PacketToprankCommand();
        pa.sender = p.getName();
        pa.assist = new ArrayList<String>();
        pa.kills = new ArrayList<String>();
        pa.deaths = new ArrayList<String>();
        pa.broken = new ArrayList<String>();
        pa.guild = new ArrayList<String>();
        pa.points = new ArrayList<String>();
        pa.time = new ArrayList<String>();
        pa.eatKox = new ArrayList<String>();
        pa.eatRef = new ArrayList<String>();
        pa.throwPearl = new ArrayList<String>();
        for (Guild g : GuildManager.guilds){
            pa.guild.add(g.getTag() + "|&|0");
        }
        for (User u : UserManager.users){
            pa.assist.add(u.getNickname() + "|&|" + u.getAssist());
            pa.kills.add(u.getNickname() + "|&|" + u.getKills());
            pa.deaths.add(u.getNickname() + "|&|" + u.getDeaths());
            pa.broken.add(u.getNickname() + "|&|" + u.getBroken());
            pa.points.add(u.getNickname() + "|&|" + u.getPoints());
            pa.time.add(u.getNickname() + "|&|" + u.getOnlineoverall());
            pa.eatKox.add(u.getNickname() + "|&|" + u.getEat_kox());
            pa.eatRef.add(u.getNickname() + "|&|" + u.getEat_ref());
            pa.throwPearl.add(u.getNickname() + "|&|" + u.getThrow_pearl());
        }

        new Top(p, pa);
        return false;
    }
    

    public boolean onCallback(final String s) {
        final PacketToprankCommand pa = JsonIterator.deserialize(s, PacketToprankCommand.class);
            final Player p = Server.getInstance().getPlayerExact(pa.sender.toLowerCase());
            if (p != null && p.isOnline()) {
                new Top(p, pa);
            }
        return false;
    }
}
