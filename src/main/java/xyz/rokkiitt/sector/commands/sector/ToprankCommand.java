package xyz.rokkiitt.sector.commands.sector;

import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.objects.Top;
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
        final PacketToprankCommand pa = new PacketToprankCommand();
        pa.sender = p.getName();
        pa.assist = new ArrayList<String>();
        pa.kills = new ArrayList<String>();
        pa.deaths = new ArrayList<String>();
        pa.broken = new ArrayList<String>();
        pa.guild = new ArrayList<String>();
        pa.points = new ArrayList<String>();
        pa.time = new ArrayList<String>();
        new Top(p, pa);
        return false;
    }
    
    @Override
    public boolean onCallback(final String s) {
        final PacketToprankCommand pa = JsonIterator.deserialize(s, PacketToprankCommand.class);
            final Player p = Server.getInstance().getPlayerExact(pa.sender.toLowerCase());
            if (p != null && p.isOnline()) {
                new Top(p, pa);
            }
        return false;
    }
}
