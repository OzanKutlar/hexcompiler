import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeMap {
    private Node[][] map;
    private int width;
    private int height;
    private int cursorX;
    private int cursorY;
    private ArrayList<String> record;
    private boolean clicking;
    HashMap<String, Hex> spells;

    private static final String[] DIRECTIONS = {"ur", "r", "dr", "dl", "l", "ul"};

    public NodeMap(int width, int height) {
        try{
            spells = readHashMapFromFile();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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


    public static void saveHashMapToFile(HashMap<String, Hex> map) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("spells.txt"))) {
            for (String key : map.keySet()) {
                writer.write(key + " - " + map.get(key).spellName + " : " + map.get(key).fullSpell);
                writer.newLine();
            }
        }
    }

    public void addSpell(String spellName){
        int TEMP_cursorX = this.cursorX;
        int TEMP_cursorY = this.cursorY;
        int REAL_cursorX = this.cursorX;
        int REAL_cursorY = this.cursorY;
        Hex spell = spells.get(spellName);
        this.clicking = false;
        boolean solved = false;
        while(!solved){
            solved = true;
            this.cursorX = TEMP_cursorX;
            this.cursorY = TEMP_cursorY;
            for(String s : spell.fullSpell.split(",")){
                if(s.charAt(0) == 'c'){
                    continue;
                }
                short flag = this.simulateCursor(s);
                if(flag == 0){
                    continue;
                }
                solved = false;
                if(ErrorCode.contains(flag, ErrorCode.X_OVER)){
                    System.out.println("Spell cant be placed");
                    return;
                }
                if(ErrorCode.contains(flag, ErrorCode.X_UNDER)){
                    TEMP_cursorX++;
                }
                if(ErrorCode.contains(flag, ErrorCode.Y_UNDER)){
                    TEMP_cursorY++;
                }
                break;
            }
        }
		this.cursorX = REAL_cursorX;
		this.cursorY = REAL_cursorY;
        moveCursorTo(TEMP_cursorX, TEMP_cursorY);
        for(String s : spell.fullSpell.split(",")){
            if(s.charAt(0) == 'c'){
                this.click(s.charAt(1) == '1');
            }
            else{
                this.moveCursor(s);
            }
        }
    }

    public static HashMap<String, Hex> readHashMapFromFile() throws IOException {
        HashMap<String, Hex> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("spells.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" : ", 2);
                if (parts.length == 2) {
                    map.put(parts[0].split(" - ")[0], new Hex(parts[0].split(" - ")[1], parts[1]));
                }
            }
        }
        return map;
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

    public void moveCursorTo(int x, int y) {
        // Move the cursor to the specified position (x, y)
        while (cursorX != x || cursorY != y) {
            if (cursorX > x && cursorY > y) {
                moveCursor("ul"); // Move up-left diagonally
            } else if (cursorX > x && cursorY < y) {
                moveCursor("dl"); // Move down-left diagonally
            } else if (cursorX < x && cursorY > y) {
                moveCursor("ur"); // Move up-right diagonally
            } else if (cursorX < x && cursorY < y) {
                moveCursor("dr"); // Move down-right diagonally
            } else if (cursorX > x) {
                moveCursor("l");  // Move left
            } else if (cursorX < x) {
                moveCursor("r");  // Move right
            } else if (cursorY > y) {
                moveCursor("ur");  // Move up
            } else if (cursorY < y) {
                moveCursor("dr");  // Move down
            }
        }
    }


    public void click(boolean onoff){
        record.add("c" + (onoff ? "1" : "0"));
        this.clicking = onoff;
    }

    public short simulateCursor(String direction) {
        short errorCode = canMoveInDirection(direction);
        if (errorCode == 0) {
            System.out.printf("Simulating cursor from %d : %d to ", cursorX, cursorY);
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
            System.out.printf("%d : %d\n", cursorX, cursorY);
        } else {
            System.out.println("Move out of bounds");
        }
        return errorCode;
    }


    public void moveCursor(String direction) {
        short errorCode = canMoveInDirection(direction);
        if (errorCode == 0) {
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
            return;
        }
    }

    private short canMoveInDirection(String direction) {
        int newX = cursorX;
        int newY = cursorY;
        System.out.println("Checking direction : " + direction);

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
        short errorCode = 0;
        if(newX < 0){
            errorCode = ErrorCode.combine(errorCode, ErrorCode.X_UNDER);
        }
        if(newX >= HexCompilerImage.mapWidth){
            errorCode = ErrorCode.combine(errorCode, ErrorCode.X_OVER);
        }
        if(newY < 0){
            errorCode = ErrorCode.combine(errorCode, ErrorCode.Y_UNDER);
        }
        if(newY > HexCompilerImage.mapHeight){
            errorCode = ErrorCode.combine(errorCode, ErrorCode.Y_OVER);
        }
		if(errorCode == 0 && map[newY][newX].checkPort(Node.reverse(direction))){
			newX -= cursorX;
			newY -= cursorY;
			if(newX < 0){
				errorCode = ErrorCode.combine(errorCode, ErrorCode.X_UNDER);
			}
			if(newX > 0){
				errorCode = ErrorCode.combine(errorCode, ErrorCode.X_OVER);
			}
			if(newY < 0){
				errorCode = ErrorCode.combine(errorCode, ErrorCode.Y_UNDER);
			}
			if(newY > 0){
				errorCode = ErrorCode.combine(errorCode, ErrorCode.Y_OVER);
			}
		}


        return errorCode;
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
