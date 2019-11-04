package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Set;

public class TrocaCores {
	
	public static BufferedImage troca(BufferedImage bufferedImage, Set<Integer> rgbsCor1, Color cor1, Color cor2) {
		BufferedImage copia = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				bufferedImage.getType());
		Graphics g = copia.getGraphics();
		g.drawImage(bufferedImage, 0, 0, null);
		g.dispose();

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (rgbsCor1.contains(bufferedImage.getRGB(x, y))) {
					copia.setRGB(x, y, cor1.getRGB());
				} else copia.setRGB(x, y, cor2.getRGB());
			}
		}
		return copia;
	}
	
	public static BufferedImage troca(BufferedImage bufferedImage, Set<Integer> rgbsCor1, int tolerancia, Color cor1, Color cor2) {
		BufferedImage copia = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				bufferedImage.getType());
		Graphics g = copia.getGraphics();
		g.drawImage(bufferedImage, 0, 0, null);
		g.dispose();

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color cor = new Color(bufferedImage.getRGB(x, y));
				
				boolean semelhante = false;
				
				for (Integer rgbCor1 : rgbsCor1) {
					if(testaSemelhanca (tolerancia, cor, new Color (rgbCor1))) {
						semelhante = true;
						break;
					}
				}
				if (semelhante) {
					copia.setRGB(x, y, cor1.getRGB());
				} else copia.setRGB(x, y, cor2.getRGB());
			}
		}
		return copia;
	}
	
	public static boolean testaSemelhanca (int tolerancia, Color corA, Color corB) {
		int red = corA.getRed() - corB.getRed();
		if(red < 0) red *=-1;
		
		int green = corA.getGreen() - corB.getGreen();
		if(green < 0) green *=-1;
		
		int blue = corA.getBlue() - corB.getBlue();
		if(blue < 0) blue *=-1;
		
		return red + green + blue <= tolerancia;
	}
	
	public static void main(String[] args) {
		System.out.println(testaSemelhanca (2, new Color (230, 1, 2), new Color (240, 0, 0)));
	}
}
