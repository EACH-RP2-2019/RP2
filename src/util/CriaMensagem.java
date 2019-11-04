package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.opencv.core.Mat;

public class CriaMensagem {
	
	public static void messagemImagem (BufferedImage bufferedImage) {
		aparenciaDoWindows();
		
		ImageIcon icon = new ImageIcon(bufferedImage);

		JLabel lagel = new JLabel();
		lagel.setIcon(icon);
		lagel.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
		JOptionPane.showMessageDialog(null, lagel);
	}
	
	public static void messagemImagem (Mat mat) {
		BufferedImage bufferedImage = Transforma.transformaEmBufferedImage(mat);
		
		aparenciaDoWindows();
		
		ImageIcon icon = new ImageIcon(bufferedImage);

		JLabel lagel = new JLabel();
		lagel.setIcon(icon);
		lagel.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
		JOptionPane.showMessageDialog(null, lagel);
	}
	
	public static void messagemPaleta (Set<Integer> rgbs) {
		aparenciaDoWindows();
		
		JPanel painel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				int i = 0;
				for (Integer rgb : rgbs) {
					g.setColor(new Color(rgb));
					g.fillRect(0+(10*(i%20)), 0+(10*(i/20)), 10, 10);
					i++;
				}
			}
		};
		painel.setPreferredSize(new Dimension(200, 20+(rgbs.size()*10/20)));
		JOptionPane.showMessageDialog(null, painel);
	}
	
	public static int messagemConfirmacaoImagem (BufferedImage imagem, String questao) {
		aparenciaDoWindows();
		
		ImageIcon imageIcon = new ImageIcon(imagem);
		return JOptionPane.showConfirmDialog(null, questao, null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, imageIcon);
	}
	
	private static void aparenciaDoWindows() {
		try {
			for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e){
			
		}
	}
}
