package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCV_Threshold {
	
	public static enum Type {
		BINARY, BINARY_INVERTED, TRUNCATE, ZERO, ZERO_INVERTED;
		
		public int valor () {
			switch (this) {
				case BINARY: return 0;
				case BINARY_INVERTED: return 1;
				case TRUNCATE: return 2;
				case ZERO: return 3;
				case ZERO_INVERTED: return 4;
			}
			return -1;
		}
	}
	
	public static Mat transforma(Mat srcGray, int thresholdValue, Type thresholdType) {
		final int MAX_BINARY_VALUE = 255;
		
		Mat dst = new Mat();
		Imgproc.threshold(srcGray, dst, thresholdValue, MAX_BINARY_VALUE, thresholdType.valor());
		return dst;
	}
}
