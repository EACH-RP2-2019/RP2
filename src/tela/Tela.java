package tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import cascade.OpenCV_CascadeFacesSuperior;
import cascade.OpenCV_CascadeOlhosCentralizados;
import feature.OpenCV_ElipsesTamanho;
import feature.OpenCV_Pupila;
import imageProcessing.BoofCV_Thresholding;
import imageProcessing.OpenCV_Morphology;
import imageProcessing.OpenCV_TransformaLuzContraste;
import tela.strategy.divisorDeTela.Quadrante;
import tela.strategy.divisorDeTela.StrategyDivisorDeTela;
import tela.strategy.divisorDeTela.Triangulo;
import tela.strategy.tipoDeTela.Fotos;
import tela.strategy.tipoDeTela.StrategyTipoDeTela;
import tela.strategy.tipoDeTela.WebCam;
import tela.strategy.tipoDeTela.WebCamEstavel;
import util.SystemSetProperty;
import util.Transforma;

public class Tela {
	
	public StrategyTipoDeTela tipoDeTela;
	public StrategyDivisorDeTela divisorDeTela;
	
	public JFrame frame;
	public JSlider cinza;
	public JSlider verdeX;
	public JSlider verdeY;
	public JSlider amareloX;
	public JSlider amareloWidth;
	public JSlider amareloY;
	public JSlider amareloHeight;
	public JPanel panel;
	
