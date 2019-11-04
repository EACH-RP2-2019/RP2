package tela;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class ShapeEllipse extends Ellipse2D.Double{
	private static final long serialVersionUID = 1L;

	private Point2D.Double centro;
	
	public ShapeEllipse(double centroX, double centroY, double width, double height) {
        super();
        centro = new Point2D.Double(centroX, centroY);
        super.setFrame(centroX-width/2, centroY-height/2, width, height);
    }
	
	public ShapeEllipse(double centroX, double centroY, double raio) {
        super();
        centro = new Point2D.Double(centroX, centroY);
        super.setFrame(centroX-raio, centroY-raio, raio*2, raio*2);
    }
	
	public Point2D.Double getCentro() {
		return centro;
	}
}
