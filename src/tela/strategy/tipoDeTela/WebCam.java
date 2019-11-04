package tela.strategy.tipoDeTela;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import tela.Tela;
import util.InverteImagem;
import util.Transforma;

public class WebCam implements StrategyTipoDeTela{

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
					while(continua.get()) {
						capitura.read(video);
						if(!video.empty()) {
							
							Mat matImagemColorida = video;
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
