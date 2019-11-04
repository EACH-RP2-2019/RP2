package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.Planar;

/**
 * This example shows you can can apply different standard image blur filters to an input image using different
 * interface.  For repeat calls using the filter interface has some advantages, but the procedural can be
 * simple to code up.
 *
 * @author Peter Abeles
 */
public class BoofCV_ImageBlur {

	public static BufferedImage transforma (BufferedImage buffered, String blurType) {
		Planar<GrayU8> input = ConvertBufferedImage.convertFrom(buffered, true, ImageType.pl(3, GrayU8.class));
		Planar<GrayU8> blurred = input.createSameShape();

		// size of the blur kernel. square region with a width of radius*2 + 1
		int radius = 8;

		switch (blurType) {
			case "Gaussian": {
				// Apply gaussian blur using a procedural interface
				GBlurImageOps.gaussian(input,blurred,-1,radius,null);
				return ConvertBufferedImage.convertTo(blurred, null, true);
			}
			case "Mean": {
				// Apply a mean filter using an object oriented interface.  This has the advantage of automatically
				// recycling memory used in intermediate steps
				BlurFilter<Planar<GrayU8>> filterMean = FactoryBlurFilter.mean(input.getImageType(),radius);
				filterMean.process(input, blurred);
				return ConvertBufferedImage.convertTo(blurred, null, true);
			}
			case "Median": {
				// Apply a median filter using image type specific procedural interface.  Won't work if the type
				// isn't known at compile time
				BlurImageOps.median(input,blurred,radius, null);
				return ConvertBufferedImage.convertTo(blurred, null, true);
			}
			default: throw new RuntimeException("blurType: "+blurType+" nao eh um valor valido");
		}
	}
}
