class hexCompiler{
	
	public static int maxLength = 34;

	public static void main(String... Args){
		// Hex me = new Hex("Mind's Reflection", "ur,ul,dl,dr", "");
		// Hex me = new Hex("Archer's Distillation", "r,r,ur,l,dr,dr,l,ur", "");
		Hex me = new Hex("Hermes' Gambit", "dr,l,ul,dl,dr,r", "");
		System.out.println(me.fullSpell);
	}
	
	public static class Hex{
		int[] hexBoundingBox;
		int hexLength;
		String spellName;
		String fullSpell;
		
		public Hex(String spellName, String spell, String middleToStart){
			this.spellName = spellName;
			int[] offSetFromStart = calculateBoundingBox(spell);
			String startOffset = "r,".repeat(hexBoundingBox[0] + 1);
			String returnMoves = calculateReturnToStart(offSetFromStart);
			returnMoves += ",r".repeat(hexBoundingBox[1] + 1);
			fullSpell = startOffset + "c1," + spell + ",c0" + returnMoves;
		}
		
		
		
		private String calculateReturnToStart(int[] offset){
			int xOffset = offset[0];
			int yOffset = offset[1];
			String returnMoves = "";
			if(yOffset != 0){
				returnMoves += (yOffset > 0 ? "d" : "u") + (xOffset > 0 ? "l" : "r");
				yOffset += (yOffset > 0 ? -1 : 1);
				xOffset += (xOffset > 0 ? -1 : 1);
			}
			else{
				returnMoves += (xOffset > 0 ? "l" : "r");
				xOffset += (xOffset > 0 ? -2 : 2);
			}
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
			hexLength = hexBoundingBox[1] - hexBoundingBox[0];
			return startOffset;
		}
	}
}