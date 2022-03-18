package xyz.rokkiitt.sector.objects.entity.utils;

import cn.nukkit.math.*;
import java.util.*;

public class Node implements Comparable<Node>
{
    private Vector3 vector3;
    private Node parent;
    private int G;
    private int H;
    private int F;
    
    Node(final Vector3 vector3, final Node parent, final int G, final int H) {
        this.vector3 = vector3;
        this.parent = parent;
        this.G = G;
        this.H = H;
        this.F = G + H;
    }
    
    Node(final Vector3 vector3) {
        this(vector3, null, 0, 0);
    }
    
    public Node getParent() {
        return this.parent;
    }
    
    public void setParent(final Node parent) {
        this.parent = parent;
    }
    
    public int getG() {
        return this.G;
    }
    
    public void setG(final int g) {
        this.G = g;
    }
    
    public int getH() {
        return this.H;
    }
    
    public void setH(final int h) {
        this.H = h;
    }
    
    public int getF() {
        return this.F;
    }
    
    public void setF(final int f) {
        this.F = f;
    }
    
    @Override
    public int compareTo(final Node o) {
        Objects.requireNonNull(o);
        if (this.getF() != o.getF()) {
            return this.getF() - o.getF();
        }
        final double breaking;
        if ((breaking = this.getG() + this.getH() * 0.1 - (o.getG() + this.getH() * 0.1)) > 0.0) {
            return 1;
        }
        if (breaking < 0.0) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return this.vector3.toString() + "| G:" + this.G + " H:" + this.H + " F" + this.getF() + ((this.parent != null) ? ("\tparent:" + String.valueOf(this.parent.getVector3())) : "");
    }
    
    public Vector3 getVector3() {
        return this.vector3;
    }
    
    public void setVector3(final Vector3 vector3) {
        this.vector3 = vector3;
    }
}