	public ImageIcon imageIcon;
	public List<Rectangle> faces;
	public List<Rectangle> olhos;
	public List<ShapeEllipse> elipses;
	public List<ShapeEllipse> publilas;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SystemSetProperty.setProperty("C:/opencv/build/java/x64");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							javax.swing.UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					Tela window = new Tela();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tela() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		SystemSetProperty.setProperty("C:/opencv/build/java/x64");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		frame = new JFrame();
		frame.setBounds(50, 50, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if(tipoDeTela != null) tipoDeTela.finaliza();
			}
		});

		cinza = new JSlider();
		cinza.setOrientation(SwingConstants.VERTICAL);
		cinza.setForeground(Color.gray);
		cinza.setInverted(true);
		cinza.setFont(new Font("Tahoma", Font.PLAIN, 8));
		cinza.setPaintLabels(true);
		cinza.setPaintTicks(true);
		cinza.setValue(20);
		cinza.setMajorTickSpacing(10);
		cinza.setMinimum(0);
		cinza.setMaximum(100);
		cinza.setMinorTickSpacing(1);
		frame.getContentPane().add(cinza, BorderLayout.WEST);
		
		verdeX = new JSlider();
		verdeX.setFont(new Font("Tahoma", Font.PLAIN, 8));
		verdeX.setForeground(Color.green);
		verdeX.setPaintLabels(true);
		verdeX.setPaintTicks(true);
		verdeX.setValue(50);
		verdeX.setMajorTickSpacing(10);
		verdeX.setMinimum(0);
		verdeX.setMaximum(100);
		verdeX.setMinorTickSpacing(1);
		
		amareloX = new JSlider();
		amareloX.setFont(new Font("Tahoma", Font.PLAIN, 8));
		amareloX.setForeground(Color.yellow);
		amareloX.setPaintLabels(true);
		amareloX.setPaintTicks(true);
		amareloX.setValue(15);
		amareloX.setMajorTickSpacing(10);
		amareloX.setMinimum(0);
		amareloX.setMaximum(100);
		amareloX.setMinorTickSpacing(1);
		
		amareloWidth = new JSlider();
		amareloWidth.setFont(new Font("Tahoma", Font.PLAIN, 8));
		amareloWidth.setForeground(Color.yellow);
		amareloWidth.setPaintLabels(true);
		amareloWidth.setPaintTicks(true);
		amareloWidth.setValue(85);
		amareloWidth.setMajorTickSpacing(10);
		amareloWidth.setMinimum(0);
		amareloWidth.setMaximum(100);
		amareloWidth.setMinorTickSpacing(1);
		
		JComboBox<StrategyDivisorDeTela> cbDivisor = new JComboBox<>();
		cbDivisor.setBackground(Color.green);
		cbDivisor.addItem(new Quadrante(this));
		cbDivisor.addItem(new Triangulo(this));
		cbDivisor.setSelectedIndex(0);
		divisorDeTela = (StrategyDivisorDeTela) cbDivisor.getSelectedItem();
		cbDivisor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					divisorDeTela = (StrategyDivisorDeTela) cbDivisor.getSelectedItem();
				}
			}
		});
		
		Box boxSOUTH_linha1 = Box.createHorizontalBox();
		Box boxSOUTH_linha2 = Box.createHorizontalBox();
		Box boxSOUTH_linha3 = Box.createHorizontalBox();
		boxSOUTH_linha1.add(verdeX);
		boxSOUTH_linha2.add(amareloX);
		boxSOUTH_linha2.add(amareloWidth);
		boxSOUTH_linha3.add(cbDivisor);
		Box boxSOUTH = Box.createVerticalBox();
		boxSOUTH.add(boxSOUTH_linha1);
		boxSOUTH.add(boxSOUTH_linha2);
		boxSOUTH.add(boxSOUTH_linha3);
		frame.getContentPane().add(boxSOUTH, BorderLayout.SOUTH);
		
		verdeY = new JSlider();
		verdeY.setOrientation(SwingConstants.VERTICAL);
		verdeY.setForeground(Color.green);
		verdeY.setInverted(true);
		verdeY.setFont(new Font("Tahoma", Font.PLAIN, 8));
		verdeY.setPaintLabels(true);
		verdeY.setPaintTicks(true);
		verdeY.setValue(50);
		verdeY.setMajorTickSpacing(10);
		verdeY.setMinimum(0);
		verdeY.setMaximum(100);
		verdeY.setMinorTickSpacing(1);
		
		amareloY = new JSlider();
		amareloY.setOrientation(SwingConstants.VERTICAL);
		amareloY.setForeground(Color.yellow);
		amareloY.setInverted(true);
		amareloY.setFont(new Font("Tahoma", Font.PLAIN, 8));
		amareloY.setPaintLabels(true);
		amareloY.setPaintTicks(true);
		amareloY.setValue(30);
		amareloY.setMajorTickSpacing(10);
		amareloY.setMinimum(0);
		amareloY.setMaximum(100);
		amareloY.setMinorTickSpacing(1);
		
		amareloHeight = new JSlider();
		amareloHeight.setOrientation(SwingConstants.VERTICAL);
		amareloHeight.setForeground(Color.yellow);
		amareloHeight.setInverted(true);
		amareloHeight.setFont(new Font("Tahoma", Font.PLAIN, 8));
		amareloHeight.setPaintLabels(true);
		amareloHeight.setPaintTicks(true);
		amareloHeight.setValue(40);
		amareloHeight.setMajorTickSpacing(10);
		amareloHeight.setMinimum(0);
		amareloHeight.setMaximum(100);
		amareloHeight.setMinorTickSpacing(1);
		
		Box boxEAST_coluna1 = Box.createVerticalBox();
		Box boxEAST_coluna2 = Box.createVerticalBox();
		boxEAST_coluna1.add(verdeY);
		boxEAST_coluna2.add(amareloY);
		boxEAST_coluna2.add(amareloHeight);
		Box boxEAST = Box.createHorizontalBox();
		boxEAST.add(boxEAST_coluna1);
		boxEAST.add(boxEAST_coluna2);
		frame.getContentPane().add(boxEAST, BorderLayout.EAST);
		
		String tipos [] = {"WebCam", "WebCamEstavel", "Fotos"};
		
		Integer resposta = JOptionPane.showOptionDialog(
						null, "Escolha um tipo de tela:", null, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, tipos, "");
		
		if(resposta == JOptionPane.CLOSED_OPTION) {
			JOptionPane.showMessageDialog(null, "Não foi possivel iniciar a tela corretamente!!!", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			switch (tipos [resposta]) {
				case "WebCam": {
					tipoDeTela = new WebCam();
					break;
				}
				case "WebCamEstavel": {
					tipoDeTela = new WebCamEstavel();
					break;
				}
				case "Fotos": {
					tipoDeTela = new Fotos();
					break;
				}
			}
		}
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				divisorDeTela.paintComponent(g);
			}
		};
		frame.add(panel, BorderLayout.CENTER);
		
		tipoDeTela.inicia(this);
	}

	public void imprimeImagem(Mat matImagemColorida, BufferedImage bufferedImageColorida) throws IOException {
		Mat matImagemCinza = new Mat();
		Imgproc.cvtColor(matImagemColorida, matImagemCinza, Imgproc.COLOR_BGR2GRAY);
		
		faces = OpenCV_CascadeFacesSuperior.detecta(matImagemColorida, matImagemCinza);
		olhos = OpenCV_CascadeOlhosCentralizados.detecta(matImagemColorida, matImagemCinza, faces, amareloX.getValue(), amareloY.getValue(), amareloWidth.getValue(), amareloHeight.getValue());
		
		matImagemColorida = OpenCV_TransformaLuzContraste.transforma(matImagemColorida, 3, 50);
		matImagemCinza = new Mat();
		Imgproc.cvtColor(matImagemColorida, matImagemCinza, Imgproc.COLOR_BGR2GRAY);
		
		BufferedImage bufferedImagemMonocromatica = BoofCV_Thresholding.transforma(bufferedImageColorida, "NICK2");
		Mat matImagemMonocromatica = OpenCV_Morphology.transforma(Transforma.transformaEmMat(bufferedImagemMonocromatica), OpenCV_Morphology.Type.ELLIPSE, 2, OpenCV_Morphology.Morph_op.DILATATION);
		elipses = OpenCV_ElipsesTamanho.detecta(matImagemColorida, matImagemMonocromatica, olhos);
		publilas = OpenCV_Pupila.detecta(matImagemColorida, matImagemCinza, olhos);
		
		imageIcon = new ImageIcon(bufferedImageColorida);
		frame.setSize(imageIcon.getIconWidth() + 130, imageIcon.getIconHeight() + 150);
		panel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
		panel.repaint();
	}
}