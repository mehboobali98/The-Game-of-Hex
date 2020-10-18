package AiHex.hexBoards;

public class HexLocation {

    private int nodeId; // 4 to 52
    private int season; // ?
    private int value; // Red = 1, Blue = 2, Blank = 0

    public HexLocation(int nodeId) {
        this.nodeId = nodeId;
        this.value = Board.BLANK;
    }

    public HexLocation(int nodeId, int season, int value) {
        this.nodeId = nodeId;
        this.season = season;
        this.value = value;
    }

    public int getNodeID() {
        return nodeId;
    }

    public void setNodeID(int nodeID) {
        this.nodeId = nodeID;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public HexLocation clone() {
        return new HexLocation(nodeId, season, value);
    }
}
