import java.util.ArrayList;
import java.util.Random;

/**
 * @author Jordan Prosch
 * @version Sept 2015
 *
 * MazeBuilder.java is a program that takes a width and height as input and 
 * prints a maze as output. The maze output will consist of the "+", "|", "-", and 
 * space characters, along with the words "start" and "finish" to signal the 
 * beginning and end of the maze.
 * 
 * The mazes that are created will have the following properties:
 *   * every cell can be reached from every other cell
 *   * there are no cycles
 *   * no boundary is deleted (except for the beginning and end)
 */
public class MazeBuilder {
	/**
	 * Implementation details:
	 * 
	 * A maze is represented by its width and height. Its cells are numbered 0 to (width * height) - 1.
	 * To visualize a sample 3x4 maze, consider the maze below, with its cells numbered:
	 *    +---+---+---+---+
	 *      0 | 1 | 2 | 3 |
	 *    +---+---+---+---+
	 *    | 4 | 5 | 6 | 7 |
	 *    +---+---+---+---+
	 *    | 8 | 9 |10 |11 
	 *    +---+---+---+---+ 
	 *    
	 * To create the maze, the maze cells are thought of as being in their own set in a Disjoint Set.
	 * Then, cells are randomly joined to create a larger set, which requires removing a maze edge. 
	 * Once all cells are connected (all cells make up a single set), the maze can be printed.
	 * This algorithm is essentially a variation of Kruskal's algorithm for minimum spanning trees.
	 */
	
	private static int WIDTH = 0;
	private static int HEIGHT = 0;
	private static int NUM_CELLS = 0;
	
	// fields used for running the maze algorithm
	private static DisjointSets djSets;
	private static ArrayList<Edge> edges;
	private static ArrayList<Edge> mazeEdges; // also used for printing the maze

	/**
	 * Prints a maze with the given width and height.
	 * @param args[0] the width of the maze
	 * @param args[1] the height of the maze
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Must have 2 arguments passed in: <width> <height>");
			System.exit(1);
		} 
		
		try {
			WIDTH = Integer.parseInt(args[0]);
			HEIGHT = Integer.parseInt(args[1]);
			NUM_CELLS = WIDTH * HEIGHT;
			if (WIDTH < 2 || HEIGHT < 2) {
				System.out.println("Width and height must be larger than 1: " + WIDTH + " " + HEIGHT);
				System.exit(1);
			}
		} catch (NumberFormatException nfe) {
			System.out.println("Width and height must be ints: " + args[0] + " " + args[1]);
			System.exit(1);
		}
		
		try {
			djSets = new DisjointSets(NUM_CELLS);
			edges = initializeEdges();
			mazeEdges = new ArrayList<Edge>();
			
			// generate the maze
			makeMaze();
			
			// print the maze
			printMaze();
		} catch (Exception e) {
			System.out.println("Exception" + e.getMessage());
			System.exit(1);
		}
	}
	
	// Runs an implementation of Kruskal's algorithm to generate 
	// an acyclic maze with the following properties:
	//   * every cell can be reached from every other cell
	//   * there are no cycles
	//   * no boundary is deleted (except for the beginning and end)
	private static void makeMaze() {
		Random random = new Random();
		while (djSets.numSets() > 1) {
			Edge randomEdge = edges.remove(random.nextInt(edges.size()));
			int u = djSets.find(randomEdge.x);
			int v = djSets.find(randomEdge.y);
			if (u != v)
				djSets.union(u, v); // removing this edge connects previously unconnected sets, so we connect them now
			else 
				mazeEdges.add(randomEdge); // cells were already connected, so we add the edge to the maze
		}
		
		// add remaining edges to the set of maze edges
		for (Edge edge : edges)
			mazeEdges.add(edge);
		
		edges.clear();
	}
	
	// Prints the entire maze to System.out
	private static void printMaze() {		
		// print top row of maze
		System.out.print("     +");
		for (int i = 0; i < WIDTH; i++) {
			System.out.print("-+");
		}
		System.out.print("\n");
		
		// print the remainder of the maze
		for (int row = 0; row < HEIGHT; row++) {
			printVerticalEdges(row);
			System.out.println();
			printHorizontalEdges(row);
			System.out.println();
		}

	}

	// Prints the vertical edges of the maze (ex: "| | |   |   |")
	private static void printVerticalEdges(int row) {
		if (row == 0)
			System.out.print("start ");
		else
			System.out.print("     |");
		
		for (int col = 0; col < WIDTH; col++) {
			int cellNum = row * WIDTH + col;
			if (cellNum + 1 == NUM_CELLS) // last cell needs an exit
				System.out.print("  finish");
			else if (isOnRight(cellNum)) // this is a wall of the maze
				System.out.print(" |");
			else if (mazeEdges.contains(new Edge(cellNum, cellNum + 1)))
				System.out.print(" |");
			else
				System.out.print("  ");
		}
	}
	
	// Prints the horizontal edges of the maze (ex: "+ +-+-+ + +")
	private static void printHorizontalEdges(int row) {
		System.out.print("     +");
		for (int col = 0; col < WIDTH; col++) {
			int cellNum = row * WIDTH + col;
			if (isOnBottom(cellNum))
				System.out.print("-+");
			else if (mazeEdges.contains(new Edge(cellNum, cellNum + WIDTH)))
				System.out.print("-+");
			else
				System.out.print(" +");
		}
	}
	
	// creates a list of all the edges in the maze
	private static ArrayList<Edge> initializeEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < djSets.numElements(); i++) {
			if (!isOnRight(i))
				edges.add(new Edge(i, i + 1));
			if (!isOnBottom(i))
				edges.add(new Edge(i, i + WIDTH));
		}
		
		
		return edges;
	}
	
	// returns true if the cell is on the right side of the maze
	private static boolean isOnRight(int cell) {
		return (cell + 1) % WIDTH == 0;
	}
	
	// returns true if the cell is on the bottom of the maze
	private static boolean isOnBottom(int cell) {
		cell++; // cancel 0-based indexing
		return (NUM_CELLS - WIDTH) < cell && cell <= NUM_CELLS;
	}
	
	// Class to represent an edge between two cells x and y
	static class Edge {
		int x;
		int y;
		
		// Constructs a new edge. It does not matter
		// what order the edges are passed in.
		public Edge(int x, int y) {
			if (x < y) {
				this.x = x;
				this.y = y;
			} else {
				this.x = y;
				this.y = x;
			}
		}
		
		// Checks if two edges are equal.
		public boolean equals(Object o) {
			if (o != null && o instanceof Edge) {
				Edge e = (Edge) o;
				return x == e.x && y == e.y;
			}
			return false;
		}
	}
}
