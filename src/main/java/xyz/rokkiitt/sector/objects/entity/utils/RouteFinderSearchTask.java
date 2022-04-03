package xyz.rokkiitt.sector.objects.entity.utils;

public class RouteFinderSearchTask implements Runnable {
    private RouteFinder route;

    private int retryTime;

    public RouteFinderSearchTask(RouteFinder route) {
        this.retryTime = 0;
        this.route = route;
    }

    public void run() {
        if (this.route == null)
            return;
        while (this.retryTime < 50) {
            if (!this.route.isSearching()) {
                this.route.research();
                return;
            }
            this.retryTime += 10;
            try {
                Thread.sleep(100L);
            } catch (InterruptedException interruptedException) {}
        }
        this.route.interrupt();
    }
}
