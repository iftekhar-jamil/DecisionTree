import java.util.ArrayList;

public class Node {
	public String value;
	public ArrayList<String> arr;
	public ArrayList<Node> children;
	
	public Node(String val) {
		value = val;
		children = new ArrayList();
	}
}
