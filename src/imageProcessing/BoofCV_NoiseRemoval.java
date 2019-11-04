package imageProcessing;
import java.awt.image.BufferedImage;
import java.util.Random;

import boofcv.abst.denoise.FactoryImageDenoise;
import boofcv.abst.denoise.WaveletDenoiseFilter;
import boofcv.alg.misc.GImageMiscOps;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;

/**
 * Example of how to "remove" noise from images using wavelet based algorithms.  A simplified interface is used
 * which hides most of the complexity.  Wavelet image processing is still under development and only floating point
 * images are currently supported.  Which is why the image  type is hard coded.
 */
public class BoofCV_NoiseRemoval {

	public static BufferedImage transforma(BufferedImage bufferedImage, String noiseRemovalType) {

		// load the input image, declare data structures, create a noisy image
		Random rand = new Random(234);
		GrayF32 input = ConvertBufferedImage.convertFromSingle(bufferedImage, null, GrayF32.class);

		GrayF32 noisy = input.clone();
		GImageMiscOps.addGaussian(noisy, rand, 20, 0, 255);
		GrayF32 denoised = noisy.createSameShape();

		// How many levels in wavelet transform
		int numLevels = 4;
		// Create the noise removal algorithm
		WaveletDenoiseFilter<GrayF32> denoiser =
				FactoryImageDenoise.waveletBayes(GrayF32.class,numLevels,0,255);

		// remove noise from the image
		denoiser.process(noisy,denoised);

		switch (noiseRemovalType) {
			case "Input": {
				return ConvertBufferedImage.convertTo(input,null);
			}
			case "Noisy": {
				return ConvertBufferedImage.convertTo(noisy,null);
			}
			case "Denoised": {
				return ConvertBufferedImage.convertTo(denoised,null);
			}
			default: throw new RuntimeException("noiseRemovalType: "+noiseRemovalType+" nao eh um valor valido");
		}
	}
}
