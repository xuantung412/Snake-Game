
public class GridNode {
	public enum NodeType {EMPTY, SNAKEHEAD, SNAKE, FOODBONUS}
	
	public NodeType nodeType;
	
	public int SnakeID;
	
	public GridNode(NodeType nodeType){
		this.nodeType = nodeType;
	}
}
