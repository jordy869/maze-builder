import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

@SuppressWarnings("resource")
public class MazeBuilder {
	
	private static int WIDTH = 0;
	private static int HEIGHT = 0;
	private static int NUM_CELLS = 0;
	
	private static DisjointSets djSets;
	private static ArrayList<Edge> edges;
	private static ArrayList<Edge> mazeEdges;

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
			
			// runs the algorithm to generate the maze
			makeMaze();
			
			printMaze();
		} catch (Exception e) {
			System.out.println("Exception" + e.getMessage());
		}
	}
	
	
	private static void printMaze() {
		PrintStream output = System.out;
		
		// print top row of maze
		output.print("     +");
		for (int i = 0; i < WIDTH; i++) {
			output.print("-+");
		}
		output.print("\n");
		for (int row = 0; row < HEIGHT; row++) {
			printVerticalEdges(row, output);
			output.println();
			printHorizontalEdges(row, output);
			output.println();
		}

	}
	

	
	private static void printVerticalEdges(int row, PrintStream output) {
		if (row == 0)
			output.print("start ");
		else
			output.print("     |");
		
		for (int col = 0; col < WIDTH; col++) {
			int cellNum = row * WIDTH + col;
			
			if (cellNum + 1 == NUM_CELLS) // last cell needs an exit
				output.print("  finish");
			else if (isOnRight(cellNum)) // this is a wall of the maze
				output.print(" |");
			else if (mazeEdges.contains(new Edge(cellNum, cellNum + 1)))
				output.print(" |");
			else
				output.print("  ");
		}
	}
	
	private static void printHorizontalEdges(int row, PrintStream output) {
		output.print("     +");
		for (int col = 0; col < WIDTH; col++) {
			int cellNum = row * WIDTH + col;
			if (isOnBottom(cellNum))
				output.print("-+");
			else if (mazeEdges.contains(new Edge(cellNum, cellNum + WIDTH)))
				output.print("-+");
			else
				output.print(" +");
		}
	}
	
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
	
	static class Edge {
		int x;
		int y;
		
		public Edge(int x, int y) {
			if (x < y) {
				this.x = x;
				this.y = y;
			} else {
				this.x = y;
				this.y = x;
			}
		}
		
		public boolean equals(Object o) {
			if (o != null && o instanceof Edge) {
				Edge e = (Edge) o;
				return x == e.x && y == e.y;
			}
			return false;
		}
	}
}
