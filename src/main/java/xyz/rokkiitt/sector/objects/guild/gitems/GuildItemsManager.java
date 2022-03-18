package xyz.rokkiitt.sector.objects.guild.gitems;

import xyz.rokkiitt.sector.packets.serializedObjects.SerializedKitItem;

import java.util.*;

public class GuildItemsManager
{
    private static final List<GuildItems> items;
    
    public static List<GuildItems> getItems() {
        return GuildItemsManager.items;
    }
    
    public static void load(final String p) {
        GuildItemsManager.items.clear();
        if (!p.equalsIgnoreCase("brak")) {
//            final List<String> datastring = JsonIterator.deserialize(p, (Class<List<String>>)List.class);
            final SerializedKitItem drop;
//            datastring.forEach(s -> {
//                drop = JsonIterator.deserialize(s, SerializedKitItem.class);
//                GuildItemsManager.items.add(new GuildItems(ItemSerializer.getItemFromSerializedItem(drop.what), drop.guislot, drop.amount));
//            });
        }
    }
    
    static {
        items = new ArrayList<GuildItems>();
    }
}
