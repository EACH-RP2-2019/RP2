package tela.strategy.tipoDeTela;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import tela.Tela;

public class Fotos implements StrategyTipoDeTela{

	@Override
	public void inicia(Tela tela) {
		String diretorioPrincipal = "rostos";
		JComboBox<String> cbFotos = new JComboBox<>();
		cbFotos.setSize(150, 30);
		cbFotos.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					try {
						Mat matImagemColorida = Imgcodecs.imread(diretorioPrincipal+File.separator+(String)cbFotos.getSelectedItem(), Imgcodecs.IMREAD_COLOR);
						BufferedImage bufferedImageColorida = ImageIO.read(new File(diretorioPrincipal+File.separator+(String)cbFotos.getSelectedItem()));
						tela.imprimeImagem(matImagemColorida, bufferedImageColorida);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		File diretorioDeImagens = new File(diretorioPrincipal);
		for (String arquivoDeImagem : diretorioDeImagens.list()) {
			cbFotos.addItem(arquivoDeImagem);
		}
		tela.frame.getContentPane().add(BorderLayout.NORTH, cbFotos);
	}

	@Override
	public void finaliza() {
		
	}
}
