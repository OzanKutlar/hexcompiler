import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Node{
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
