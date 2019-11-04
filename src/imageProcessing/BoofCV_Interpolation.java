package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.alg.interpolate.InterpolatePixelS;
import boofcv.alg.interpolate.InterpolationType;
import boofcv.factory.interpolate.FactoryInterpolation;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.border.BorderType;
import boofcv.struct.image.GrayF32;

/**
 * Interpolation is used to convert an image, which is discrete by its nature, into a (piecewise) smooth function.
 * Interpolation is in many CV applications, such as feature detection, and when distorting images.  In this
 * example a low resolution is scaled up using several different techniques to make the differences easily visible.
 * For computer vision applications bilinear interpolation is almost always used.
 *
 * @author Peter Abeles
 */
public class BoofCV_Interpolation {

	public static BufferedImage transforma(BufferedImage buffered, InterpolationType type) {
		// For sake of simplicity assume it's a gray scale image.  Interpolation functions exist for planar and
		// interleaved color images too
		GrayF32 input  = ConvertBufferedImage.convertFrom(buffered, (GrayF32)null);
		GrayF32 scaled = input.createNew(500,500*input.height/input.width);

			// Create the single band (gray scale) interpolation function for the input image
			InterpolatePixelS<GrayF32> interp = FactoryInterpolation.
					createPixelS(0,255,type,BorderType.EXTENDED,input.getDataType());

			// Tell it which image is being interpolated
			interp.setImage(input);

			// Manually apply scaling to the input image.  See FDistort() for a built in function which does
			// the same thing and is slightly more efficient
			for (int y = 0; y < scaled.height; y++) {
				// iterate using the 1D index for added performance.  Altertively there is the set(x,y) operator
				int indexScaled = scaled.startIndex + y*scaled.stride;
				float origY = y*input.height/(float)scaled.height;

				for (int x = 0; x < scaled.width; x++) {
					float origX = x*input.width/(float)scaled.width;

					scaled.data[indexScaled++] = interp.get(origX,origY);
				}
			}
			// Add the results to the output
		return  ConvertBufferedImage.convertTo(scaled,null,true);
	}
}
