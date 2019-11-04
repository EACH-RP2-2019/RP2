package imageProcessing;

import java.awt.image.BufferedImage;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConfigLength;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import util.GetPath;

/**
 * Demonstration of different techniques for automatic thresholding an image to
 * create a binary image. The binary image can then be used for shape analysis
 * and other applications. Global methods apply the same threshold to the entire
 * image. Local methods compute a local threshold around each pixel and can
 * handle uneven lighting, but produce noisy results in regions with uniform
 * lighting.
 *
 * @see boofcv.BoofCV_ExampleBinaryOps.imageprocessing.ExampleBinaryOps
 *
 * @author Peter Abeles
 */
public class BoofCV_Thresholding {

	public static BufferedImage transforma(BufferedImage bufferedImage, String thresholdingType) {
		// convert into a usable format
		GrayF32 input = ConvertBufferedImage.convertFromSingle(bufferedImage, null, GrayF32.class);
		GrayU8 binary = new GrayU8(input.width, input.height);

		switch (thresholdingType) {
		// Global Methods
		case "Mean": {
			GThresholdImageOps.threshold(input, binary, ImageStatistics.mean(input), true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}

		case "Otsu": {
			GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeOtsu(input, 0, 255), true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "Entropy": {
			GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeEntropy(input, 0, 255), true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}

		// Local method
		case "Square": {
			GThresholdImageOps.localMean(input, binary, ConfigLength.fixed(57), 1.0, true, null, null, null);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "Block Min-Max": {
			GThresholdImageOps.blockMinMax(input, binary, ConfigLength.fixed(21), 1.0, true, 15);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "Block Mean": {
			GThresholdImageOps.blockMean(input, binary, ConfigLength.fixed(21), 1.0, true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "Block Otsu": {
			GThresholdImageOps.blockOtsu(input, binary, false, ConfigLength.fixed(21), 0.5, 1.0, true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "Gaussian": {
			GThresholdImageOps.localGaussian(input, binary, ConfigLength.fixed(85), 1.0, true, null, null);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "Sauvola": {
			GThresholdImageOps.localSauvola(input, binary, ConfigLength.fixed(11), 0.30f, true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "NICK": {
			GThresholdImageOps.localNick(input, binary, ConfigLength.fixed(11), -0.2f, true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "NICK2": {
			GThresholdImageOps.localNick(input, binary, ConfigLength.fixed(11), -0.4f, true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		case "NICK3": {
			GThresholdImageOps.localNick(input, binary, ConfigLength.fixed(11), -0.35f, true);
			return VisualizeBinaryData.renderBinary(binary, false, null);
		}
		default:
			throw new RuntimeException("O Método " + thresholdingType + " não é um método válido");
		}
	}

	public static void main(String[] args) {
		transforma(UtilImageIO.loadImage(UtilIO.pathExample(GetPath.path("carol1.jpeg"))), "NICK");
	}
}
