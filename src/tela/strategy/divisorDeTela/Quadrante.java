package tela.strategy.divisorDeTela;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.ImageIcon;

import tela.ShapeEllipse;
import tela.Tela;

public class Quadrante implements StrategyDivisorDeTela {
	private Tela tela;

	private boolean superiorEsquerdo;
	private boolean superiorDireito;
	
	private boolean inferiorEsquerdo;
	private boolean inferiorDireito;
	
	/**
	 * Create the panel.
	 */
	public Quadrante(Tela tela) {
		this.tela = tela;
	}

	@Override
	public void paintComponent(Graphics g) {
		if(tela.imageIcon == null) return;
		
		Graphics2D g2D = (Graphics2D)g;
		
		ImageIcon imageIcon = tela.imageIcon;
		Image imagem = imageIcon.getImage();
		g2D.drawImage(imagem, 0, 0, tela.panel);
		
		g.setColor(Color.white);
		Rectangle imagemSuperiorEsquerdo = new Rectangle(0, 0, imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2);
		g2D.draw(imagemSuperiorEsquerdo);
		Rectangle imagemSuperiorDireito = new Rectangle(imageIcon.getIconWidth()/2, 0, imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2);
		g2D.draw(imagemSuperiorDireito);
		
		Rectangle imagemInferiorEsquerdo = new Rectangle(0, imageIcon.getIconHeight()/2, imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2);
		g2D.draw(imagemInferiorEsquerdo);
		Rectangle imagemInferiorDireito = new Rectangle(imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2, imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2);
		g2D.draw(imagemInferiorDireito);
		
		g.setColor(Color.red);
		for (Rectangle face : tela.faces) {
			g2D.draw(face);
		}
		
		float verdeX = tela.verdeX.getValue() / 100.0F;
		float verdeY = tela.verdeY.getValue() / 100.0F;
		float cinza = tela.cinza.getValue() / 100.0F;
		
		int contadorSuperiorEsquerdo = 0;
		int contadorSuperiorDireito = 0;
		
		int contadorInferiorEsquerdo = 0;
		int contadorInferiorDireito = 0;
		
		for (Rectangle olho : tela.olhos) {
			
			int cetroOlhoX = olho.x + (int)(olho.width*verdeX);
			int cetroOlhoY = olho.y + (int)(olho.height*verdeY);
			
			g.setColor(Color.green);
			Polygon olhoSuperiorEsquerdo = new Polygon(new int [] {olho.x, cetroOlhoX, cetroOlhoX, olho.x}, new int [] {olho.y, olho.y, cetroOlhoY, cetroOlhoY}, 4);
			g2D.draw(olhoSuperiorEsquerdo);
			Polygon olhoSuperiorDireito = new Polygon(new int [] {cetroOlhoX, olho.x+olho.width, olho.x+olho.width, cetroOlhoX}, new int [] {olho.y, olho.y, cetroOlhoY, cetroOlhoY}, 4);
			g2D.draw(olhoSuperiorDireito);
			
			Polygon olhoInferiorEsquerdo = new Polygon(new int [] {olho.x, cetroOlhoX, cetroOlhoX, olho.x}, new int [] {cetroOlhoY, cetroOlhoY, olho.y+olho.height, olho.y+olho.height}, 4);
			g2D.draw(olhoInferiorEsquerdo);
			Polygon olhoInferiorDireito = new Polygon(new int [] {cetroOlhoX, olho.x+olho.width, olho.x+olho.width, cetroOlhoX}, new int [] {cetroOlhoY, cetroOlhoY, olho.y+olho.height, olho.y+olho.height}, 4);
			g2D.draw(olhoInferiorDireito);
			
			g.setColor(Color.yellow);
			g2D.draw(olho);
			
			g.setColor(Color.gray);
			Rectangle olhoZonaMorta = new Rectangle(cetroOlhoX - (int)(olho.width*cinza/2), cetroOlhoY - (int)(olho.height*cinza/2), (int)(olho.width*cinza), (int)(olho.height*cinza));
			g2D.draw(olhoZonaMorta);
			
			for (ShapeEllipse elipse : tela.elipses) {
				if(!olhoZonaMorta.contains(elipse.getCentro())) {
					if(olhoSuperiorEsquerdo.contains(elipse.getCentro())) {
						contadorSuperiorEsquerdo++;
					}
					else if(olhoSuperiorDireito.contains(elipse.getCentro())) {
						contadorSuperiorDireito++;
					}
					
					else if(olhoInferiorEsquerdo.contains(elipse.getCentro())) {
						contadorInferiorEsquerdo++;
					}
					else if(olhoInferiorDireito.contains(elipse.getCentro())) {
						contadorInferiorDireito++;
					}
				}
			}
			
			for (ShapeEllipse pupila : tela.publilas) {
				if(olhoSuperiorEsquerdo.contains(pupila.getCentro())) {
					contadorSuperiorEsquerdo++;
				}
				else if(olhoSuperiorDireito.contains(pupila.getCentro())) {
					contadorSuperiorDireito++;
				}
				
				else if(olhoInferiorEsquerdo.contains(pupila.getCentro())) {
					contadorInferiorEsquerdo++;
				}
				else if(olhoInferiorDireito.contains(pupila.getCentro())) {
					contadorInferiorDireito++;
				}
			}
		}
		
		if(contadorSuperiorEsquerdo >= 2) {
			superiorEsquerdo = true;
			superiorDireito = false;
			
			inferiorEsquerdo = false;
			inferiorDireito = false;
		}
		else if(contadorSuperiorDireito >= 2) {
			superiorEsquerdo = false;
			superiorDireito = true;
			
			inferiorEsquerdo = false;
			inferiorDireito = false;		
		}
		
		else if(contadorInferiorEsquerdo >= 2) {
			superiorEsquerdo = false;
			superiorDireito = false;
			
			inferiorEsquerdo = true;
			inferiorDireito = false;
		}
		else if(contadorInferiorDireito >= 2) {
			superiorEsquerdo = false;
			superiorDireito = false;
			
			inferiorEsquerdo = false;
			inferiorDireito = true;
		}
		
		g.setColor(new Color(255, 255, 255, 127));
		if(superiorEsquerdo) {
			g2D.fill(imagemSuperiorEsquerdo);
		}
		else if(superiorDireito) {
			g2D.fill(imagemSuperiorDireito);			
		}
		
		else if(inferiorEsquerdo) {
			g2D.fill(imagemInferiorEsquerdo);
		}
		else if(inferiorDireito) {
			g2D.fill(imagemInferiorDireito);
		}
		
		g.setColor(Color.blue);
		for (Shape elipse : tela.elipses) {
			g2D.draw(elipse);
		}
		
		g.setColor(Color.pink);
		for (Shape pupila : tela.publilas) {
			g2D.draw(pupila);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
