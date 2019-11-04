package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.alg.enhance.EnhanceImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.gui.ListDisplayPanel;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;

/**
 * Demonstration of various ways an image can be "enhanced". Image enhancement
 * typically refers to making it easier for people to view the image and pick
 * out its details.
 *
 * @author Peter Abeles
 */
public class BoofCV_Enhancement {

	/**
	 * Histogram adjustment algorithms aim to spread out pixel intensity values
	 * uniformly across the allowed range. This if an image is dark, it will have
	 * greater contrast and be brighter.
	 */
	public static BufferedImage histogram(BufferedImage buffered) {
		GrayU8 gray = ConvertBufferedImage.convertFrom(buffered, (GrayU8) null);
		GrayU8 adjusted = gray.createSameShape();

		int histogram[] = new int[256];
		int transform[] = new int[256];

		ListDisplayPanel panel = new ListDisplayPanel();

		ImageStatistics.histogram(gray, 0, histogram);
		EnhanceImageOps.equalize(histogram, transform);
		EnhanceImageOps.applyTransform(gray, transform, adjusted);
		panel.addImage(ConvertBufferedImage.convertTo(adjusted, null), "Global");

		EnhanceImageOps.equalizeLocal(gray, 50, adjusted, 256, null);
		panel.addImage(ConvertBufferedImage.convertTo(adjusted, null), "Local");

		return ConvertBufferedImage.convertTo(gray, null);
	}

	/**
	 * When an image is sharpened the intensity of edges are made more extreme while
	 * flat regions remain unchanged.
	 */
	public static BufferedImage sharpen(BufferedImage buffered) {
		GrayU8 gray = ConvertBufferedImage.convertFrom(buffered, (GrayU8) null);
		GrayU8 adjusted = gray.createSameShape();

		ListDisplayPanel panel = new ListDisplayPanel();

		EnhanceImageOps.sharpen4(gray, adjusted);
		panel.addImage(ConvertBufferedImage.convertTo(adjusted, null), "Sharpen-4");

		EnhanceImageOps.sharpen8(gray, adjusted);
		panel.addImage(ConvertBufferedImage.convertTo(adjusted, null), "Sharpen-8");

		return ConvertBufferedImage.convertTo(gray, null);
	}
}
