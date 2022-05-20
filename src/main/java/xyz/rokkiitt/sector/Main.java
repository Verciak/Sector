package xyz.rokkiitt.sector;

import xyz.rokkiitt.sector.commands.*;
import xyz.rokkiitt.sector.commands.HelpCommand;
import xyz.rokkiitt.sector.commands.SpawnCommand;
import xyz.rokkiitt.sector.commands.admins.*;
import xyz.rokkiitt.sector.commands.admins.GamemodeCommand;
import xyz.rokkiitt.sector.commands.admins.KillCommand;
import xyz.rokkiitt.sector.commands.admins.StatusCommand;
import xyz.rokkiitt.sector.commands.admins.StopCommand;
import xyz.rokkiitt.sector.commands.admins.TimeCommand;
import xyz.rokkiitt.sector.commands.admins.WeatherCommand;
import xyz.rokkiitt.sector.commands.guild.*;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.database.mysql.MySQL;
import xyz.rokkiitt.sector.database.threads.DatabaseThread;
import xyz.rokkiitt.sector.database.threads.QueryThread;
import xyz.rokkiitt.sector.listeners.InventoryClickListener;
import xyz.rokkiitt.sector.objects.IncognitoInventory;
import xyz.rokkiitt.sector.objects.ac.EatModule;
import xyz.rokkiitt.sector.objects.ac.EspModule;
import xyz.rokkiitt.sector.objects.ac.PhaseModule;
import xyz.rokkiitt.sector.objects.ac.SpeedmineModule;
import xyz.rokkiitt.sector.objects.antigrief.AntiGrief;
import xyz.rokkiitt.sector.objects.enchants.CustomKnockback;
import xyz.rokkiitt.sector.objects.entity.Zombie;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.guild.GuildPanelGUI;
import xyz.rokkiitt.sector.objects.guild.entity.EntityHead;
import xyz.rokkiitt.sector.objects.guild.logblock.LogblockInventory;
import xyz.rokkiitt.sector.objects.inventory.FakeInventoriesListener;
import xyz.rokkiitt.sector.objects.itemshop.ItemshopInventory;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.modify.ModifyManager;
import xyz.rokkiitt.sector.objects.randomtp.RandomTPListener;
import xyz.rokkiitt.sector.objects.randomtp.RandomTPManager;
import xyz.rokkiitt.sector.objects.stonefarms.StoneManager;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.water.FakeWater;
import xyz.rokkiitt.sector.objects.water.WaterManager;
import xyz.rokkiitt.sector.objects.waypoint.Waypoint;
import xyz.rokkiitt.sector.tasks.*;
import xyz.rokkiitt.sector.utils.BlockFactory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.item.*;

import java.util.concurrent.*;
import cn.nukkit.level.*;
import cn.nukkit.entity.*;
import cn.nukkit.event.*;
import xyz.rokkiitt.sector.listeners.guild.*;
import xyz.rokkiitt.sector.listeners.*;
import cn.nukkit.plugin.*;
import xyz.rokkiitt.sector.objects.entity.projectile.*;
import xyz.rokkiitt.sector.objects.block.*;
import cn.nukkit.potion.*;
import cn.nukkit.item.food.*;
import cn.nukkit.item.enchantment.*;
import cn.nukkit.command.*;
import cn.nukkit.command.defaults.*;
import io.netty.util.collection.*;
import cn.nukkit.item.*;
import java.util.*;
import cn.nukkit.inventory.*;
import java.lang.reflect.*;

public class Main extends PluginBase
{
    private static Main ins;
    private static MySQL provider;
    private static DatabaseThread database;
    private static QueryThread query;
    public static boolean isStop;
    public static boolean saveOnStop;
    public static Set<GuildPanelGUI> panels;
    public static Set<IncognitoInventory> incognitos;
    public static Set<LogblockInventory> logs;
    public static Set<ItemshopInventory> items;
    private static long tntPerSecondTime;
    public static int tntPerSecond;
    
