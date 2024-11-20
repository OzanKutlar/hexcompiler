import java.util.*;

class hexCompiler{
	
	public static int maxLength = 12;
	
	public static Hex startRecord = new Hex("", "l,dl,dr,r");
	public static Hex stopRecord = new Hex("", "r,dr,dl,l");
	public static Hex combineLists = new Hex("", "ur,ur,l,dr,dr");
	public static Hex load = new Hex("", "r,ul,l,dl,dr,r,ur");
	public static Hex save = new Hex("", "r,dl,l,ul,ur,r,dr");

	public static void main(String... Args){
		HashMap<String, Hex> hexes = new HashMap<>();
		hexes.put("me",new Hex("Mind's Reflection", "ur,ul,dl,dr"));
		hexes.put("eyePos",new Hex("Compass' Purification", "r,ul,dl"));
		hexes.put("archer", new Hex("Archer's Distillation", "r,r,ur,l,dr,dr,l,ur"));
		hexes.put("execute", new Hex("Hermes' Gambit", "dr,l,ul,dl,dr,r"));
		hexes.put("execute", new Hex("", "ur,dr,l,ul,dl,dr,r"));
		hexes.put("multiply", new Hex("", "dr,dr,ur,ul,dl,dl"));
		hexes.put("getNumber10", new Hex("", "dr,ur,ul,dl,r,dr"));
		hexes.put("giveElytra", new Hex("Altiora", "ul,dl,dl,r,r,ul,l,ul,r,r,r,dl,dr"));
		hexes.put("meFlight", new Hex(hexes.get("me"), hexes.get("giveElytra")));
		hexes.put("getStackSize", new Hex("", "ul,l,l,dr,dl,r,r,ur,l,ul,dl,dr,ur"));
		hexes.put("addNElements", new Hex("", "dl,l,l,ur,ul,r,r,dr"));
		hexes.put("convertAllToList", new Hex(hexes.get("getStackSize"), hexes.get("addNElements")));
		hexes.put("getListLength", new Hex("", "ur,ur,ul,dl,dr,dr"));
		hexes.put("getFromList", new Hex("", "ul,l,dr,dl,r,ur"));
		hexes.put("addToList", new Hex("", "dl,l,ur,ul,r,dr"));
		hexes.put("rechargeItem", new Hex("", "ul,l,dl,dr,r,ur,ur,l,ul,dl,l,dr,dl,r,dr,ur,r,ul"));
		hexes.put("createArtifact", new Hex("", "r,r,r,ul,l,dl,dr,r,ur,r,ul,ul,l,l,dl,dl,dr,dr,r,r,ur,ur,ur,ul,l,ul,dl,ul,dl,l,dl,dr,dl,dr,r,dr,ur,dr,ur,r,ur,ul"));
		hexes.put("levitate", new Hex("", "l,dl,dr,r,ur,ul,dl,dl,dl,r,r,ul,ul"));
		hexes.put("weakness", new Hex("", "ul,l,dl,dr,r,ur,l,dl,dl,r,r,ul,ul"));
		hexes.put("breakBlock", new Hex("", "r,ur,l,dl,dr,r,ur,ul"));
		hexes.put("placeBlock", new Hex("", "dl,l,ul,ur,r,dr,l,ul"));
		hexes.put("blink", new Hex("", "dl,r,r,ur,ul,l,l,dr,r"));
		hexes.put("push", new Hex("", "dl,r,r,ur,ul,l,l,dr,r,r"));
		hexes.put("explode", new Hex("", "r,ul,dl,dl,r,ul,ul,dl,r"));
		hexes.put("normalPos", new Hex("", "ur,dr,l"));
		hexes.put("direction", new Hex("", "ur,ur,l"));
		hexes.put("myPosEye", new Hex(hexes.get("me"), hexes.get("eyePos")));
		hexes.put("myPos", new Hex(hexes.get("me"), hexes.get("normalPos")));
		hexes.put("canSave", new Hex("", "r,dl,l,ul,ur,r,dr,r"));
		hexes.put("canLoad", new Hex("", "r,ul,l,dl,dr,r,ur,r"));
		hexes.put("blockImLookingAt", new Hex(hexes.get("myPosEye"), hexes.get("me"), hexes.get("direction"), hexes.get("archer")));
		hexes.put("startFlight", new Hex(hexes.get("me"), hexes.get("meFlight"), hexes.get("me"), hexes.get("direction") , hexes.get("getNumber10"), hexes.get("multiply"), hexes.get("push")));
		
		
		
		for(String h : hexes.keySet()){
			System.out.println(h + " : " + hexes.get(h).fullSpell);
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
					fullSpell += "," + "dl,".repeat(3) + stopRecord.fullSpell + ",l".repeat(9) + ",dl,dl," + (totalStops != 1 ? combineLists.fullSpell + "," : "") + save.fullSpell + ",ps," + load.fullSpell + ",ur,ur," + startRecord.fullSpell;
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