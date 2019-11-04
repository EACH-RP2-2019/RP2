package tela.strategy.tipoDeTela;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;

import tela.Tela;
import util.InverteImagem;
import util.Transforma;

public class WebCamEstavel implements StrategyTipoDeTela {

	private Thread thread;
	private AtomicBoolean continua;
	private VideoCapture capitura;

	@Override
	public void inicia(Tela tela) {
		continua = new AtomicBoolean();
		continua.set(true);
		
		thread = new Thread (new Runnable() {
			@Override
			public void run() {
				Mat video = new Mat();
				capitura = new VideoCapture(0);

				if(capitura.isOpened()) {
					Mat a = null;
					Mat b = null;
					Mat c = null;

					while(continua.get()) {
						capitura.read(video);
						if(!video.empty()) {
							if(a == null) {
								a = new Mat();
								continue;
							}
							if(b == null) {
								b = new Mat();
								continue;
							}
							c = new Mat();
							
							Mat matImagemColorida = join(a, b, c);
							a = null;
							b = null;
							c = null;
							
							matImagemColorida = InverteImagem.horizontal(matImagemColorida);
							
							BufferedImage bufferedImageColorida = Transforma.transformaEmBufferedImage(matImagemColorida);
							
							try {
								tela.imprimeImagem(matImagemColorida, bufferedImageColorida);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		thread.start();
	}

	public static Mat join(Mat a, Mat b, Mat c) {
		Size size = a.size();
		for (int i = 0; i < size.height; i++) {
			for (int j = 0; j < size.width; j++) {
				double[] dataA = a.get(i, j);
				double[] dataB = b.get(i, j);
				double[] dataC = c.get(i, j);
				dataA[0] = (dataA[0] +  dataB[0] + dataC[0]) / 3;
				dataA[1] = (dataA[1] +  dataB[1] + dataC[1]) / 3;
				dataA[2] = (dataA[2] +  dataB[2] + dataC[2]) / 3;
			}
		}

		return a;
	}

	@Override
	public void finaliza() {
		continua.set(false);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt();
		capitura.release();
	}
}
