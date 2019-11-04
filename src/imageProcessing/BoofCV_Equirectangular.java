package imageProcessing;
import java.awt.image.BufferedImage;

import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.spherical.CameraToEquirectangular_F32;
import boofcv.alg.interpolate.InterpolatePixel;
import boofcv.alg.interpolate.InterpolationType;
import boofcv.factory.distort.FactoryDistort;
import boofcv.factory.interpolate.FactoryInterpolation;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.border.BorderType;
import boofcv.struct.calib.CameraPinhole;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.Planar;
import georegression.geometry.ConvertRotation3D_F32;
import georegression.struct.EulerType;

/**
 * Demonstration for how to project a synthetic pinhole camera view given an equirectangular image. To
 * specify the projection the camera model needs to be configured and the orientation of the view needs to bet set.
 *
 * @author Peter Abeles
 */
public class BoofCV_Equirectangular {
	public static BufferedImage transforma (BufferedImage buffered, String equirectangularType) {

		// Specify what the pinhole camera should look like
		CameraPinhole pinholeModel = new CameraPinhole(200,200,0,250,250,500,500);

		Planar<GrayU8> equiImage =
				ConvertBufferedImage.convertFrom(buffered, true, ImageType.pl(3,GrayU8.class));

		// Declare storage for pinhole camera image
		Planar<GrayU8> pinholeImage = equiImage.createNew(pinholeModel.width, pinholeModel.height);

		// Create the image distorter which will render the image
		InterpolatePixel<Planar<GrayU8>> interp = FactoryInterpolation.
				createPixel(0, 255, InterpolationType.BILINEAR, BorderType.EXTENDED, equiImage.getImageType());
		ImageDistort<Planar<GrayU8>,Planar<GrayU8>> distorter =
				FactoryDistort.distort(false,interp,equiImage.getImageType());

		// This is where the magic is done.  It defines the transform rfom equirectangular to pinhole
		CameraToEquirectangular_F32 pinholeToEqui = new CameraToEquirectangular_F32();
		pinholeToEqui.setEquirectangularShape(equiImage.width,equiImage.height);
		pinholeToEqui.setCameraModel(pinholeModel);

		// Pass in the transform to the image distorter
		distorter.setModel(pinholeToEqui);

		// change the orientation of the camera to make the view better
		ConvertRotation3D_F32.eulerToMatrix(EulerType.YXZ,0, 1.45f, 2.2f,pinholeToEqui.getRotation());

		// Let's look at another view
		ConvertRotation3D_F32.eulerToMatrix(EulerType.YXZ,0, 1.25f, -1.25f,pinholeToEqui.getRotation());


		switch (equirectangularType) {
			case "Pinehole View 0": {
				distorter.apply(equiImage,pinholeImage);
				return ConvertBufferedImage.convertTo(pinholeImage,null,true);
			}
			case "Pinehole View 1": {
				distorter.apply(equiImage,pinholeImage);
				return ConvertBufferedImage.convertTo(pinholeImage,null,true);
			}
			case "Equirectangular": {
				return buffered;
			}
			default: throw new RuntimeException("noiseRemovalType: "+equirectangularType+" nao eh um valor valido");
		}
	}
}
