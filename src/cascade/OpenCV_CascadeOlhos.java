package cascade;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

public class OpenCV_CascadeOlhos {
	public static CascadeClassifier cascade;
	
	static {
		cascade = new CascadeClassifier("cascades/haarcascade_eye.xml");
	}
	
	public static List<Rectangle> detecta(Mat matImagemColorida, Mat matImagemCinza, List<Rectangle> faces) {
		MatOfRect olhosDetectadas = new MatOfRect();
		cascade.detectMultiScale(matImagemCinza, olhosDetectadas, 1.1, 3, 0);
		
		List<Rectangle> olhos = new ArrayList<>();
		for(Rect olho : olhosDetectadas.toArray()) {
			for(Rectangle face : faces) {
				if(face.contains(new Point2D.Double (olho.x, olho.y)) && face.contains(new Point2D.Double (olho.x+olho.width, olho.y+olho.height))) {
					olhos.add(new Rectangle(olho.x, olho.y, olho.width, olho.height));
					break;
				}
			}
		}
		
		return olhos;
	}
}
