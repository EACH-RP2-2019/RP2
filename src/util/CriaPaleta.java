package util;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class CriaPaleta {	
	public static Set<Integer> getRgbsDentro (BufferedImage bufferedImage, Collection<Shape> shapes) {
		Set<Integer> rgbs = new TreeSet<>();
		
		int width = bufferedImage.getWidth(); 
		int height = bufferedImage.getHeight();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (Shape shape : shapes) {
					if(shape.contains(x, y)) {
						rgbs.add(bufferedImage.getRGB(x, y));
						break;
					}
				}
			}
		}
		
		return rgbs;
	}
}
