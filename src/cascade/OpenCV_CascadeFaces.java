package cascade;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

public class OpenCV_CascadeFaces {
	public static CascadeClassifier cascade;
	
	static {
		cascade = new CascadeClassifier("cascades/haarcascade_frontalface_default.xml");
	}
	
	public static List<Rectangle> detecta(Mat matImagemColorida, Mat matImagemCinza) {
		MatOfRect facesDetectadas = new MatOfRect();
		cascade.detectMultiScale(matImagemCinza, facesDetectadas,
				1.05, // Scale Factor
				3,   // MinNeighbors
				0,   // flags
				new Size(30, 30), // minSize
				new Size(500, 500)  // maxSize
				);
		
		List<Rectangle> faces = new ArrayList<>();
		for (Rect face : facesDetectadas.toArray()) {
			faces.add(new Rectangle(face.x, face.y, face.width, face.height));
		}
		
		return faces;
	}
}
