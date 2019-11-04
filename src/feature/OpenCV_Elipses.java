package feature;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import imageProcessing.OpenCV_Smoothing;
import tela.ShapeEllipse;

public class OpenCV_Elipses {
	public static List<ShapeEllipse> detecta(Mat matImagemColorida, Mat matImagemMonocromatica, List<Rectangle> olhos) {
		Mat cannyOutput = new Mat();

		//Imgproc.blur(matImagemMonocromatica, matImagemMonocromatica, new Size(3, 3));
		matImagemMonocromatica = OpenCV_Smoothing.transforma(matImagemMonocromatica, "Median", 1);
		
		//int limite = 212;
		int limite = 80;
		Imgproc.Canny(matImagemMonocromatica, cannyOutput, limite, limite * 2);
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

		List<ShapeEllipse> elipses = new ArrayList<>();
		for (MatOfPoint contour : contours) {
			if (contour.rows() > 5) {
				RotatedRect minEllipse = Imgproc.fitEllipse(new MatOfPoint2f(contour.toArray()));

				double height = minEllipse.size.height;
				double width = minEllipse.size.width;
				if(height <= width * 1.5 && width <= height * 1.5) {
					for (Rectangle olho : olhos) {
						ShapeEllipse elipse = new ShapeEllipse(minEllipse.center.x, minEllipse.center.y, minEllipse.size.width, minEllipse.size.height);
						if(olho.contains(elipse.getCentro()) && olho.height >  height && olho.width >  width) {
							elipses.add(elipse);
							break;
						}
					}
				}
			}
		}
		
		return elipses;
	}
}
