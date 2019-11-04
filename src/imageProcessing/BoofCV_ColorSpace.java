package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.color.ColorYuv;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.Planar;

/**
 * Simple demonstration for converting between color spaces in BoofCV. Currently RGB, YUV, HSV, and YCbCr are
 * supported.
 *
 * @author Peter Abeles
 */
public class BoofCV_ColorSpace {

	public static String get(BufferedImage bufferedImage) {
		StringBuilder retorno = new StringBuilder();

		// Convert input image into a BoofCV RGB image
		Planar<GrayF32> rgb = ConvertBufferedImage.convertFromPlanar(bufferedImage, null,true, GrayF32.class);

		//---- convert RGB image into different color formats
		Planar<GrayF32> hsv = rgb.createSameShape();
		ColorHsv.rgbToHsv(rgb, hsv);

		Planar<GrayF32> yuv = rgb.createSameShape();
		ColorYuv.yuvToRgb(rgb, yuv);

		//---- Convert individual pixels into different formats
		float[] pixelHsv = new float[3];
		ColorHsv.rgbToHsv(10,50.6f,120,pixelHsv);
		retorno.append(String.format("Found RGB->HSV = %5.2f %5.3f %5.1f\n",pixelHsv[0],pixelHsv[1],pixelHsv[2]));

		float[] pixelRgb = new float[3];
		ColorHsv.hsvToRgb(pixelHsv[0],pixelHsv[1],pixelHsv[2],pixelRgb);
		retorno.append(String.format("Found HSV->RGB = %5.1f %5.1f %5.1f expected 10 50.6 120\n",
				pixelRgb[0],pixelRgb[1],pixelRgb[2]));

		float[] pixelYuv = new float[3];
		ColorYuv.rgbToYuv(10,50.6f,120,pixelYuv);
		retorno.append(String.format("Found RGB->YUV = %5.1f %5.1f %5.1f\n",pixelYuv[0],pixelYuv[1],pixelYuv[2]));

		ColorYuv.yuvToRgb(pixelYuv[0],pixelYuv[1],pixelYuv[2],pixelRgb);
		retorno.append(String.format("Found YUV->RGB = %5.1f %5.1f %5.1f expected 10 50.6 120\n",
				pixelRgb[0],pixelRgb[1],pixelRgb[2]));
		
		return retorno.toString();
	}
}
