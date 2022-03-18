package xyz.rokkiitt.sector.objects.entity.utils;

import xyz.rokkiitt.sector.objects.entity.WalkingEntity;
import cn.nukkit.block.*;
import cn.nukkit.math.*;
import java.util.*;

public class WalkerRouteFinder extends SimpleRouteFinder
{
    private final PriorityQueue<Node> openList;
    private final ArrayList<Node> closeList;
    private int searchLimit;
    
    public WalkerRouteFinder(final WalkingEntity entity) {
        super(entity);
        this.openList = new PriorityQueue<Node>();
        this.closeList = new ArrayList<Node>();
        this.searchLimit = 100;
        this.level = entity.getLevel();
    }
    
    public WalkerRouteFinder(final WalkingEntity entity, final Vector3 start) {
        super(entity);
        this.openList = new PriorityQueue<Node>();
        this.closeList = new ArrayList<Node>();
        this.searchLimit = 100;
        this.level = entity.getLevel();
        this.start = start;
    }
    
    public WalkerRouteFinder(final WalkingEntity entity, final Vector3 start, final Vector3 destination) {
        super(entity);
        this.openList = new PriorityQueue<Node>();
        this.closeList = new ArrayList<Node>();
        this.searchLimit = 100;
        this.level = entity.getLevel();
        this.start = start;
        this.destination = destination;
    }
    
    private int calHeuristic(final Vector3 pos1, final Vector3 pos2) {
        return 10 * (Math.abs(pos1.getFloorX() - pos2.getFloorX()) + Math.abs(pos1.getFloorZ() - pos2.getFloorZ())) + 11 * Math.abs(pos1.getFloorY() - pos2.getFloorY());
    }
    
    @Override
    public boolean search() {
        this.finished = false;
        this.searching = true;
        if (this.start == null) {
            this.start = (Vector3)this.entity;
        }
        if (this.destination == null) {
            final Vector3 vec = this.entity.getTargetVector();
            if (vec == null) {
                this.searching = false;
                this.finished = true;
                return false;
            }
            this.destination = vec;
        }
        this.resetTemporary();
        Node presentNode = new Node(this.start);
        this.closeList.add(new Node(this.start));
        while (!this.isPositionOverlap(presentNode.getVector3(), this.destination)) {
            if (this.isInterrupted()) {
                this.searchLimit = 0;
                this.searching = false;
                this.finished = true;
                return false;
            }
            this.putNeighborNodeIntoOpen(presentNode);
            if (this.openList.peek() == null || this.searchLimit-- <= 0) {
                this.searching = false;
                this.finished = true;
                this.reachable = false;
                this.addNode(new Node(this.destination));
                return false;
            }
            this.closeList.add(presentNode = this.openList.poll());
        }
        if (!presentNode.getVector3().equals((Object)this.destination)) {
            this.closeList.add(new Node(this.destination, presentNode, 0, 0));
        }
        ArrayList<Node> findingPath = this.getPathRoute();
        findingPath = this.FloydSmooth(findingPath);
        this.resetNodes();
        this.addNode(findingPath);
        this.finished = true;
        this.searching = false;
        return true;
    }
    
    private Block getHighestUnder(final Vector3 vector3, final int limit) {
        if (limit > 0) {
            for (int y = vector3.getFloorY(); y >= vector3.getFloorY() - limit; --y) {
                final Block block = this.level.getBlock(vector3.getFloorX(), y, vector3.getFloorZ(), false);
                if (this.isWalkable((Vector3)block) && this.level.getBlock((Vector3)block.add(0.0, 1.0, 0.0), false).getId() == 0) {
                    return block;
                }
            }
            return null;
        }
        for (int y = vector3.getFloorY(); y >= 0; --y) {
            final Block block = this.level.getBlock(vector3.getFloorX(), y, vector3.getFloorZ(), false);
            if (this.isWalkable((Vector3)block) && this.level.getBlock((Vector3)block.add(0.0, 1.0, 0.0), false).getId() == 0) {
                return block;
            }
        }
        return null;
    }
    
    private boolean canWalkOn(final Block block) {
        return block.getId() != 10 && block.getId() != 11 && block.getId() != 81;
    }
    
    private boolean isWalkable(final Vector3 vector3) {
        final Block block = this.level.getBlock(vector3, false);
        return !block.canPassThrough() && this.canWalkOn(block);
    }
    
