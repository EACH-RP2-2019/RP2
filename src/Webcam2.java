
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import util.SystemSetProperty;
import util.Transforma;

public class Webcam2 {

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
					Webcam2 window = new Webcam2();
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
	public Webcam2() {
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
			private CascadeClassifier cascadeFaces = new CascadeClassifier("cascades/haarcascade_frontalface_default.xml");
			private CascadeClassifier cascadeOlhos = new CascadeClassifier("cascades/haarcascade_eye.xml");
			
			@Override
			public void run() {
				Mat video = new Mat();
				VideoCapture capitura = new VideoCapture(0);
				                                        // n do dispositivo
				if(capitura.isOpened()) {
					while(true) {
						capitura.read(video);
						if(!video.empty()) {
							
							Mat matImagemColorida = video;
							
							Mat matImagemCinza = new Mat();
							Imgproc.cvtColor(matImagemColorida, matImagemCinza, Imgproc.COLOR_BGR2GRAY);
							
							MatOfRect facesDetectadas = new MatOfRect();
							cascadeFaces.detectMultiScale(matImagemCinza, facesDetectadas,
									1.1, // Scale Factor
									1,   // MinNeighbors
									0,   // flags
									new Size(200, 200), // minSize
									new Size(500, 500)  // maxSize
									);
							
							for(Rect rect : facesDetectadas.toArray()) {
								Imgproc.rectangle(matImagemColorida, rect, new Scalar(0, 0 , 255), 2);
							}
							
							MatOfRect olhosDetectadas = new MatOfRect();
							cascadeOlhos.detectMultiScale(matImagemCinza, olhosDetectadas);
							
							for(Rect rect : olhosDetectadas.toArray()) {
								Imgproc.rectangle(matImagemColorida, rect, new Scalar(0, 255, 255), 2);
							}
							
							imagem = Transforma.transformaEmBufferedImage(video);
							//frame.setSize(imagem.getWidth() + 17, imagem.getHeight() + 38);
							//panel.setSize(imagem.getWidth(), imagem.getHeight());
							panel.repaint();
						}
					}
				}
			}
		});
		thread.start();
	}
}
