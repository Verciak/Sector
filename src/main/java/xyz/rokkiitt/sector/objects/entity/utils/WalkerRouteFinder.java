package xyz.rokkiitt.sector.objects.entity.utils;

import cn.nukkit.block.Block;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import xyz.rokkiitt.sector.objects.entity.WalkingEntity;

public class WalkerRouteFinder extends SimpleRouteFinder {
    private final PriorityQueue<Node> openList;

    private final ArrayList<Node> closeList;

    private int searchLimit;

    public WalkerRouteFinder(WalkingEntity entity) {
        super(entity);
        this.openList = new PriorityQueue<>();
        this.closeList = new ArrayList<>();
        this.searchLimit = 100;
        this.level = entity.getLevel();
    }

    public WalkerRouteFinder(WalkingEntity entity, Vector3 start) {
        super(entity);
        this.openList = new PriorityQueue<>();
        this.closeList = new ArrayList<>();
        this.searchLimit = 100;
        this.level = entity.getLevel();
        this.start = start;
    }

    public WalkerRouteFinder(WalkingEntity entity, Vector3 start, Vector3 destination) {
        super(entity);
        this.openList = new PriorityQueue<>();
        this.closeList = new ArrayList<>();
        this.searchLimit = 100;
        this.level = entity.getLevel();
        this.start = start;
        this.destination = destination;
    }

    private int calHeuristic(Vector3 pos1, Vector3 pos2) {
        return 10 * (Math.abs(pos1.getFloorX() - pos2.getFloorX()) + Math.abs(pos1.getFloorZ() - pos2.getFloorZ())) + 11 *
                Math.abs(pos1.getFloorY() - pos2.getFloorY());
    }

    public boolean search() {
        this.finished = false;
        this.searching = true;
        if (this.start == null)
            this.start = (Vector3)this.entity;
        if (this.destination == null) {
            Vector3 vec = this.entity.getTargetVector();
            if (vec == null) {
                this.searching = false;
                this.finished = true;
                return false;
            }
            this.destination = vec;
        }
        resetTemporary();
        Node presentNode = new Node(this.start);
        this.closeList.add(new Node(this.start));
        while (!isPositionOverlap(presentNode.getVector3(), this.destination)) {
            if (isInterrupted()) {
                this.searchLimit = 0;
                this.searching = false;
                this.finished = true;
                return false;
            }
            putNeighborNodeIntoOpen(presentNode);
            if (this.openList.peek() == null || this.searchLimit-- <= 0) {
                this.searching = false;
                this.finished = true;
                this.reachable = false;
                addNode(new Node(this.destination));
                return false;
            }
            this.closeList.add(presentNode = this.openList.poll());
        }
        if (!presentNode.getVector3().equals(this.destination))
            this.closeList.add(new Node(this.destination, presentNode, 0, 0));
        ArrayList<Node> findingPath = getPathRoute();
        findingPath = FloydSmooth(findingPath);
        resetNodes();
        addNode(findingPath);
        this.finished = true;
        this.searching = false;
        return true;
    }

    private Block getHighestUnder(Vector3 vector3, int limit) {
        if (limit > 0) {
            for (int i = vector3.getFloorY(); i >= vector3.getFloorY() - limit; i--) {
                Block block = this.level.getBlock(vector3.getFloorX(), i, vector3.getFloorZ(), false);
                if (isWalkable((Vector3)block) && this.level
                        .getBlock((Vector3)block.add(0.0D, 1.0D, 0.0D), false).getId() == 0)
                    return block;
            }
            return null;
        }
        for (int y = vector3.getFloorY(); y >= 0; y--) {
            Block block = this.level.getBlock(vector3.getFloorX(), y, vector3.getFloorZ(), false);
            if (isWalkable((Vector3)block) && this.level
                    .getBlock((Vector3)block.add(0.0D, 1.0D, 0.0D), false).getId() == 0)
                return block;
        }
        return null;
    }

    private boolean canWalkOn(Block block) {
        return (block.getId() != 10 && block.getId() != 11 && block.getId() != 81);
    }

    private boolean isWalkable(Vector3 vector3) {
        Block block = this.level.getBlock(vector3, false);
        return (!block.canPassThrough() && canWalkOn(block));
    }

    private boolean isPassable(Vector3 vector3) {
        double radius = (this.entity.getWidth() * this.entity.getScale() / 2.0F);
        float height = this.entity.getHeight() * this.entity.getScale();
        AxisAlignedBB AxisAlignedBB = new AxisAlignedBB(vector3.getX() - radius, vector3.getY(), vector3.getZ() - radius, vector3.getX() + radius, vector3.getY() + height, vector3.getZ() + radius);
        return (!Utils.hasCollisionBlocks(this.level, (AxisAlignedBB)AxisAlignedBB) &&
                !this.level.getBlock(vector3.add(0.0D, -1.0D, 0.0D), false).canPassThrough());
    }

