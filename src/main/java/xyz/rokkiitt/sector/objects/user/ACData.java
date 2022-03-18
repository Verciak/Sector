package xyz.rokkiitt.sector.objects.user;

public class ACData
{
    private long speedminePerSecondTime;
    public int speedminePerSecond;
    private long entityPerSecondTime;
    public int entityPerSecond;
    
    public boolean speedmineLimit(final boolean isHalf) {
        if (this.speedminePerSecondTime < System.currentTimeMillis()) {
            this.speedminePerSecondTime = System.currentTimeMillis() + (isHalf ? 500L : 1000L);
            this.speedminePerSecond = 0;
        }
        return ++this.speedminePerSecond >= (isHalf ? 4 : 10);
    }
    
    public boolean entityLimit() {
        if (this.entityPerSecondTime < System.currentTimeMillis()) {
            this.entityPerSecondTime = System.currentTimeMillis() + 1000L;
            this.entityPerSecond = 0;
        }
        return ++this.entityPerSecond >= 60;
    }
}
