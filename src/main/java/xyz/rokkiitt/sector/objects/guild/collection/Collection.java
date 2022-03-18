package xyz.rokkiitt.sector.objects.guild.collection;

import java.util.concurrent.*;

import java.util.*;

public class Collection
{
    public int type;
    public Set<CollectionData> participants;
    public int toReceive;
    
    public Collection() {
        this.participants = ConcurrentHashMap.newKeySet();
    }
    
    public void removeAmount(final String who, final int x) {
        this.toReceive -= x;
        boolean wasAdded = false;
        for (final CollectionData cd : this.participants) {
            if (cd.who.equalsIgnoreCase(who)) {
                final CollectionData collectionData = cd;
                collectionData.out += x;
                wasAdded = true;
                break;
            }
        }
        if (!wasAdded) {
            final CollectionData cd2 = new CollectionData();
            cd2.who = who;
            cd2.in = 0;
            cd2.out = x;
            this.participants.add(cd2);
        }
    }
    
    public void addAmount(final String who, final int x) {
        this.toReceive += x;
        boolean wasAdded = false;
        for (final CollectionData cd : this.participants) {
            if (cd.who.equalsIgnoreCase(who)) {
                final CollectionData collectionData = cd;
                collectionData.in += x;
                wasAdded = true;
                break;
            }
        }
        if (!wasAdded) {
            final CollectionData cd2 = new CollectionData();
            cd2.who = who;
            cd2.in = x;
            cd2.out = 0;
            this.participants.add(cd2);
        }
    }
    
    public static String serialize(final Set<Collection> collections) {
        if (collections.isEmpty()) {
            collections.addAll(getDeafults());
        }
        final List<String> cc = new ArrayList<String>();
        for (final Collection b : collections) {
            final List<String> map = new ArrayList<String>();
            for (final CollectionData n : b.participants) {
                map.add(n.who + "|&|" + n.in + "|&|" + n.out);
            }
//            cc.add(b.type + "||" + b.toReceive + "||" + JsonStream.serialize(map));
        }
//        return cc.isEmpty() ? "brak" : Util.StringToString(JsonStream.serialize(cc));
        return null;
    }
    
    public static Set<Collection> deserialize(final String x) {
        final Set<Collection> collections = ConcurrentHashMap.newKeySet();
//        final List<String> cc = JsonIterator.deserialize(Util.StringFromString(x), (Class<List<String>>)List.class);
//        for (final String b : cc) {
//            final String[] split = b.split(Pattern.quote("||"));
//            if (split.length >= 3) {
//                final Collection collection = new Collection();
//                collection.type = Integer.valueOf(split[0]);
//                collection.toReceive = Integer.valueOf(split[1]);
//                final List<String> es = JsonIterator.deserialize(split[2], (Class<List<String>>)List.class);
//                for (final String z : es) {
//                    final String[] spl = z.split(Pattern.quote("|&|"));
//                    if (spl.length >= 3) {
//                        final CollectionData cd = new CollectionData();
//                        cd.who = spl[0];
//                        cd.in = Integer.valueOf(spl[1]);
//                        cd.out = Integer.valueOf(spl[2]);
//                        collection.participants.add(cd);
//                    }
//                }
//                collections.add(collection);
//            }
//        }
        return collections;
    }
    
    public static Set<Collection> getDeafults() {
        final Set<Collection> collections = ConcurrentHashMap.newKeySet();
        final Collection c1 = new Collection();
        c1.type = 1;
        c1.toReceive = 0;
        final Collection c2 = new Collection();
        c2.type = 2;
        c2.toReceive = 0;
        final Collection c3 = new Collection();
        c3.type = 3;
        c3.toReceive = 0;
        final Collection c4 = new Collection();
        c4.type = 4;
        c4.toReceive = 0;
        final Collection c5 = new Collection();
        c5.type = 5;
        c5.toReceive = 0;
        final Collection c6 = new Collection();
        c6.type = 6;
        c6.toReceive = 0;
        final Collection c7 = new Collection();
        c7.type = 7;
        c7.toReceive = 0;
        final Collection c8 = new Collection();
        c8.type = 8;
        c8.toReceive = 0;
        final Collection c9 = new Collection();
        c9.type = 9;
        c9.toReceive = 0;
        collections.addAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9));
        return collections;
    }
}
