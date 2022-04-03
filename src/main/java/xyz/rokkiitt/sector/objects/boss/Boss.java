package xyz.rokkiitt.sector.objects.boss;

import cn.nukkit.Player;

public class Boss {

    private String author;
    private boolean boss;
    private boolean perch;

    public String getAuthor() {
        return author;
    }

    public boolean isBoss() {
        return boss;
    }

    public boolean isPerch() {
        return perch;
    }


    public void setPerch(boolean trading) {
        perch = trading;
    }

    public void setBoss(boolean trading) {
        boss = trading;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boss(Player player) {
        this.author = player.getName();
        this.boss = false;
        this.perch = false;
    }

}
