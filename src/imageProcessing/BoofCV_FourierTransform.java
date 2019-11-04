package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.abst.transform.fft.DiscreteFourierTransform;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.misc.PixelMath;
import boofcv.alg.transform.fft.DiscreteFourierTransformOps;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.InterleavedF32;

/**
 * Example demonstrating how to compute the Discrete Fourier Transform,
 * visualize the transform, and apply a filter frequency domain.
 *
 * @author Peter Abeles
 */
public class BoofCV_FourierTransform {

	/**
	 * Demonstration of how to apply a box filter in the frequency domain and
	 * compares the results to a box filter which has been applied in the spatial
	 * domain
	 */
	public static BufferedImage transforma (BufferedImage bufferedImage, String fourierTransformType) {
		GrayF32 input = ConvertBufferedImage.convertFromSingle(bufferedImage, null, GrayF32.class);

		// declare storage
		GrayF32 boxImage = new GrayF32(input.width, input.height);
		InterleavedF32 boxTransform = new InterleavedF32(input.width, input.height, 2);
		InterleavedF32 transform = new InterleavedF32(input.width, input.height, 2);
		GrayF32 blurredImage = new GrayF32(input.width, input.height);
		GrayF32 spatialBlur = new GrayF32(input.width, input.height);

		DiscreteFourierTransform<GrayF32, InterleavedF32> dft = DiscreteFourierTransformOps.createTransformF32();

		// Make the image scaled from 0 to 1 to reduce overflow issues
		PixelMath.divide(input, 255.0f, input);

		// compute the Fourier Transform
		dft.forward(input, transform);

		// create the box filter which is centered around the pixel. Note that the
		// filter gets wrapped around
		// the image edges
		for (int y = 0; y < 15; y++) {
			int yy = y - 7 < 0 ? boxImage.height + (y - 7) : y - 7;
			for (int x = 0; x < 15; x++) {
				int xx = x - 7 < 0 ? boxImage.width + (x - 7) : x - 7;
				// Set the value such that it doesn't change the image intensity
				boxImage.set(xx, yy, 1.0f / (15 * 15));
			}
		}

		// compute the DFT for the box filter
		dft.forward(boxImage, boxTransform);

		// apply the filter. convolution in spacial domain is the same as multiplication
		// in the frequency domain
		DiscreteFourierTransformOps.multiplyComplex(transform, boxTransform, transform);

		// convert the image back and display the results
		dft.inverse(transform, blurredImage);
		// undo change of scale
		PixelMath.multiply(blurredImage, 255.0f, blurredImage);
		PixelMath.multiply(input, 255.0f, input);

		// For sake of comparison, let's compute the box blur filter in the spatial
		// domain
		// NOTE: The image border will be different since the frequency domain wraps
		// around and this implementation
		// of the spacial domain adapts the kernel size
		BlurImageOps.mean(input, spatialBlur, 7, null, null);
		
		switch (fourierTransformType) {
			case "Spacial Domain Box": {
				return ConvertBufferedImage.convertTo(spatialBlur, null);
			}
			case "Frequency Domain Box": {
				return ConvertBufferedImage.convertTo(blurredImage, null);
			}
			default: throw new RuntimeException("fourierTransformType: "+fourierTransformType+" nao eh um valor valido");
		}
	}
}
