package util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Transforma {

	public static BufferedImage transformaEmBufferedImage(Mat mat) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (mat.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}

		int bufferSize = mat.channels() * mat.cols() * mat.rows();
		byte[] bytes = new byte[bufferSize];
		mat.get(0, 0, bytes);
		BufferedImage imagem = new BufferedImage(mat.cols(), mat.rows(), type);
		byte[] targetPixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		System.arraycopy(bytes, 0, targetPixels, 0, bytes.length);
		return imagem;
	}

	public static Mat transformaEmMat(BufferedImage bufferedImage) throws IOException {
		Mat out = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
		byte[] data = new byte[bufferedImage.getWidth() * bufferedImage.getHeight() * (int) out.elemSize()];
		int[] dataBuff = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
		for (int i = 0; i < dataBuff.length; i++) {
			data[i * 3] = (byte) ((dataBuff[i]));
			data[i * 3 + 1] = (byte) ((dataBuff[i]));
			data[i * 3 + 2] = (byte) ((dataBuff[i]));
		}
		out.put(0, 0, data);
		return out;
	}
	
	public static Mat transformaEmMat2(BufferedImage bufferedImage) throws IOException {
		Mat out = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_32F);
		byte[] data = new byte[bufferedImage.getWidth() * bufferedImage.getHeight() * (int) out.elemSize()];
		int[] dataBuff = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
		for (int i = 0; i < dataBuff.length; i++) {
			data[i * 3] = (byte) ((dataBuff[i]));
			data[i * 3 + 1] = (byte) ((dataBuff[i]));
			data[i * 3 + 2] = (byte) ((dataBuff[i]));
		}
		out.put(0, 0, data);
		return out;
	}
}
