package xyz.rokkiitt.sector.objects;

public enum Perms
{
    VIP("vip"), 
    SVIP("svip"), 
    SPONSOR("sponsor"), 
    VIP_KIT("vip.kit"), 
    SVIP_KIT("svip.kit"), 
    SPONSOR_KIT("sponsor.kit"), 
    INFOADMIN("infoadmin"), 
    KITBYPASS("kitbypass"), 
    TPBYPASS("tpbypass"), 
    ANTYCHEAT("antycheat"), 
    CHATBYPASS("chatbypass"), 
    CRAFTINGBYPASS("craftingbypass"), 
    SPAWNBYPASS("spawnbypass"), 
    COMMANDBYPASS("commandbypass"), 
    PLACEBYPASS("placebypass"), 
    BREAKBYPASS("breakbypass"), 
    BUCKETBYPASS("bucketbypass"), 
    INTERACTBYPASS("interactbypass"), 
    GNOTIFYBYPASS("gnotifybypass"), 
    DEVICESEE("devicesee"), 
    CHESTSEE("chestsee"), 
    FREEZEBYPASS("freezebypass"), 
    INCOGNITOSEE("incognitosee"), 
    CMD_PERMSLIST("cmd.permslist"), 
    CMD_CMDLIST("cmd.cmdlist"), 
    CMD_MODIFY("cmd.modify"), 
    CMD_SAVE("cmd.save"), 
    CMD_GA("cmd.gadmin"), 
    CMD_ENCHANT("cmd.enchant"), 
    CMD_GIVE("cmd.give"), 
    CMD_FEED("cmd.feed"), 
    CMD_EFFECT("cmd.effect"), 
    CMD_GAMERULE("cmd.gamerule"), 
    CMD_DIFFICULTY("cmd.difficulty"), 
    CMD_GC("cmd.gc"), 
    CMD_SETSTAT("cmd.setstat"), 
    CMD_STATUS("cmd.status"), 
    CMD_TIMINGS("cmd.timings"), 
    CMD_GM("cmd.gm"), 
    CMD_WEATHER("cmd.weather"), 
    CMD_GM_ADMIN("cmd.gmadmin"), 
    CMD_GM_ALL("cmd.gmall"), 
    CMD_OP("cmd.op"), 
    CMD_STOP("cmd.stop"), 
    CMD_TP("cmd.tp"), 
    CMD_TURBODROP("cmd.turbodrop"), 
    CMD_EVENT("cmd.event"), 
    CMD_SETTINGS("cmd.settings"), 
    CMD_FLY("cmd.fly"), 
    CMD_SETGROUP("cmd.setgroup"), 
    CMD_MUTE("cmd.mute"), 
    CMD_SECTORS("cmd.sectors"), 
    CMD_PANDORA("cmd.pandora"), 
    CMD_GOD("cmd.god"), 
    CMD_SAFESEE("cmd.safesee"), 
    CMD_SAFEBACKUP("cmd.safebackup"), 
    CMD_BACKUP("cmd.backup"), 
    CMD_CHAT("cmd.chat"), 
    CMD_HEAL("cmd.heal"), 
    CMD_WINGS("cmd.wings"), 
    CMD_TOP("cmd.top"), 
    CMD_VANISH("cmd.vanish"), 
    CMD_INVSEE("cmd.invsee"), 
    CMD_PITEMS("cmd.pitems"), 
    CMD_RANDOMTP("cmd.randomtp"), 
    CMD_ENDERSEE("cmd.endersee"), 
    CMD_EC("cmd.enderchest"), 
    CMD_WB("cmd.workbench"), 
    CMD_DIRECTION("cmd.direction"), 
    CMD_REPAIR("cmd.repair"), 
    CMD_REPAIRALL("cmd.repairall"), 
    CMD_ALERTAC("cmd.alertac"), 
    CMD_TIME("cmd.time"), 
    CMD_KILL("cmd.kill"), 
    CMD_ALERT("cmd.alert"), 
    CMD_CLEAR("cmd.clear"), 
    CMD_KICK("cmd.kick"), 
    CMD_BAN("cmd.ban"), 
    CMD_IPMANAGE("cmd.ipmanage"), 
    CMD_KEYS("cmd.keys"), 
    CMD_SERVICES("cmd.servies"),
    CMDADMINPANEL("cmd.adminpanel"),
    CMD_WL("cmd.wl");

    private final String perms;

    Perms(String s) {
        this.perms = s;
    }

    public String getPermission() {
        return this.perms.toLowerCase();
    }
}
