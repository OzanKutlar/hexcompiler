import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

class HexCompilerImage{

	public static void main(String... args) throws Exception {
		NodeMap map = new NodeMap(18, 11);
		HashMap<String, String> spells = readHashMapFromFile();
		map.moveCursorToTopLeft();
		map.moveCursor("dl");
		map.click(true);
		map.moveCursor("r");
		map.moveCursor("ur");
		map.moveCursor("l");
		map.moveCursor("dl");
		map.click(false);
		System.out.println(map.getFullSpell());
		map.displayNodeMap();


	}
	
	public static void saveHashMapToFile(HashMap<String, String> map) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("spells.txt"))) {
			for (String key : map.keySet()) {
				String value = map.get(key);
				writer.write(key + " : " + value);
				writer.newLine();
			}
		}
	}

	// Function to read HashMap from a file
	public static HashMap<String, String> readHashMapFromFile() throws IOException {
		HashMap<String, String> map = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("spells.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" : ", 2); // Split only on the first occurrence of " : "
				if (parts.length == 2) {
					map.put(parts[0].split(" - ")[0], parts[1]);
				}
			}
		}
		return map;
	}



	public static class NodeMap {
		private Node[][] map;
		private int width;
		private int height;
		private int cursorX;
		private int cursorY;
		private ArrayList<String> record;
		private boolean clicking;

		private static final String[] DIRECTIONS = {"ur", "r", "dr", "dl", "l", "ul"};

		public NodeMap(int width, int height) {
			this.width = width;
			this.height = height;
			this.map = new Node[height][width];
			this.record = new ArrayList<String>();
			this.clicking = false;
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					map[y][x] = new Node();
				}
			}

			cursorX = width>>1;
			cursorY = height>>1;
		}

		public void displayNodeMap(){
			try{
				generateHexagonalImage(this.map, 10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		public static void generateHexagonalImage(Node[][] nodeMatrix, int nodeSize) throws Exception {
			// Image resolution (1920x1080)
			int width = 1920;
			int height = 1080;

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = image.createGraphics();
			
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Fill the background with white
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, width, height);

			// Calculate the starting point for centering the hexagonal grid within the image
			int permStartX = 40;
			int startY = 40;

			// Define offsets for hexagonal grid layout
			int xOffset = 53;
			int yOffset = xOffset;

			// Draw all the nodes
			for (int row = 0; row < nodeMatrix.length; row++) {
				int startX = permStartX;
				int y = (int) (startY + (row * yOffset));
				if((row & 1) == 0){
					startX += (xOffset>>1);
				}
				for (int col = 0; col < nodeMatrix[0].length; col++) {
					int x = (int) (startX + (col * xOffset));


					// Draw the node as a simple circle
					g2d.setColor(Color.BLACK);
					g2d.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);

					// For each node, draw the lines connecting to other nodes based on open ports
					Node node = nodeMatrix[row][col];

					// Check the ports and draw lines accordingly
					if (node.checkPort("ur") && row > 0 && col < nodeMatrix[0].length - 1) {
						drawLine(g2d, x, y, x + (xOffset>>1), y - yOffset);
					}
					if (node.checkPort("r") && col < nodeMatrix[0].length - 1) {
						drawLine(g2d, x, y, x + (xOffset), y);
					}
					if (node.checkPort("dr") && row < nodeMatrix.length - 1 && col < nodeMatrix[0].length - 1) {
						drawLine(g2d, x, y, x + (xOffset>>1), y + yOffset);
					}
					if (node.checkPort("dl") && row < nodeMatrix.length - 1 && col > 0) {
						drawLine(g2d, x, y, x - (xOffset>>1), y + yOffset);
					}
					if (node.checkPort("l") && col > 0) {
						drawLine(g2d, x, y, x - (xOffset), y);
					}
					if (node.checkPort("ul") && row > 0 && col > 0) {
						drawLine(g2d, x, y, x - (xOffset>>1), y - yOffset);
					}
				}
			}

			// Save the image as a PNG file in the current directory
			File outputFile = new File("hexagonal_nodes.png");
			ImageIO.write(image, "PNG", outputFile);
			g2d.dispose();
		}

		// Helper function to draw a line between two nodes
		private static void drawLine(Graphics2D g2d, int x, int y, int endx, int endy) {
			g2d.setColor(Color.BLUE);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(x, y, endx, endy);
		}




		public void moveCursorToTopLeft() {
			// Move the cursor to the top-left (0, 0) position
			while (cursorX > 0 || cursorY > 0) {
				if (cursorX > 0 && cursorY > 0) {
					moveCursor("ul");  // Move up-left diagonally
				} else if (cursorX > 0) {
					moveCursor("l");   // Move left
				} else if (cursorY > 0) {
					moveCursor("ul");  // Move up-left if only y is positive
				}
			}
		}

		public void click(boolean onoff){
			record.add("c" + (onoff ? "1" : "0"));
			this.clicking = onoff;
		}


		public void moveCursor(String direction) {
			if (canMoveInDirection(direction)) {
				record.add(direction);
				System.out.printf("Moving cursor from %d : %d to ", cursorX, cursorY);
				if(clicking){
					map[cursorY][cursorX].setPort(direction, true);
				}
				switch(direction.charAt(0)){
					case 'u':
						cursorY--;
						break;
					case 'd':
						cursorY++;
						break;
					case 'r':
						cursorX++;
						break;
					case 'l':
						cursorX--;
						break;
					default:
						throw new IllegalArgumentException("Invalid direction");
				}
				if(direction.length() == 2){
					// A bit of reverse feeling logic here. On an hexagonal grid the X axis doesnt change we go left from an odd row or if we go right from an even one. The logic stays the same even though we use 0 based arrays bc we increment the cursorY in the previous one.
					switch(direction.charAt(1)){
						case 'l':
							if((cursorY & 1) == 0){
								cursorX--;
							}
							break;
						case 'r':
							if((cursorY & 1) == 1){
								cursorX++;
							}
							break;
						default:
							throw new IllegalArgumentException("Invalid direction");
					}
				}
				if(clicking){
					map[cursorY][cursorX].setPort(Node.reverse(direction), true);
				}
				System.out.printf("%d : %d\n", cursorX, cursorY);

			} else {
				System.out.println("Move out of bounds");
			}
		}

		private boolean canMoveInDirection(String direction) {
			int newX = cursorX;
			int newY = cursorY;

			switch(direction.charAt(0)){
				case 'u':
					newY--;
					break;
				case 'd':
					newY++;
					break;
				case 'r':
					newX++;
					break;
				case 'l':
					newX--;
					break;
				default:
					throw new IllegalArgumentException("Invalid direction");
			}
			if(direction.length() == 2){
				// A bit of reverse feeling logic here. On an hexagonal grid the X axis doesnt change we go left from an odd row or if we go right from an even one. The logic stays the same even though we use 0 based arrays bc we increment the cursorY in the previous one.
				switch(direction.charAt(1)){
					case 'l':
						if((newY & 1) == 0){
							newX--;
						}
						break;
					case 'r':
						if((newY & 1) == 1){
							newX++;
						}
						break;
					default:
						throw new IllegalArgumentException("Invalid direction");
				}
			}

			return newX >= 0 && newX < width && newY >= 0 && newY < height;
		}

		public String getCursorPosition() {
			return "(" + cursorX + ", " + cursorY + ")";
		}

		public String getFullSpell() {
			return String.join(",", record);
		}

		public Node getNodeAt(int x, int y) {
			if (x >= 0 && x < width && y >= 0 && y < height) {
				return map[y][x];
			}
			return null;
		}

		public void setNodePort(int x, int y, String port, boolean value) {
			Node node = getNodeAt(x, y);
			if (node != null) {
				node.setPort(port, value);
			}
		}
	}




	public static class Node{
		private boolean[] ports = {false, false, false, false, false, false};

		public void setPort(String port, boolean value) {
			switch (port) {
				case "ur":
					ports[0] = value;
					break;
				case "r":
					ports[1] = value;
				   break;
				case "dr":
					ports[2] = value;
					break;
				case "dl":
					ports[3] = value;
					break;
				case "l":
					ports[4] = value;
					break;
				case "ul":
					ports[5] = value;
					break;
				default:
					throw new IllegalArgumentException("Unknown direction");
			}
		}

		public boolean hasActivePort(){
			return ports[0] || ports[1] || ports[2] || ports[3] || ports[4] || ports[5];
		}

		public static String reverse(String input) {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);
				if (c == 'u') {
					result.append('d');
				} else if (c == 'd') {
					result.append('u');
				} else if (c == 'r') {
					result.append('l');
				} else if (c == 'l') {
					result.append('r');
				} else {
					result.append(c);
				}
			}
			return result.toString();
		}

		public boolean checkPort(String port) {
			switch (port) {
				case "ur":
					return ports[0];
				case "r":
					return ports[1];
				case "dr":
					return ports[2];
				case "dl":
					return ports[3];
				case "l":
					return ports[4];
				case "ul":
					return ports[5];
				default:
					throw new IllegalArgumentException("Unknown direction");
			}
		}
	}

}