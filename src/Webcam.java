
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import util.SystemSetProperty;
import util.Transforma;

public class Webcam {

	private JFrame frame;
	private JPanel panel;
	
	private Thread thread;
	private BufferedImage imagem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
						if("Nimbus".equals(info.getName())) {
							javax.swing.UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					Webcam window = new Webcam();
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
	public Webcam() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		SystemSetProperty.setProperty("C:/opencv/build/java/x64");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		frame = new JFrame();
		frame.setSize(657, 518);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				thread.interrupt();
			}
		});
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				if(imagem != null) g.drawImage(imagem, 0, 0, imagem.getWidth(), imagem.getHeight(), null);
			}
		};
		panel.setBounds(0, 0, 657, 518);
		frame.getContentPane().add(panel);
		
		thread = new Thread (new Runnable() {
			@Override
			public void run() {
				Mat video = new Mat();
				VideoCapture capitura = new VideoCapture(0);
				                                        // n do dispositivo
				if(capitura.isOpened()) {
					Mat ultima = new Mat();
					Mat penultima = ultima;
					Mat antepenultima = ultima;
					
					boolean iniciou = false;
					while(true) {
						capitura.read(video);
						if(!video.empty()) {
							if(!iniciou) {
								Imgproc.cvtColor(video, ultima, Imgproc.COLOR_BGR2GRAY);
								penultima = ultima;
								antepenultima = ultima;
								iniciou = true;
							} else {	
								antepenultima = penultima;
								penultima = ultima;
								Imgproc.cvtColor(video, ultima, Imgproc.COLOR_BGR2GRAY);
								
								imagem = Transforma.transformaEmBufferedImage(calculaDiferenca(antepenultima,penultima,ultima));
								panel.repaint();
							}
						}
					}
				}
			}
		});
		thread.start();
	}
	
	private Mat calculaDiferenca(Mat img1, Mat img2, Mat img3) {
		Mat d1 = new Mat();
		Core.absdiff(img3, img2, d1);
		
		Mat d2 = new Mat();
		Core.absdiff(img2, img1, d2);
		
		Mat retorno = new Mat();
		Core.absdiff(d1, d2, retorno);

		return retorno;
	}
}
