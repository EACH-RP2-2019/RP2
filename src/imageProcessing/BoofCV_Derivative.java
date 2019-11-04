package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.abst.filter.derivative.AnyImageDerivative;
import boofcv.alg.filter.derivative.DerivativeType;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.border.BorderType;
import boofcv.struct.image.GrayF32;

/**
 * Example showing how to compute different image derivatives using built in functions.
 *
 * @author Peter Abeles
 */
public class BoofCV_Derivative {
	public static BufferedImage transforma (BufferedImage bufferedImage, String derivativeType) {

		// We will use floating point images here, but GrayU8 with GrayS16 for derivatives also works
		GrayF32 grey = new GrayF32(bufferedImage.getWidth(),bufferedImage.getHeight());
		ConvertBufferedImage.convertFrom(bufferedImage, grey);

		// First order derivative, also known as the gradient
		GrayF32 derivX = new GrayF32(grey.width,grey.height);
		GrayF32 derivY = new GrayF32(grey.width,grey.height);

		GImageDerivativeOps.gradient(DerivativeType.SOBEL, grey, derivX, derivY, BorderType.EXTENDED);

		// Second order derivative, also known as the Hessian
		GrayF32 derivXX = new GrayF32(grey.width,grey.height);
		GrayF32 derivXY = new GrayF32(grey.width,grey.height);
		GrayF32 derivYY = new GrayF32(grey.width,grey.height);

		GImageDerivativeOps.hessian(DerivativeType.SOBEL, derivX, derivY, derivXX, derivXY, derivYY, BorderType.EXTENDED);

		// There's also a built in function for computing arbitrary derivatives
		AnyImageDerivative<GrayF32,GrayF32> derivative =
				GImageDerivativeOps.createAnyDerivatives(DerivativeType.SOBEL, GrayF32.class, GrayF32.class);

		// the boolean sequence indicates if its an X or Y derivative
		derivative.setInput(grey);
		GrayF32 derivXYX = derivative.getDerivative(true, false, true);

		switch (derivativeType) {
			case "Sobel X": return VisualizeImageData.colorizeSign(derivX, null, -1);
			case "Sobel Y": return  VisualizeImageData.colorizeSign(derivY, null, -1);
			
			// Use colors to show X and Y derivatives in one image.  Looks pretty.
			case "Sobel X and Y": return VisualizeImageData.colorizeGradient(derivX, derivY, -1, null);
			case "Sobel XX": return VisualizeImageData.colorizeSign(derivXX, null,-1);
			case "Sobel XY": return VisualizeImageData.colorizeSign(derivXY, null,-1);
			case "Sobel XYX": return VisualizeImageData.colorizeSign(derivXYX, null,-1);
				
			default: throw new RuntimeException("derivativeType: "+derivativeType+" nao eh um valor valido");
		}
	}
}