    private int getWalkableHorizontalOffset(Vector3 vector3) {
        Block block = getHighestUnder(vector3, 4);
        if (block != null)
            return block.getFloorY() - vector3.getFloorY() + 1;
        return -256;
    }

    public int getSearchLimit() {
        return this.searchLimit;
    }

    public void setSearchLimit(int limit) {
        this.searchLimit = limit;
    }

    private void putNeighborNodeIntoOpen(Node node) {
        Vector3 vector3 = new Vector3(node.getVector3().getFloorX() + 0.5D, node.getVector3().getY(), node.getVector3().getFloorZ() + 0.5D);
        double y;
        boolean E;
        if (E = ((y = getWalkableHorizontalOffset(vector3.add(1.0D, 0.0D, 0.0D))) != -256.0D)) {
            Vector3 vec = vector3.add(1.0D, y, 0.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 10 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 10 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 10);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        boolean S;
        if (S = ((y = getWalkableHorizontalOffset(vector3.add(0.0D, 0.0D, 1.0D))) != -256.0D)) {
            Vector3 vec = vector3.add(0.0D, y, 1.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 10 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 10 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 10);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        boolean W;
        if (W = ((y = getWalkableHorizontalOffset(vector3.add(-1.0D, 0.0D, 0.0D))) != -256.0D)) {
            Vector3 vec = vector3.add(-1.0D, y, 0.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 10 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 10 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 10);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        boolean N;
        if (N = ((y = getWalkableHorizontalOffset(vector3.add(0.0D, 0.0D, -1.0D))) != -256.0D)) {
            Vector3 vec = vector3.add(0.0D, y, -1.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 10 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 10 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 10);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        if (N && E && (y = getWalkableHorizontalOffset(vector3.add(1.0D, 0.0D, -1.0D))) != -256.0D) {
            Vector3 vec = vector3.add(1.0D, y, -1.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 14 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 14 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 14);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        if (E && S && (y = getWalkableHorizontalOffset(vector3.add(1.0D, 0.0D, 1.0D))) != -256.0D) {
            Vector3 vec = vector3.add(1.0D, y, 1.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 14 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 14 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 14);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        if (W && S && (y = getWalkableHorizontalOffset(vector3.add(-1.0D, 0.0D, 1.0D))) != -256.0D) {
            Vector3 vec = vector3.add(-1.0D, y, 1.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 14 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 14 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 14);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
        if (W && N && (y = getWalkableHorizontalOffset(vector3.add(-1.0D, 0.0D, -1.0D))) != -256.0D) {
            Vector3 vec = vector3.add(-1.0D, y, -1.0D);
            if (isPassable(vec) && !isContainsInClose(vec)) {
                Node nodeNear = getNodeInOpenByVector2(vec);
                if (nodeNear == null) {
                    this.openList
                            .offer(new Node(vec, node, 14 + node.getG(), calHeuristic(vec, this.destination)));
                } else if (node.getG() + 14 < nodeNear.getG()) {
                    nodeNear.setParent(node);
                    nodeNear.setG(node.getG() + 14);
                    nodeNear.setF(nodeNear.getG() + nodeNear.getH());
                }
            }
        }
    }

    private Node getNodeInOpenByVector2(Vector3 vector2) {
        for (Node node : this.openList) {
            if (vector2.equals(node.getVector3()))
                return node;
        }
        return null;
    }

    private Node getNodeInCloseByVector2(Vector3 vector2) {
        for (Node node : this.closeList) {
            if (vector2.equals(node.getVector3()))
                return node;
        }
        return null;
    }

    private boolean isContainsInClose(Vector3 vector2) {
        return (getNodeInCloseByVector2(vector2) != null);
    }

    private boolean hasBarrier(Node node1, Node node2) {
        return hasBarrier(node1.getVector3(), node2.getVector3());
    }

    private boolean hasBarrier(Vector3 pos1, Vector3 pos2) {
        if (pos1.equals(pos2))
            return false;
        if (pos1.getFloorY() != pos2.getFloorY())
            return true;
        boolean traverseDirection = (Math.abs(pos1.getX() - pos2.getX()) > Math.abs(pos1.getZ() - pos2.getZ()));
        if (traverseDirection) {
            double d1 = Math.min(pos1.getX(), pos2.getX());
            double d2 = Math.max(pos1.getX(), pos2.getX());
            ArrayList<Vector3> arrayList = new ArrayList<>();
            double d3;
            for (d3 = Math.ceil(d1); d3 <= Math.floor(d2); d3++) {
                double result;
                if ((result = Utils.calLinearFunction(pos1, pos2, d3, 0)) != Double.MAX_VALUE)
                    arrayList.add(new Vector3(d3, pos1.getY(), result));
            }
            return hasBlocksAround(arrayList);
        }
        double loopStart = Math.min(pos1.getZ(), pos2.getZ());
        double loopEnd = Math.max(pos1.getZ(), pos2.getZ());
        ArrayList<Vector3> list = new ArrayList<>();
        double i;
        for (i = Math.ceil(loopStart); i <= Math.floor(loopEnd); i++) {
            double result;
            if ((result = Utils.calLinearFunction(pos1, pos2, i, 1)) != Double.MAX_VALUE)
                list.add(new Vector3(result, pos1.getY(), i));
        }
        return hasBlocksAround(list);
    }

    private boolean hasBlocksAround(ArrayList<Vector3> list) {
        double radius = (this.entity.getWidth() * this.entity.getScale() / 2.0F) + 0.1D;
        double height = (this.entity.getHeight() * this.entity.getScale());
        for (Vector3 vector3 : list) {
            AxisAlignedBB AxisAlignedBB = new AxisAlignedBB(vector3.getX() - radius, vector3.getY(), vector3.getZ() - radius, vector3.getX() + radius, vector3.getY() + height, vector3.getZ() + radius);
            if (Utils.hasCollisionBlocks(this.level, (AxisAlignedBB)AxisAlignedBB))
                return true;
            boolean xIsInt = (vector3.getX() % 1.0D == 0.0D);
            boolean zIsInt = (vector3.getZ() % 1.0D == 0.0D);
            if (xIsInt && zIsInt) {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0D, vector3.getZ()), false)
                        .canPassThrough() || this.level
                        .getBlock(new Vector3(vector3.getX() - 1.0D, vector3.getY() - 1.0D, vector3.getZ()), false)
                        .canPassThrough() || this.level

                        .getBlock(new Vector3(vector3.getX() - 1.0D, vector3.getY() - 1.0D, vector3.getZ() - 1.0D), false)

                        .canPassThrough() || this.level
                        .getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0D, vector3.getZ() - 1.0D), false)
                        .canPassThrough())
                    return true;
                continue;
            }
            if (xIsInt) {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0D, vector3.getZ()), false)
                        .canPassThrough() || this.level
                        .getBlock(new Vector3(vector3.getX() - 1.0D, vector3.getY() - 1.0D, vector3.getZ()), false)
                        .canPassThrough())
                    return true;
                continue;
            }
            if (zIsInt) {
                if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0D, vector3.getZ()), false)
                        .canPassThrough() || this.level
                        .getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0D, vector3.getZ() - 1.0D), false)
                        .canPassThrough())
                    return true;
                continue;
            }
            if (this.level.getBlock(new Vector3(vector3.getX(), vector3.getY() - 1.0D, vector3.getZ()), false)
                    .canPassThrough())
                return true;
        }
        return false;
    }

    private ArrayList<Node> FloydSmooth(ArrayList<Node> array) {
        int current = 0;
        int total = 2;
        if (array.size() > 2) {
            while (total < array.size()) {
                if (!hasBarrier(array.get(current), array.get(total)) && total != array.size() - 1) {
                    total++;
                    continue;
                }
                ((Node)array.get(total - 1)).setParent(array.get(current));
                current = total - 1;
                total++;
            }
            Node temp = array.get(array.size() - 1);
            ArrayList<Node> tempL = new ArrayList<>();
            tempL.add(temp);
            while (temp.getParent() != null)
                tempL.add(temp = temp.getParent());
            Collections.reverse(tempL);
            return tempL;
        }
        return array;
    }

    private ArrayList<Node> getPathRoute() {
        ArrayList<Node> nodes = new ArrayList<>();
        Node temp = this.closeList.get(this.closeList.size() - 1);
        nodes.add(temp);
        while (!temp.getParent().getVector3().equals(this.start))
            nodes.add(temp = temp.getParent());
        nodes.add(temp.getParent());
        Collections.reverse(nodes);
        return nodes;
    }

    private boolean isPositionOverlap(Vector3 vector2, Vector3 vector2_) {
        return (vector2.getFloorX() == vector2_.getFloorX() && vector2.getFloorZ() == vector2_.getFloorZ() && vector2
                .getFloorY() == vector2_.getFloorY());
    }

    public void resetTemporary() {
        this.openList.clear();
        this.closeList.clear();
        this.searchLimit = 100;
    }
}
