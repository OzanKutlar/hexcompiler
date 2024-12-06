import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

class HexCompiler{

	public static void main(String... args) {
		NodeMap map = new NodeMap(10, 10);

		map.moveCursorToTopLeft();
		map.moveCursor("dr");
		map.click(true);
		map.moveCursor("r");
		map.moveCursor("ur");
		map.moveCursor("l");
		map.moveCursor("dl");
		map.click(false);

		map.displayNodeMap();


	}



	public static class NodeMap {
		private Node[][] map;
		private int width;
		private int height;
		private int cursorX;
		private int cursorY;
		private StringBuilder record;
		private boolean clicking;

		private static final String[] DIRECTIONS = {"ur", "r", "dr", "dl", "l", "ul"};

		public NodeMap(int width, int height) {
			this.width = width;
			this.height = height;
			this.map = new Node[height][width];
			this.record = new StringBuilder();
			this.clicking = false;
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					map[y][x] = new Node();
				}
			}

			cursorX = width / 2;
			cursorY = height / 2;
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
			
			// Set anti-aliasing for smoother lines and shapes
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Fill the background with white
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, width, height);

			// Calculate the starting point for centering the hexagonal grid within the image
			int startX = 100;
			int startY = 100;

			// Define offsets for hexagonal grid layout
			double xOffset = 106;
			double yOffset = Math.sqrt(3) * xOffset;

			// Draw all the nodes
			for (int row = 0; row < nodeMatrix.length; row++) {
				for (int col = 0; col < nodeMatrix[0].length; col++) {
					int x = (int) (startX + col * xOffset);
					int y = (int) (startY + row * yOffset);

					// Offset every second row (hexagonal grid behavior)
					if (col % 2 == 1) {
						y += (int) (yOffset / 2);
					}

					// Draw the node as a simple circle
					g2d.setColor(Color.BLACK);
					g2d.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);

					// For each node, draw the lines connecting to other nodes based on open ports
					Node node = nodeMatrix[row][col];

					// Check the ports and draw lines accordingly
					if (node.checkPort("ur") && row > 0 && col < nodeMatrix[0].length - 1) {
						drawLine(g2d, x, y, nodeSize, row - 1, col + 1, startX, startY, -106, -106);
					}
					if (node.checkPort("r") && col < nodeMatrix[0].length - 1) {
						drawLine(g2d, x, y, nodeSize, row, col + 1, startX, startY, 106, 0);
					}
					if (node.checkPort("dr") && row < nodeMatrix.length - 1 && col < nodeMatrix[0].length - 1) {
						drawLine(g2d, x, y, nodeSize, row + 1, col + 1, startX, startY, 106, 106);
					}
					if (node.checkPort("dl") && row < nodeMatrix.length - 1 && col > 0) {
						drawLine(g2d, x, y, nodeSize, row + 1, col - 1, startX, startY, -106, 106);
					}
					if (node.checkPort("l") && col > 0) {
						drawLine(g2d, x, y, nodeSize, row, col - 1, startX, startY, -106, 0);
					}
					if (node.checkPort("ul") && row > 0 && col > 0) {
						drawLine(g2d, x, y, nodeSize, row - 1, col - 1, startX, startY, -106, -106);
					}
				}
			}

			// Save the image as a PNG file in the current directory
			File outputFile = new File("hexagonal_nodes.png");
			ImageIO.write(image, "PNG", outputFile);
			g2d.dispose();
		}

		// Helper function to draw a line between two nodes
		private static void drawLine(Graphics2D g2d, int x, int y, int nodeSize, int row, int col, int startX, int startY, int dx, int dy) {
			// Offset for the node's position
			double xOffset = 1.5 * nodeSize;
			double yOffset = Math.sqrt(3) * nodeSize;

			int targetX = (int) (startX + col * xOffset + dx);
			int targetY = (int) (startY + row * yOffset + dy);

			// Offset every second row (hexagonal grid behavior)
			if (col % 2 == 1) {
				targetY += (int) (yOffset / 2);
			}

			g2d.setColor(Color.BLUE);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(x, y, targetX, targetY);
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
			record.append("c" + (onoff ? "1" : "0"));
			this.clicking = onoff;
		}


		public void moveCursor(String direction) {
			if (canMoveInDirection(direction)) {
				record.append(direction);
				if(clicking){
					map[cursorX][cursorY].setPort(direction, true);
				}
				switch (direction) {
					case "ur":
						cursorY--; cursorX++;
						break;
					case "r":
						cursorX++;
						break;
					case "dr":
						cursorY++; cursorX++;
						break;
					case "dl":
						cursorY++;
						cursorX--;
						break;
					case "l":
						cursorX--;
						break;
					case "ul":
						cursorY--; cursorX--;
						break;
					default:
						throw new IllegalArgumentException("Invalid direction");
				}
				if(clicking){
					map[cursorX][cursorY].setPort(Node.reverse(direction), true);
				}

			} else {
				System.out.println("Move out of bounds");
			}
		}

		private boolean canMoveInDirection(String direction) {
			int newX = cursorX;
			int newY = cursorY;

			switch (direction) {
				case "ur":
					newY--; newX++;
					break;
				case "r":
					newX++;
					break;
				case "dr":
					newY++; newX++;
					break;
				case "dl":
					newY++; newX--;
					break;
				case "l":
					newX--;
					break;
				case "ul":
					newY--; newX--;
					break;
				default:
					throw new IllegalArgumentException("Invalid direction");
			}

			return newX >= 0 && newX < width && newY >= 0 && newY < height;
		}

		public String getCursorPosition() {
			return "(" + cursorX + ", " + cursorY + ")";
		}

		public String getMovementHistory() {
			return record.toString();
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