    private boolean isPassable(final Vector3 vector3) {
        final double radius = this.entity.getWidth() * this.entity.getScale() / 2.0f;
        final float height = this.entity.getHeight() * this.entity.getScale();
        final AxisAlignedBB bb = (AxisAlignedBB)new SimpleAxisAlignedBB(vector3.getX() - radius, vector3.getY(), vector3.getZ() - radius, vector3.getX() + radius, vector3.getY() + height, vector3.getZ() + radius);
        return !Utils.hasCollisionBlocks(this.level, bb) && !this.level.getBlock(vector3.add(0.0, -1.0, 0.0), false).canPassThrough();
    }
    
    private int getWalkableHorizontalOffset(final Vector3 vector3) {
        final Block block = this.getHighestUnder(vector3, 4);
        if (block != null) {
            return block.getFloorY() - vector3.getFloorY() + 1;
        }
        return -256;
    }
    
    public int getSearchLimit() {
        return this.searchLimit;
    }
    
    public void setSearchLimit(final int limit) {
        this.searchLimit = limit;
    }
    
    private void putNeighborNodeIntoOpen(final Node node) {
        final Vector3 vector3 = new Vector3(node.getVector3().getFloorX() + 0.5, node.getVector3().getY(), node.getVector3().getFloorZ() + 0.5);
        double y;
        final boolean E;
        if (E = ((y = this.getWalkableHorizontalOffset(vector3.add(1.0, 0.0, 0.0))) != -256.0)) {
            final Vector3 vec = vector3.add(1.0, y, 0.0);
            if (this.isPassable(vec) && !this.isContainsInClose(vec)) {
                final Node nodeNear = this.getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList.offer(new Node(vec, node, 10 + node.getG(), this.calHeuristic(vec, this.destination)));
                }
                else if (node.getG() + 10 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 10);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        final boolean S;
        if (S = ((y = this.getWalkableHorizontalOffset(vector3.add(0.0, 0.0, 1.0))) != -256.0)) {
            final Vector3 vec2 = vector3.add(0.0, y, 1.0);
            if (this.isPassable(vec2) && !this.isContainsInClose(vec2)) {
                final Node nodeNear2 = this.getNodeInOpenByVector2(vec2);
                if (nodeNear2 == null) {
                    this.openList.offer(new Node(vec2, node, 10 + node.getG(), this.calHeuristic(vec2, this.destination)));
                }
                else if (node.getG() + 10 < nodeNear2.getG()) {
                    nodeNear2.setParent(node);
                    nodeNear2.setG(node.getG() + 10);
                    nodeNear2.setF(nodeNear2.getG() + nodeNear2.getH());
                }
            }
        }
        final boolean W;
        if (W = ((y = this.getWalkableHorizontalOffset(vector3.add(-1.0, 0.0, 0.0))) != -256.0)) {
            final Vector3 vec3 = vector3.add(-1.0, y, 0.0);
            if (this.isPassable(vec3) && !this.isContainsInClose(vec3)) {
                final Node nodeNear3 = this.getNodeInOpenByVector2(vec3);
                if (nodeNear3 == null) {
                    this.openList.offer(new Node(vec3, node, 10 + node.getG(), this.calHeuristic(vec3, this.destination)));
                }
                else if (node.getG() + 10 < nodeNear3.getG()) {
                    nodeNear3.setParent(node);
                    nodeNear3.setG(node.getG() + 10);
                    nodeNear3.setF(nodeNear3.getG() + nodeNear3.getH());
                }
            }
        }
        final boolean N;
        if (N = ((y = this.getWalkableHorizontalOffset(vector3.add(0.0, 0.0, -1.0))) != -256.0)) {
            final Vector3 vec4 = vector3.add(0.0, y, -1.0);
            if (this.isPassable(vec4) && !this.isContainsInClose(vec4)) {
                final Node nodeNear4 = this.getNodeInOpenByVector2(vec4);
                if (nodeNear4 == null) {
                    this.openList.offer(new Node(vec4, node, 10 + node.getG(), this.calHeuristic(vec4, this.destination)));
                }
                else if (node.getG() + 10 < nodeNear4.getG()) {
                    nodeNear4.setParent(node);
                    nodeNear4.setG(node.getG() + 10);
                    nodeNear4.setF(nodeNear4.getG() + nodeNear4.getH());
                }
            }
        }
        if (N && E && (y = this.getWalkableHorizontalOffset(vector3.add(1.0, 0.0, -1.0))) != -256.0) {
            final Vector3 vec4 = vector3.add(1.0, y, -1.0);
            if (this.isPassable(vec4) && !this.isContainsInClose(vec4)) {
                final Node nodeNear4 = this.getNodeInOpenByVector2(vec4);
                if (nodeNear4 == null) {
                    this.openList.offer(new Node(vec4, node, 14 + node.getG(), this.calHeuristic(vec4, this.destination)));
                }
                else if (node.getG() + 14 < nodeNear4.getG()) {
                    nodeNear4.setParent(node);
                    nodeNear4.setG(node.getG() + 14);
                    nodeNear4.setF(nodeNear4.getG() + nodeNear4.getH());
                }
            }
        }
        if (E && S && (y = this.getWalkableHorizontalOffset(vector3.add(1.0, 0.0, 1.0))) != -256.0) {
            final Vector3 vec4 = vector3.add(1.0, y, 1.0);
            if (this.isPassable(vec4) && !this.isContainsInClose(vec4)) {
                final Node nodeNear4 = this.getNodeInOpenByVector2(vec4);
                if (nodeNear4 == null) {
                    this.openList.offer(new Node(vec4, node, 14 + node.getG(), this.calHeuristic(vec4, this.destination)));
                }
                else if (node.getG() + 14 < nodeNear4.getG()) {
                    nodeNear4.setParent(node);
                    nodeNear4.setG(node.getG() + 14);
                    nodeNear4.setF(nodeNear4.getG() + nodeNear4.getH());
                }
            }
        }
        if (W && S && (y = this.getWalkableHorizontalOffset(vector3.add(-1.0, 0.0, 1.0))) != -256.0) {
            final Vector3 vec4 = vector3.add(-1.0, y, 1.0);
            if (this.isPassable(vec4) && !this.isContainsInClose(vec4)) {
                final Node nodeNear4 = this.getNodeInOpenByVector2(vec4);
                if (nodeNear4 == null) {
                    this.openList.offer(new Node(vec4, node, 14 + node.getG(), this.calHeuristic(vec4, this.destination)));
                }
                else if (node.getG() + 14 < nodeNear4.getG()) {
                    nodeNear4.setParent(node);
                    nodeNear4.setG(node.getG() + 14);
                    nodeNear4.setF(nodeNear4.getG() + nodeNear4.getH());
                }
            }
        }
        if (W && N && (y = this.getWalkableHorizontalOffset(vector3.add(-1.0, 0.0, -1.0))) != -256.0) {
            final Vector3 vec4 = vector3.add(-1.0, y, -1.0);
            if (this.isPassable(vec4) && !this.isContainsInClose(vec4)) {
                final Node nodeNear4 = this.getNodeInOpenByVector2(vec4);
                if (nodeNear4 == null) {
                    this.openList.offer(new Node(vec4, node, 14 + node.getG(), this.calHeuristic(vec4, this.destination)));
                }
                else if (node.getG() + 14 < nodeNear4.getG()) {
                    nodeNear4.setParent(node);
                    nodeNear4.setG(node.getG() + 14);
                    nodeNear4.setF(nodeNear4.getG() + nodeNear4.getH());
                }
            }
        }
    }
    
    private Node getNodeInOpenByVector2(final Vector3 vector2) {
        for (final Node node : this.openList) {
            if (vector2.equals((Object)node.getVector3())) {
                return node;
            }
        }
        return null;
    }
    
    private Node getNodeInCloseByVector2(final Vector3 vector2) {
        for (final Node node : this.closeList) {
            if (vector2.equals((Object)node.getVector3())) {
                return node;
            }
        }
        return null;
    }
    
    private boolean isContainsInClose(final Vector3 vector2) {
        return this.getNodeInCloseByVector2(vector2) != null;
    }
    
    private boolean hasBarrier(final Node node1, final Node node2) {
        return this.hasBarrier(node1.getVector3(), node2.getVector3());
    }
    
    private boolean hasBarrier(final Vector3 pos1, final Vector3 pos2) {
        if (pos1.equals((Object)pos2)) {
            return false;
        }
        if (pos1.getFloorY() != pos2.getFloorY()) {
            return true;
        }
        final boolean traverseDirection = Math.abs(pos1.getX() - pos2.getX()) > Math.abs(pos1.getZ() - pos2.getZ());
        if (traverseDirection) {
            final double loopStart = Math.min(pos1.getX(), pos2.getX());
            final double loopEnd = Math.max(pos1.getX(), pos2.getX());
            final ArrayList<Vector3> list = new ArrayList<Vector3>();
            for (double i = Math.ceil(loopStart); i <= Math.floor(loopEnd); ++i) {
                final double result;
                if ((result = Utils.calLinearFunction(pos1, pos2, i, 0)) != Double.MAX_VALUE) {
                    list.add(new Vector3(i, pos1.getY(), result));
                }
            }
            return this.hasBlocksAround(list);
        }
        final double loopStart = Math.min(pos1.getZ(), pos2.getZ());
        final double loopEnd = Math.max(pos1.getZ(), pos2.getZ());
        final ArrayList<Vector3> list = new ArrayList<Vector3>();
        for (double i = Math.ceil(loopStart); i <= Math.floor(loopEnd); ++i) {
            final double result;
            if ((result = Utils.calLinearFunction(pos1, pos2, i, 1)) != Double.MAX_VALUE) {
                list.add(new Vector3(result, pos1.getY(), i));
            }
        }
        return this.hasBlocksAround(list);
    }
    
    private boolean hasBlocksAround(final ArrayList<Vector3> list) {
        final double radius = this.entity.getWidth() * this.entity.getScale() / 2.0f + 0.1;
        final double height = this.entity.getHeight() * this.entity.getScale();
        for (final Vector3 vector3 : list) {
            final AxisAlignedBB bb = (AxisAlignedBB)new SimpleAxisAlignedBB(vector3.getX() - radius, vector3.getY(), vector3.getZ() - radius, vector3.getX() + radius, vector3.getY() + height, vector3.getZ() + radius);
            if (Utils.hasCollisionBlocks(this.level, bb)) {
                return true;
            }
            final boolean xIsInt = vector3.getX() % 1.0 == 0.0;
            final boolean zIsInt = vector3.getZ() % 1.0 == 0.0;
            if (xIsInt && zIsInt) {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0, vector3.getZ()), false).canPassThrough() || this.level.getBlock(new Vector3(vector3.getX() - 1.0, vector3.getY() - 1.0, vector3.getZ()), false).canPassThrough() || this.level.getBlock(new Vector3(vector3.getX() - 1.0, vector3.getY() - 1.0, vector3.getZ() - 1.0), false).canPassThrough() || this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0, vector3.getZ() - 1.0), false).canPassThrough()) {
                    return true;
                }
                continue;
            }
            else if (xIsInt) {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0, vector3.getZ()), false).canPassThrough() || this.level.getBlock(new Vector3(vector3.getX() - 1.0, vector3.getY() - 1.0, vector3.getZ()), false).canPassThrough()) {
                    return true;
                }
                continue;
            }
            else if (zIsInt) {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0, vector3.getZ()), false).canPassThrough() || this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0, vector3.getZ() - 1.0), false).canPassThrough()) {
                    return true;
                }
                continue;
            }
            else {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0, vector3.getZ()), false).canPassThrough()) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    private ArrayList<Node> FloydSmooth(final ArrayList<Node> array) {
        int current = 0;
        int total = 2;
        if (array.size() > 2) {
            while (total < array.size()) {
                if (!this.hasBarrier(array.get(current), array.get(total)) && total != array.size() - 1) {
                    ++total;
                }
                else {
                    array.get(total - 1).setParent(array.get(current));
                    current = total - 1;
                    ++total;
                }
            }
            Node temp = array.get(array.size() - 1);
            final ArrayList<Node> tempL = new ArrayList<Node>();
            tempL.add(temp);
            while (temp.getParent() != null) {
                tempL.add(temp = temp.getParent());
            }
            Collections.reverse(tempL);
            return tempL;
        }
        return array;
    }
    
    private ArrayList<Node> getPathRoute() {
        final ArrayList<Node> nodes = new ArrayList<Node>();
        Node temp = this.closeList.get(this.closeList.size() - 1);
        nodes.add(temp);
        while (!temp.getParent().getVector3().equals((Object)this.start)) {
            nodes.add(temp = temp.getParent());
        }
        nodes.add(temp.getParent());
        Collections.reverse(nodes);
        return nodes;
    }
    
    private boolean isPositionOverlap(final Vector3 vector2, final Vector3 vector2_) {
        return vector2.getFloorX() == vector2_.getFloorX() && vector2.getFloorZ() == vector2_.getFloorZ() && vector2.getFloorY() == vector2_.getFloorY();
    }
    
    public void resetTemporary() {
        this.openList.clear();
        this.closeList.clear();
        this.searchLimit = 100;
    }
}
