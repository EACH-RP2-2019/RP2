package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class OpenCV_Smoothing {
	public static Mat transforma(Mat mat, String smoothingType, int size) {
		final int MAX_KERNEL_LENGTH = 31;
		if (size % 2 != 1 || size < 1 || size >= MAX_KERNEL_LENGTH)
			throw new RuntimeException("size = " + size + " nao eh um valor validos, " + "somente valores impares entre 1 e "
					+ MAX_KERNEL_LENGTH + " sao validos");
		
		Mat dst = new Mat();
		switch (smoothingType) {
			case "Homogeneous":
				Imgproc.blur(mat, dst, new Size(size, size), new Point(-1, -1));
				break;
			case "Gaussian":
				Imgproc.GaussianBlur(mat, dst, new Size(size, size), 0, 0);
				break;
			case "Median":
				Imgproc.medianBlur(mat, dst, size);
				break;
			case "Bilateral":
				Imgproc.bilateralFilter(mat, dst, size, size * 2, size / 2);
				break;
				
			default: throw new RuntimeException("smoothingType: "+smoothingType+" nao eh um valor valido");
		}
		
		return dst;
	}
}
