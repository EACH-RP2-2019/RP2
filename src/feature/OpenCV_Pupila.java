package feature;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import tela.ShapeEllipse;

public class OpenCV_Pupila {
	
	public static List<ShapeEllipse> detecta(Mat matImagemColorida, Mat matImagemCinza, List<Rectangle> olhos){
	    //Imgproc.medianBlur(matImagemCinza, matImagemCinza, 3);
	    
		Mat circles = new Mat();
	    Imgproc.HoughCircles(matImagemCinza, circles, Imgproc.HOUGH_GRADIENT, 1.0,
	    		(double)matImagemCinza.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 20.0, 7, 18); // change the last two parameters
                // (min_radius & max_radius) to detect larger circles
	    
	    List<ShapeEllipse> publilas = new ArrayList<>();
	    for (int x = 0; x < circles.cols(); x++) {
	        double[] c = circles.get(0, x);
	        Point center = new Point(Math.round(c[0]), Math.round(c[1]));
	        int radius = (int) Math.round(c[2]);
	        
			for (Rectangle olho : olhos) {
				ShapeEllipse publila = new ShapeEllipse(center.x, center.y, radius);
				if(olho.contains(publila.getCentro()) && olho.height >  2*radius && olho.width >  2*radius) {
					publilas.add(publila);
					break;
				}
			}
	    }
	    
	    return publilas;
	}
}
