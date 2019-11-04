

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.Mat;

import com.github.sarxos.webcam.Webcam;

import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.webcamcapture.UtilWebcamCapture;
import imageProcessing.BoofCV_Thresholding;
import imageProcessing.OpenCV_Morphology;
import util.Transforma;

public class WebCamBoofBinarioEstavel {


	public static void main(String[] args) throws IOException {
		Webcam webcam = UtilWebcamCapture.openDefault(640,480);

		ImagePanel gui = new ImagePanel();
		gui.setPreferredSize(webcam.getViewSize());
		ShowImages.showWindow(gui,"KLT Tracker",true);
		
		while(true) {
			BufferedImage image = inverteImagenBinarias(
					BoofCV_Thresholding.transforma(webcam.getImage(), "NICK3"),
					BoofCV_Thresholding.transforma(webcam.getImage(), "NICK3"),
					BoofCV_Thresholding.transforma(webcam.getImage(), "NICK3"));
			
			Mat mat = OpenCV_Morphology.transforma(Transforma.transformaEmMat2(image), OpenCV_Morphology.Type.ELLIPSE, 2, OpenCV_Morphology.Morph_op.DILATATION);
			gui.setImageRepaint(Transforma.transformaEmBufferedImage(mat));
		}
	}
	
	public static BufferedImage inverteImagenBinarias (BufferedImage a, BufferedImage b, BufferedImage c) {
		final int width = a.getWidth(); 
		final int height = a.getHeight();
		
		final int branco = Color.WHITE.getRGB();
		final int preto = Color.BLACK.getRGB();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				a.setRGB(x, y, 
						a.getRGB(x, y) == branco || b.getRGB(x, y) == branco || b.getRGB(x, y) == branco
						? branco : preto);
			}
		}
		
		return a;
	}
}
