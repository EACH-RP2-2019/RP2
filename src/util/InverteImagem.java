package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class InverteImagem {
	public static BufferedImage horizontal(BufferedImage bufferedImage) {  
        int w = bufferedImage.getWidth();  
        int h = bufferedImage.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, bufferedImage.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(bufferedImage, 0, 0, w, h, w, 0, 0, h, null);  
        g.dispose();  
        return dimg;  
    } 
	
	public static BufferedImage vertical(BufferedImage bufferedImage) {  
        int w = bufferedImage.getWidth();  
        int h = bufferedImage.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, bufferedImage.getColorModel().getTransparency());  
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(bufferedImage, 0, 0, w, h, 0, h, w, 0, null);  
        g.dispose();  
        return dimg;  
    } 
	
	public static Mat horizontal(Mat mat) {  
		Mat invertida = new Mat();
		Core.flip(mat, invertida, 1);
		
		return invertida;
    } 
	
	public static Mat vertical(Mat mat) {  
		Mat invertida = new Mat();
		Core.flip(mat, invertida, -1);
		
		return invertida;
    }
}
