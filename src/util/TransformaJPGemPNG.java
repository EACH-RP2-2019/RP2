package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TransformaJPGemPNG {

	public static void main(String[] args) throws Exception {
		String diretorioEntrada = "rostos";
		String diretorioSaida = "rostos";
		for (String nome : new File(diretorioEntrada).list()) {
			BufferedImage imagem = ImageIO.read(new File(diretorioEntrada + "/" + nome));

			nome = nome.replace(".bmp", "").replace(".jpg", "").replace(".jpeg", "").replace(".png", "");

			ImageIO.write(imageToBufferedImage(imagem, imagem.getWidth(), imagem.getHeight()), "PNG",
					new File(diretorioSaida+File.separator+nome + ".png"));
		}
	}

	private static BufferedImage imageToBufferedImage(Image image, int width, int height) {
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return dest;
	}
}