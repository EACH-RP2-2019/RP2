
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import imageProcessing.OpenCV_Smoothing;
import imageProcessing.OpenCV_Threshold;
import tela.ShapeEllipse;
import util.SystemSetProperty;
import util.Transforma;

public class WebcamMovimentos {

	private JFrame frame;
	private JPanel panel;
	
	private Thread thread;
	private AtomicBoolean continua;
	private VideoCapture capitura;
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
					WebcamMovimentos window = new WebcamMovimentos();
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
	public WebcamMovimentos() {
		continua = new AtomicBoolean();
		continua.set(true);
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
				continua.set(false);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				thread.interrupt();
				capitura.release();
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
				capitura = new VideoCapture(0);

				if(capitura.isOpened()) {
					Mat ultima = new Mat();
					Mat penultima = ultima;
					Mat atepenultima = ultima;
					
					while(continua.get()) {
						capitura.read(video);
						if(!video.empty()) {
							penultima = ultima;
							Imgproc.cvtColor(video, ultima, Imgproc.COLOR_BGR2GRAY);
							ultima = OpenCV_Threshold.transforma(ultima, 100, OpenCV_Threshold.Type.BINARY);
								
							Mat gerada = pegaMovimentos(ultima, penultima, atepenultima);
							
							imagem = Transforma.transformaEmBufferedImage(gerada);
							Graphics2D g2D = (Graphics2D)imagem.getGraphics();
							
							g2D.setColor(Color.blue);
							for (Shape pupila : detecta(gerada)) {
								g2D.draw(pupila);
							}
							
							g2D.setColor(Color.pink);
							for (Shape pupila : detecta2(gerada)) {
								g2D.draw(pupila);
							}
							
							panel.repaint();
						}
					}
				}
			}
		});
		thread.start();
	}
	
	public static Mat pegaMovimentos(Mat ultima, Mat penultima, Mat atepenultima) {
		Mat g1 = new Mat();
		Core.absdiff(ultima, penultima, g1);
		
		Mat g2 = new Mat();
		Core.absdiff(penultima, atepenultima, g2);
		
		Mat gerada = new Mat();
		Core.absdiff(g1, g2, gerada);
		
		return gerada;
	}
	
	public static List<ShapeEllipse> detecta(Mat matImagemMonocromatica) {
		Mat cannyOutput = new Mat();

		//Imgproc.blur(matImagemMonocromatica, matImagemMonocromatica, new Size(3, 3));
		matImagemMonocromatica = OpenCV_Smoothing.transforma(matImagemMonocromatica, "Median", 1);
		
		//int limite = 212;
		int limite = 80;
		Imgproc.Canny(matImagemMonocromatica, cannyOutput, limite, limite * 2);
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

		List<ShapeEllipse> elipses = new ArrayList<>();
		for (MatOfPoint contour : contours) {
			if (contour.rows() > 5) {
				RotatedRect minEllipse = Imgproc.fitEllipse(new MatOfPoint2f(contour.toArray()));

				double height = minEllipse.size.height;
				double width = minEllipse.size.width;
				if(height <= width * 1.5 && width <= height * 1.5) {
					ShapeEllipse elipse = new ShapeEllipse(minEllipse.center.x, minEllipse.center.y, minEllipse.size.width, minEllipse.size.height);
					elipses.add(elipse);
				}
			}
		}
		
		return elipses;
	}
	
	public static List<ShapeEllipse> detecta2(Mat matImagemCinza){
	    //Imgproc.medianBlur(matImagemCinza, matImagemCinza, 3);
	    
		Mat circles = new Mat();
	    Imgproc.HoughCircles(matImagemCinza, circles, Imgproc.HOUGH_GRADIENT, 1.0,
	    		(double)matImagemCinza.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 20.0, 7, 18); // change the last two parameters
                // (min_radius & max_radius) to detect larger circles
	    
	    List<ShapeEllipse> publilas = new ArrayList<>();
	    for (int x = 0; x < circles.cols(); x++) {
	        double[] c = circles.get(0, x);
	        Point center = new Point(Math.round(c[0]), Math.round(c[1]));
	        int radius = (int) Math.round(c[2]);
	        
	        ShapeEllipse publila = new ShapeEllipse(center.x, center.y, radius);
			publilas.add(publila);
	    }
	    
	    return publilas;
	}
}
