import java.util.*;

class hexCompiler{
	
	public static int maxLength = 16;
	
	public static Hex startRecord = new Hex("Introspection", "l,dl,dr,r");
	public static Hex stopRecord = new Hex("Retrospection", "r,dr,dl,l");
	public static Hex combineLists = new Hex("Additive Distillation", "ur,ur,l,dr,dr");
	public static Hex load = new Hex("Scribe's Reflection", "r,ul,l,dl,dr,r,ur");
	public static Hex save = new Hex("Scribe's Gambit", "r,dl,l,ul,ur,r,dr");
	public static String[] direction = new String[] {"ur", "r", "dr", "dl", "l", "ul"};
	private static HashMap<String, Hex> utilHexes;

	public static void main(String... Args){
		int rows = 5;
		int cols = 5;

		Node[][] grid = new Node[rows][cols];

		// Initialize the grid
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new Node();
			}
		}

		// Connect the nodes in a hexagonal pattern
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				// Example of setting some ports to true
				if (i > 0 && j < cols - 1) grid[i][j].setPort("ur"); // Upper right
				if (j < cols - 1) grid[i][j].setPort("r");  // Right
				if (i < rows - 1 && j < cols - 1) grid[i][j].setPort("dr"); // Down right
				if (i < rows - 1 && j > 0) grid[i][j].setPort("dl"); // Down left
				if (j > 0) grid[i][j].setPort("l");  // Left
				if (i > 0 && j > 0) grid[i][j].setPort("ul"); // Upper left
			}
		}

		printGrid(grid);
	}



	// public static void main2(String... Args){
	// 	HashMap<String, Hex> hexes = new HashMap<>();
	// 	utilHexes = new HashMap<>();
	// 	utilHexes.put("me",new Hex("Mind's Reflection", "ur,ul,dl,dr"));
	// 	utilHexes.put("eyePos",new Hex("Compass' Purification", "r,ul,dl"));
	// 	utilHexes.put("archer", new Hex("Archer's Distillation", "r,r,ur,l,dr,dr,l,ur"));
	// 	utilHexes.put("execute", new Hex("Hermes' Gambit", "dr,l,ul,dl,dr,r"));
	// 	utilHexes.put("multiply", new Hex("Multiplicative Dstl", "dr,dr,ur,ul,dl,dl"));
	// 	utilHexes.put("getNumber10", new Hex("Numerical Reflection 10", "dr,ur,ul,dl,r,dr"));
	// 	utilHexes.put("getNumber6", new Hex("Numerical Reflection 6", "dr,ur,ul,dl,r,dr,l,l"));
	// 	utilHexes.put("getNumber1", new Hex("Numerical Reflection 1", "dr,ur,ul,dl,r,r"));
	// 	utilHexes.put("giveElytra", new Hex("Altiora", "ul,dl,dl,r,r,ul,l,ul,r,r,r,dl,dr"));
	// 	utilHexes.put("meFlight", new Hex(utilHexes.get("me"), utilHexes.get("giveElytra")));
	// 	utilHexes.put("getStackSize", new Hex("Flock's Reflection", "ul,l,l,dr,dl,r,r,ur,l,ul,dl,dr,ur"));
	// 	utilHexes.put("addNElements", new Hex("Flock's Gambit", "dl,l,l,ur,ul,r,r,dr"));
	// 	utilHexes.put("getNthElement", new Hex("Selection Distillation", "ul,r,dr,dl,l,ur"));
	// 	utilHexes.put("pushEmptyList", new Hex("Vacant Reflection", "ur,ul,l,dr,dl,r,ul,ur"));
	// 	utilHexes.put("convertAllToList", new Hex(utilHexes.get("getStackSize"), utilHexes.get("addNElements")));
	// 	utilHexes.put("getListLength", new Hex("Length Purification", "ur,ur,ul,dl,dr,dr"));
	// 	utilHexes.put("getTopOfList", new Hex("Derivation Distillation", "ul,l,dr,dl,r,ur"));
	// 	utilHexes.put("addToList", new Hex("Integration Distillation", "dl,l,ur,ul,r,dr"));
	// 	utilHexes.put("rechargeItem", new Hex("Recharge Item", "ul,l,dl,dr,r,ur,ur,l,ul,dl,l,dr,dl,r,dr,ur,r,ul"));
	// 	utilHexes.put("createArtifact", new Hex("Craft Artifact", "r,r,r,ul,l,dl,dr,r,ur,r,ul,ul,l,l,dl,dl,dr,dr,r,r,ur,ur,ur,ul,l,ul,dl,ul,dl,l,dl,dr,dl,dr,r,dr,ur,dr,ur,r,ur,ul"));
	// 	utilHexes.put("levitate", new Hex("Blue Sun's Nadir", "l,dl,dr,r,ur,ul,dl,dl,dl,r,r,ul,ul,r"));
	// 	utilHexes.put("wither", new Hex("Black Sun's Nadir", "dl,dr,r,ur,ul,l,dr,dl,dl,r,r,ul,ul,ur"));
	// 	utilHexes.put("weakness", new Hex("White Sun's Nadir", "ul,l,dl,dr,r,ur,l,dl,dl,r,r,ul,ul"));
	// 	utilHexes.put("breakBlock", new Hex("Break Block", "r,ur,l,dl,dr,r,ur,ul"));
	// 	utilHexes.put("placeBlock", new Hex("Place Block", "dl,l,ul,ur,r,dr,l,ul"));
	// 	utilHexes.put("saveToMem", new Hex("Huginn's Gambit", "ul,ur,ul,l,l,dr,dr,r,ul,dl,dl"));
	// 	utilHexes.put("copyFromMem", new Hex("Muninn's Reflection", "ur,ul,ur,r,r,dl,dl,l,ur,dr,dr"));
	// 	utilHexes.put("placeWater", new Hex("Create Water", "dr,ur,ul,dl,dl,dr,ur,dr,ur,ul"));
	// 	utilHexes.put("removeWater", new Hex("Destroy Liquid", "dl,ul,ur,dr,dr,dl,ul,dl,ul,ur"));
	// 	utilHexes.put("duplicate", new Hex("Gemini Decomposition", "r,ul,dl,ul,dl,r"));
	// 	utilHexes.put("blink", new Hex("Blink", "dl,r,r,ur,ul,l,l,dr,r"));
	// 	utilHexes.put("push", new Hex("Impulse", "dl,r,r,ur,ul,l,l,dr,r,r"));
	// 	utilHexes.put("explode", new Hex("Explosion", "r,ul,dl,dl,r,ul,ul,dl,r"));
	// 	utilHexes.put("normalPos", new Hex("Compass' Purification II", "ur,dr,l"));
	// 	utilHexes.put("direction", new Hex("Alidade's Purification", "ur,ur,l"));
	// 	utilHexes.put("myPosEye", new Hex(utilHexes.get("me"), utilHexes.get("eyePos")));
	// 	utilHexes.put("myPos", new Hex(utilHexes.get("me"), utilHexes.get("normalPos")));
	// 	utilHexes.put("canSave", new Hex("Assessor's Reflection", "r,dl,l,ul,ur,r,dr,r"));
	// 	utilHexes.put("canLoad", new Hex("Auditor's Reflection", "r,ul,l,dl,dr,r,ur,r"));
	// 	utilHexes.put("blockImLookingAt", new Hex(utilHexes.get("myPosEye"), utilHexes.get("me"), utilHexes.get("direction"), utilHexes.get("archer")));
	// 	if(Args.length == 0){
	// 		hexes.put("meLevitate", new Hex(utilHexes.get("me"), utilHexes.get("getNumber10"), utilHexes.get("levitate")));
	// 		hexes.put("meWither", new Hex(utilHexes.get("me"), utilHexes.get("getNumber10"), utilHexes.get("getNumber1") , utilHexes.get("wither")));
	// 		hexes.put("unlockHurting", new Hex(utilHexes.get("me"), utilHexes.get("getNumber6"), utilHexes.get("levitate")));
	// 		hexes.put("startFlight", new Hex(utilHexes.get("me"), utilHexes.get("meFlight"), utilHexes.get("me"), utilHexes.get("direction") , utilHexes.get("getNumber10"), utilHexes.get("multiply"), utilHexes.get("push")));
	// 		hexes.put("breakBlock", new Hex(utilHexes.get("blockImLookingAt"), utilHexes.get("breakBlock")));
	// 		for(String h : hexes.keySet()){
	// 			System.out.println(h + " : " + hexes.get(h).fullSpell + "\n");
	// 		}
	// 	}
	// 	else{
	// 		String spellNew = Args[0].split(",");
	// 		Hex[] hexes = new Hex[] {};
	// 		for(String s : spellNew){
	// 			s = s.trim();
	// 			try{
	// 				try{
	// 			 		int i = Integer.parse(s);
	// 			 		hexes[hexes.length] = i;
	// 			 	}
	// 			 	catch(Exception e){
	// 			 		hexes[hexes.length] = utilHexes.get(s);
	// 			 	}

	// 			}
	// 			catch(Exception e){
	// 				System.out.println("Error on spell, you tried to use '" + s + "' as a spell");
	// 				e.printStackTrace();
	// 			}
	// 		}
	// 	}
	// }


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

	public static Hex generateNumberHex(int num){
		String spell = "dr,ur,ul,dl,r";
		String dir = "r";
		int tester = Integer.highestOneBit(num);
		boolean foundOne = false;
		while(tester != 0){
			if((num & tester) == tester){
				spell += dir + ",";
			}
			dir = getDirection(dir, -2);
			spell += dir + ",";
			tester = num >>> 1;
		}

		return new Hex("Number" + num, spell);
	}



	public static String getDirection(String current, int dir){
		for(int i = 0; i < direction.length; i++){
			if(current == direction[i]){
				i += dir;
				if(i < 0){
					i += direction.length;
				}
				if(i >= direction.length){
					i -= direction.length;
				}
				return direction[i];
			}
		}
		return null;
	}

	public static void printGrid(Node[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			// Print leading spaces for staggered rows
			for (int j = 0; j < i; j++) {
				System.out.print(" ");
			}

			// Print nodes in a row
			for (int j = 0; j < grid[i].length; j++) {
				// Check if the node has any ports active
				if (grid[i][j].hasActivePort()) {
					System.out.print("\033[1m* \033[0m"); // Bold asterisk for active ports
				} else {
					System.out.print("* ");
				}
			}
			System.out.println();
		}
	}
	
	
	public static class Hex{
		int[] hexBoundingBox;
		int hexLength;
		String spellName;
		String fullSpell;
		
		public Hex(Hex... spells){
			int totalLength = 0;
			fullSpell = spells[0].fullSpell;
			int totalStops = 0;
			for(int i = 1; i < spells.length; i++){
				Hex h = spells[i];
				totalLength += h.hexLength + 2;
				if(totalLength > hexCompiler.maxLength){
					totalStops += 1;
					totalLength = h.hexLength + 2;
					fullSpell += "," + "cn," + "r,".repeat(5) + "dr,".repeat(3) + stopRecord.fullSpell
					+ ",l".repeat(9) + ",dl," + (totalStops != 1 ? "dl," + combineLists.fullSpell + "," : "")
					+ save.fullSpell + ",ps," + load.fullSpell + ",ur,ur," + startRecord.fullSpell;
				}
				fullSpell += "," + h.fullSpell;
			}
			if(totalStops != 0){
				fullSpell = startRecord.fullSpell + "," + fullSpell + "," + "dl,".repeat(3) + stopRecord.fullSpell + ",l".repeat(9) + ",dl,dl," + combineLists.fullSpell + "," + save.fullSpell;
			}
		}
		
		// public Hex(Hex spellOne, Hex spellTwo){
			// hexLength = spellOne.hexLength + spellTwo.hexLength + 1;
			// fullSpell = spellOne.fullSpell + "," + spellTwo.fullSpell;
		// }
		
		public Hex(String spellName, String spell){
			this.spellName = spellName;
			int[] offSetFromStart = calculateBoundingBox(spell);
			String startOffset = "r,".repeat(1 - (hexBoundingBox[0] / 2));
			String returnMoves = calculateReturnToStart(offSetFromStart);
			// returnMoves += ",r".repeat(((hexBoundingBox[1] + 1) / 2) + 1);
			returnMoves += ",r".repeat(((hexBoundingBox[1]) / 2) + 1);
			fullSpell = startOffset + "c1," + spell + ",c0" + returnMoves;
		}
		
		
		
		private String calculateReturnToStart(int[] offset){
			int xOffset = offset[0];
			int yOffset = offset[1];
			String returnMoves = "";
			while(xOffset != 0 || yOffset != 0){
				if(yOffset != 0){
					returnMoves += "," + (yOffset > 0 ? "d" : "u") + (xOffset > 0 ? "l" : "r");
					yOffset += (yOffset > 0 ? -1 : 1);
					xOffset += (xOffset > 0 ? -1 : 1);
				}
				else{
					returnMoves += "," + (xOffset > 0 ? "l" : "r");
					xOffset += (xOffset > 0 ? -2 : 2);
				}
			}
			return returnMoves;
		}
		
		private int[] calculateBoundingBox(String spell){
			String[] spellComponents = spell.split(",");
			hexBoundingBox = new int[]{0, 0};
			int[] startOffset = new int[]{0, 0};
			for(int i = 0; i < spellComponents.length; i++){
				Boolean err = false;
				String s = spellComponents[i];
				if(s.length() == 2){
					switch(s){
						case "dl":
							startOffset[0] -= 1;
							startOffset[1] -= 1;
							break;
						case "dr":
							startOffset[0] += 1;
							startOffset[1] -= 1;
							break;
						case "ul":
							startOffset[0] -= 1;
							startOffset[1] += 1;
							break;
						case "ur":
							startOffset[0] += 1;
							startOffset[1] += 1;
							break;
						default:
							if(s.charAt(0) != 'c'){
								err = true;
								System.out.println("Unknown character at item " + (i + 1) + " in spell '" + spellName + "'.");
							}
							break;
					}
				}
				else if(s.length() == 1){
					switch(s.charAt(0)){
						case 'l':
							startOffset[0] -= 2;
							break;
						case 'r':
							startOffset[0] += 2;
							break;
						default:
							System.out.println("Unknown character at item " + (i + 1) + " in spell '" + spellName + "'.");
							break;
					}
				}
				else{
					System.out.println("Unknown character at item " + (i + 1) + " in spell '" + spellName + "'.");
					continue;
				}
				
				if(startOffset[0] < hexBoundingBox[0]){
					hexBoundingBox[0] = startOffset[0];
				}
				if(startOffset[0] > hexBoundingBox[1]){
					hexBoundingBox[1] = startOffset[0];
				}
			}
			this.hexLength = hexBoundingBox[1] - hexBoundingBox[0];
			return startOffset;
		}
	}
}
