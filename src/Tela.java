import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import util.CriaMensagem;
import util.SystemSetProperty;
import util.Transforma;

public class Tela {

	private JFrame frame;

	private ImageIcon imageIcon;
	private JPanel panel;

	private JSlider hue;
	private JSlider saturation;
	private JSlider value;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box box = Box.createVerticalBox();
		frame.getContentPane().add(box, BorderLayout.NORTH);
		
		hue = new JSlider();
		hue.setFont(new Font("Tahoma", Font.PLAIN, 8));
		hue.setPaintLabels(true);
		hue.setPaintTicks(true);
		hue.setValue(0);
		hue.setMajorTickSpacing(10);
		hue.setMinimum(0);
		hue.setMaximum(180);
		hue.setMinorTickSpacing(1);
		box.add(hue);
		
		saturation = new JSlider();
		saturation.setFont(new Font("Tahoma", Font.PLAIN, 8));
		saturation.setPaintLabels(true);
		saturation.setPaintTicks(true);
		saturation.setValue(0);
		saturation.setMajorTickSpacing(10);
		saturation.setMinimum(0);
		saturation.setMaximum(255);
		saturation.setMinorTickSpacing(1);
		box.add(saturation);
		
		value = new JSlider();
		value.setFont(new Font("Tahoma", Font.PLAIN, 8));
		value.setPaintLabels(true);
		value.setPaintTicks(true);
		value.setValue(0);
		value.setMajorTickSpacing(10);
		value.setMinimum(0);
		value.setMaximum(255);
		value.setMinorTickSpacing(1);
		box.add(value);
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if(imageIcon != null) {
					Image imagem = imageIcon.getImage();
					g.drawImage(imagem, 0, 0, panel);
				}
			}
		};
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton button = new JButton("Acionar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roda();
				
				frame.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight() + 200);
				panel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
				
				panel.repaint();
			}
		});
		frame.add(button, BorderLayout.SOUTH);
	}

	public void roda() {		
		Mat src = Imgcodecs.imread("rostos/carol1.jpeg", Imgcodecs.IMREAD_COLOR);

		Mat blurredImage = new Mat();
		Mat hsvImage = new Mat();
		Mat mask = new Mat();
		Mat morphOutput = new Mat();

		// remove some noise
		Imgproc.blur(src, blurredImage, new Size(7, 7));

		// convert the frame to HSV
		Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);

		// get thresholding values from the UI
		// remember: H ranges 0-180, S and V range 0-255
		Scalar minValues = new Scalar(hue.getValue(), saturation.getValue(), value.getValue());
		Scalar maxValues = new Scalar(hue.getValue(), saturation.getValue(), value.getValue());

		// show the current selected HSV range
		String valuesToPrint = "Hue range: " + minValues.val[0] + "-" + maxValues.val[0] + "\tSaturation range: "
				+ minValues.val[1] + "-" + maxValues.val[1] + "\tValue range: " + minValues.val[2] + "-"
				+ maxValues.val[2];

		System.out.println(valuesToPrint);
		
		// threshold HSV image to select tennis balls
		Core.inRange(hsvImage, minValues, maxValues, mask);

		// morphological operators
		// dilate with large element, erode with small ones
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

		Imgproc.erode(mask, morphOutput, erodeElement);
		Imgproc.erode(mask, morphOutput, erodeElement);

		Imgproc.dilate(mask, morphOutput, dilateElement);
		Imgproc.dilate(mask, morphOutput, dilateElement);

		// init
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();

		// find contours
		Imgproc.findContours(hierarchy, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
                            // ?
		// if any contour exist...
		if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
			// for each contour, display it in blue
			for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
				Imgproc.drawContours(src, contours, idx, new Scalar(250, 0, 0));
			}
		}
		
		imageIcon = new ImageIcon(Transforma.transformaEmBufferedImage(hsvImage));
	}
}
