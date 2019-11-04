package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.alg.filter.convolve.GConvolveImageOps;
import boofcv.core.image.border.FactoryImageBorder;
import boofcv.factory.filter.kernel.FactoryKernelGaussian;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.border.BorderType;
import boofcv.struct.border.ImageBorder;
import boofcv.struct.convolve.Kernel1D_S32;
import boofcv.struct.convolve.Kernel2D_S32;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;

/**
 * Several examples demonstrating convolution.
 *
 * @author Peter Abeles
 */
public class BoofCV_Convolution {

	/**
	 * Convolves a 1D kernel horizontally and vertically
	 */
	public static BufferedImage convolve1D(BufferedImage bufferedImage, String convolve1DType) {
		GrayU8 gray = ConvertBufferedImage.convertFromSingle(bufferedImage, null, GrayU8.class);
		
		ImageBorder<GrayU8> border = FactoryImageBorder.wrap(BorderType.EXTENDED, gray);
		Kernel1D_S32 kernel = new Kernel1D_S32(2);
		kernel.offset = 1; // specify the kernel's origin
		kernel.data[0] = 1;
		kernel.data[1] = -1;

		GrayS16 output = new GrayS16(gray.width,gray.height);

		switch (convolve1DType) {
			case "1D Horizontal":{
				GConvolveImageOps.horizontal(kernel, gray, output, border);
				return VisualizeImageData.standard(output, null);
			}
			case "1D Vertical": {
				GConvolveImageOps.vertical(kernel, gray, output, border);
				return VisualizeImageData.standard(output, null);
			}
				
			default: throw new RuntimeException("convolve1DType: "+convolve1DType+" nao eh um valor valido");
		}
	}

	/**
	 * Convolves a 2D kernel
	 */
	public static BufferedImage convolve2D(BufferedImage image) {
		GrayU8 gray = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);
		
		// By default 2D kernels will be centered around width/2
		Kernel2D_S32 kernel = new Kernel2D_S32(3);
		kernel.set(1,0,2);
		kernel.set(2,1,2);
		kernel.set(0,1,-2);
		kernel.set(1,2,-2);

		// Output needs to handle the increased domain after convolution.  Can't be 8bit
		GrayS16 output = new GrayS16(gray.width,gray.height);
		ImageBorder<GrayU8> border = FactoryImageBorder.wrap( BorderType.EXTENDED,gray);

		GConvolveImageOps.convolve(kernel, gray, output, border);
		return VisualizeImageData.standard(output, null);
	}

	/**
	 * Convolves a 2D normalized kernel.  This kernel is divided by its sum after computation.
	 */
	public static BufferedImage normalize2D(BufferedImage image) {
		GrayU8 gray = ConvertBufferedImage.convertFromSingle(image, null, GrayU8.class);
		
		// Create a Gaussian kernel with radius of 3
		Kernel2D_S32 kernel = FactoryKernelGaussian.gaussian2D(GrayU8.class, -1, 3);
		// Note that there is a more efficient way to compute this convolution since it is a separable kernel
		// just use BlurImageOps instead.

		// Since it's normalized it can be saved inside an 8bit image
		GrayU8 output = new GrayU8(gray.width,gray.height);

		GConvolveImageOps.convolveNormalized(kernel, gray, output);
		return VisualizeImageData.standard(output, null);
	}
}
