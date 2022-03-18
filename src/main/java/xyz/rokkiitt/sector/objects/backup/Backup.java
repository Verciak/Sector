package xyz.rokkiitt.sector.objects.backup;

import java.util.*;
import cn.nukkit.item.*;

public class Backup
{
    public int id;
    public String player;
    public String killer;
    public int points;
    public long date;
    public Map<Integer, Item> inventory;
}