    public void onEnable() {
        Main.ins = this;
        this.saveDefaultConfig();
        this.getServer().setAutoSave(true);
        this.getServer().setMaxPlayers(500);
        this.getServer().setPropertyBoolean("achievements", false);
        this.getServer().setPropertyBoolean("announce-player-achievements", false);
        Config.loadOthers();
        Main.provider = new MySQL();
        Main.database = new DatabaseThread();
        Main.query = new QueryThread();
        Settings.set();
        this.registerRecipes();
        this.registerEnchants();
        this.registerEntity();
        this.registerBlocks();
        this.registerItems();
        this.registerCommands();
        this.registerTasks();
        this.registerListeners();
        this.registerManager();
    }
    
    public void onDisable() {
        MeteoriteManager.forceCancel();
        for (final Guild g : GuildManager.guilds) {
            g.saveRegeneration();
            g.saveLogblock();
        }
        for (final FakeWater water : WaterManager.getWaters().values()) {
            water.getLevel().setBlock(water, Block.get(0));
        }
        for (final Level level : Server.getInstance().getLevels().values()) {
            final Entity[] entities = level.getEntities();
            for (final Entity e : entities) {
                if (e instanceof EntityXPOrb || e instanceof EntityEnderman || e instanceof EntityCreeper || e instanceof EntityPrimedTNT) {
                    e.close();
                }
            }
        }
        for (final ConcurrentHashMap<String, String> s : AntiGrief.getGrief().values()) {
            final Location loc = new Location(Integer.parseInt(s.get("x")), Integer.parseInt(s.get("y")), Integer.parseInt(s.get("z")), Server.getInstance().getLevelByName(s.get("world")));
            final Block v = loc.getLevelBlock();
            if (AntiGrief.removeBlock(v)) {
                loc.getLevel().setBlock(loc, Block.get(0));
            }
        }
        Main.database.shutdown();
        Main.query.shutdown();
        try {
            Thread.sleep(5000L);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        Main.provider.close();
    }
    

    public static MySQL getProvider() {
        return Main.provider;
    }
    

    public static DatabaseThread getDatabase() {
        return Main.database;
    }
    
    public static QueryThread getQuery() {
        return Main.query;
    }
    
    private void registerListeners() {
        final long startTime = System.nanoTime();
        final PluginManager pm = this.getServer().getPluginManager();
        final List<Listener> list = Arrays.asList(
                new InventoryClickListener(),
                new KoxRefPeralListener(),
                new PlayerLoginListener(),
                new DataPacketEvents(),
                new FakeInventoriesListener(),
                new MovementListener(),
                new ExplodeListener(),
                new SpawnListener(),
                new BucketsListeners(),
                new BlockUpdateListener(),
                new InteractListener(),
                new BreakListener(),
                new PlaceListener(),
                new PrePlayerCommandListener(),
                new PlayerInteractListeners(),
                new MoveHeartListener(),
                new PlayerTeleportListener(),
                new ChatListener(),
                new CombatListener(),
                new DropBreakListener(),
                new RepairItemListener(),
                new CraftingListener(),
                new InventoryOpenListener(),
                new InventoryPickupItemListener(),
                new SignChangeListener(),
                new RandomTPListener(),
                new ServerStopListener(),
                new InventoryClickListener(),
                new NetherListener(),
                new EatModule(),
                new SpeedmineModule(),
                new PhaseModule());
        for (final Listener l : list) {
            pm.registerEvents(l, this);
        }
        final long endTime = System.nanoTime();
        this.getLogger().info("Loaded {D} listeners in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)).replace("{D}", String.valueOf(list.size())));
    }
    
    private void registerEntity() {
        final long startTime = System.nanoTime();
        Entity.registerEntity(Zombie.class.getSimpleName(), Zombie.class);
        Entity.registerEntity("EntityHead", EntityHead.class);
        Entity.registerEntity("Waypoint", Waypoint.class);
        Entity.registerEntity(Snowball.class.getSimpleName(), Snowball.class);
        Entity.registerEntity("PrimedTnt", PrimedTNT.class);
        Entity.registerEntity(Egg.class.getSimpleName(), Egg.class);
        Entity.registerEntity(EnderPearl.class.getSimpleName(), EnderPearl.class);
        Entity.registerEntity("Arrow", Arrow.class);
//        BlockEntity.registerBlockEntity("Chest", ChestTile.class);
//        BlockEntity.registerBlockEntity("Hopper", FixedHopperTile.class);
        final long endTime = System.nanoTime();
        this.getLogger().info("Registered 12 entities in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
    }
    
    private void registerBlocks() {
        final long startTime = System.nanoTime();
        BlockFactory.registerBlock(46, TNT.class);
        BlockFactory.registerBlock(165, Slime.class);
        BlockFactory.registerBlock(12, FixedSand.class);
        BlockFactory.registerBlock(13, FixedGravel.class);
        BlockFactory.registerBlock(154, FixedHopper.class);
        final long endTime = System.nanoTime();
        this.getLogger().info("Registered 6 blocks in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
    }
    
    private void registerItems() {
        final long startTime = System.nanoTime();
        Food.registerFood(new FoodEffective(4, 9.6f).addEffect(Effect.getEffect(10).setAmplifier(3).setDuration(130)).addEffect(Effect.getEffect(22).setAmplifier(1).setDuration(1200)).addRelative(322), this);
        Food.registerFood(new FoodEffective(4, 9.6f).addEffect(Effect.getEffect(10).setAmplifier(4).setDuration(600)).addEffect(Effect.getEffect(22).setDuration(2400).setAmplifier(1)).addEffect(Effect.getEffect(11).setDuration(4800)).addEffect(Effect.getEffect(12).setDuration(4800)).addRelative(466), this);
        final long endTime = System.nanoTime();
        this.getLogger().info("Registered 2 items in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
    }
    
    private void registerEnchants() {
        final long startTime = System.nanoTime();
        this.registerEnchant(new CustomKnockback());
        final long endTime = System.nanoTime();
        this.getLogger().info("Registered 1 enchant in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
    }
    
    private void registerManager() {
        final long startTime = System.nanoTime();
        StoneManager.enable();
        RandomTPManager.enable();
        ModifyManager.load();
        UserManager.loadUsers();
        GuildManager.loadGuilds();
        final long endTime = System.nanoTime();
        this.getLogger().info("Loaded 3 managers in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
    }
    
    private void registerCommands() {
        final long startTime = System.nanoTime();
        List<Command> cmd = Arrays.asList(new GuildInfoCommand(),new GuildFFACommand(), new GuildExpandCommand(), new GuildCommand(), new GuildDeleteCommand(), new TestCommand(), new AdminPanelCommand(), new GuildHomeCommand(), new GuildCreateCommand(), new GuildWarCommand(), new ToprankCommand(), new CmdlistCommand(), new DirectionCommand(), new TestCommand(), new GuildRegenerationCommand(), new GuildTreasureCommand(), new TrashCommand(), new PItemsCommand(), new ClearCommand(),
                new GodCommand(), new FlyCommand(), new BossCommand(), new VanishCommand(), new DropCommand(), new DepositCommand(), new KitCommand(), new HealCommand(), new FeedCommand(), new SpawnCommand(), new CxCommand(),
                new HelpCommand(), new VipCommand(), new SvipCommand(), new SponsorCommand(), new YouTubeCommand(), new TnTCommand(), new StatuteCommand(), new AlertCommand(), new DescriptionCommand(), new RepairCommand(),
                new TopCommand(), new EffectsCommand(), new HelpopCommand(), new ResetRankingCommand(), new EnderCommand(), new BlocksCommand(), new IgnoreCommand(), new UnIgnoreCommand(), new IgnoreListCommand(), new CraftingsCommand(),
                new InvseeCommand(), new EnderseeCommand(), new AlertACCommand(), new PermsListCommand(), new RandomTpCommand(), new WingsCommand(), new StopCommand(), new GcCommand(), new TimingsCommand("timings"),
                new StatusCommand(), new KillCommand(), new TimeCommand(), new GamemodeCommand(), new WeatherCommand(), new DifficultyCommand("difficulty"),
                new ModifyCommand(), new HomeCommand(), new SetHomeCommand(), new DelHomeCommand(), new WaypointCommand(),
                new ItemshopCommand(),new SetGroupCommand(), new WorkbenchCommand(), new TradeCommand(), new GuildCollectionCommand());
        for (Command c : cmd){
            ServerCommand.registerCommand(c);
    }
        for (final Command cmdsss : this.getServer().getCommandMap().getCommands().values()) {
            cmdsss.setPermissionMessage(Util.fixColor(Settings.getMessage("commandpermission").replace("{PERM}", cmdsss.getPermission())));
        }
        final long endTime = System.nanoTime();
        this.getLogger().info("Loaded "+cmd.size()+" commands in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));

    }
    
    private void registerTasks() {
        final long startTime = System.nanoTime();
        this.getServer().getScheduler().scheduleRepeatingTask(this, new HalfSecTask(), 10, true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new ThreeTickTask(), 3, true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new SecondTask(), 20, false);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new ThirtySecondsTask(), 600, true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new SaveDataTask(), 3000, true);
        this.getServer().getScheduler().scheduleRepeatingTask(this, new EspModule(), 10, true);
        final long endTime = System.nanoTime();
        this.getLogger().info("Loaded 6 tasks in {S}ms".replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
    }
    
    private void registerRecipes() {
        final Map<Character, Item> ingredients = new CharObjectHashMap<Item>();
        ingredients.put('A', Item.get(41));
        ingredients.put('B', Item.get(260));
        Server.getInstance().getCraftingManager().registerRecipe(419 , new ShapedRecipe(Item.get(466), new String[] { "AAA", "ABA", "AAA" }, ingredients, new ArrayList()));
        ingredients.clear();
        ingredients.put('A', Item.get(49));
        ingredients.put('B', Item.get(368));
        Server.getInstance().getCraftingManager().registerRecipe(419 , new ShapedRecipe(Item.get(130), new String[] { "AAA", "ABA", "AAA" }, ingredients, new ArrayList()));
        ingredients.clear();
        ingredients.put('A', Item.get(20));
        ingredients.put('B', Item.get(397, 1));
        ingredients.put('C', Item.get(49));
        Server.getInstance().getCraftingManager().registerRecipe(419 , new ShapedRecipe(Item.get(138), new String[] { "AAA", "BBB", "CCC" }, ingredients, new ArrayList()));
        Server.getInstance().getCraftingManager().rebuildPacket();
    }
    
    public void registerEnchant(final Enchantment customEnchant) {
        try {
            final Field enchants = Enchantment.class.getDeclaredField("enchantments");
            enchants.setAccessible(true);
            final Enchantment[] enchantments = (Enchantment[])enchants.get(null);
            enchantments[customEnchant.getId()] = customEnchant;
            enchants.set(null, enchantments);
            enchants.setAccessible(false);
        }
        catch (Exception exception) {
            this.getServer().getLogger().warning("Couldn't load enchantment " + customEnchant.getName(), exception);
        }
    }
    
    public static boolean tntLimit() {
        if (Main.tntPerSecondTime < System.currentTimeMillis()) {
            Main.tntPerSecondTime = System.currentTimeMillis() + 1000L;
            Main.tntPerSecond = 0;
        }
        return ++Main.tntPerSecond >= 20;
    }
    
    public static Main getPlugin() {
        return Main.ins;
    }
    
    static {
        Main.isStop = true;
        Main.saveOnStop = false;
        Main.panels = ConcurrentHashMap.newKeySet();
        Main.incognitos = ConcurrentHashMap.newKeySet();
        Main.logs = ConcurrentHashMap.newKeySet();
        Main.items = ConcurrentHashMap.newKeySet();
    }
}
