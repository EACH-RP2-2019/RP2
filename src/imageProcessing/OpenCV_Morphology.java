package imageProcessing;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import util.CriaMensagem;
import util.SystemSetProperty;

public class OpenCV_Morphology {
	public static enum Type {
		RECTANGLE, CROSS, ELLIPSE;
		
		public int valor () {
			switch (this) {
				case RECTANGLE: return Imgproc.CV_SHAPE_RECT;
				case CROSS: return Imgproc.CV_SHAPE_CROSS;
				case ELLIPSE: return Imgproc.CV_SHAPE_ELLIPSE;
			}
			return -1;
		}
	}
	
	public static enum Morph_op {
		EROSION, DILATATION;
		
		public boolean isErosion () {
			return this.equals(EROSION);
		}
	}

	public static Mat transforma(Mat src, Type elementType, int kernelSize, Morph_op Morph_op) {
		if(kernelSize > 21) throw new RuntimeException("kernelSize nao pode ser maior que 21");
		
		Mat dst = new Mat();
		
		Mat element = Imgproc.getStructuringElement(elementType.valor(), new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
				new Point(kernelSize, kernelSize));
		if (Morph_op.isErosion()) {
			Imgproc.erode(src, dst, element);
		} else {
			Imgproc.dilate(src, dst, element);
		}
		
		return dst;
	}

	public static void main(String[] args) throws IOException {
		SystemSetProperty.setProperty("C:/opencv/build/java/x64");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String imagePath = "rostos/carol1.jpeg";
		Mat mat = Imgcodecs.imread(imagePath);
		//BufferedImage bufferedImagemMonocromatica = BoofCV_Thresholding.transforma(Transforma.transformaEmBufferedImage(mat), "NICK");
		//BufferedImage bufferedImagemMonocromatica = BoofCV_Thresholding.transforma(Transforma.transformaEmBufferedImage(mat), "Sauvola");
		//mat = Transforma.transformaEmMat(bufferedImagemMonocromatica);
		
		Mat matImagemCinza = new Mat();
		Imgproc.cvtColor(mat, matImagemCinza, Imgproc.COLOR_BGR2GRAY);
		mat = OpenCV_Threshold.transforma(matImagemCinza, 80, OpenCV_Threshold.Type.BINARY_INVERTED);
		
		//mat = transforma(mat, Type.ELLIPSE, 1, Morph_op.DILATATION);
		//mat = transforma(mat, Type.ELLIPSE, 1, Morph_op.EROSION);
		
		CriaMensagem.messagemImagem(mat);
	}
}
