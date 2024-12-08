import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

class HexCompilerImage{
	
	public static int mapWidth = 18;
	public static int mapHeight = 11;

	public static void main(String... args) throws Exception {
		NodeMap map = new NodeMap(mapWidth, mapHeight);
		
		// map.click(true);
		map.moveCursorToTopLeft();
		// map.click(false);
		map.addSpell("createArtifact");
		System.out.println(map.getFullSpell());
		map.displayNodeMap();


	}
	

}