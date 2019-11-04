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

public class Triangulo implements StrategyDivisorDeTela {
	private Tela tela;

	private boolean esquerdo;
	private boolean direito;
	
	private boolean superior;
	private boolean inferior;
	
	/**
	 * Create the panel.
	 */
	public Triangulo(Tela tela) {
		this.tela = tela;
	}

	@Override
	public void paintComponent(Graphics g) {
		if(tela.imageIcon == null) return;
		
		Graphics2D g2D = (Graphics2D)g;
		
		ImageIcon imageIcon = tela.imageIcon;
		Image imagem = imageIcon.getImage();
		g2D.drawImage(imagem, 0, 0, tela.panel);
		
		int cetroTelaX = imageIcon.getIconWidth()/2;
		int cetroTelaY = imageIcon.getIconHeight()/2;
		
		g.setColor(Color.white);
		Polygon imagemEsquerdo = new Polygon(new int [] {0, cetroTelaX, 0}, new int [] {0, cetroTelaY, imageIcon.getIconHeight()}, 3);
		g2D.draw(imagemEsquerdo);
		Polygon imagemDireito = new Polygon(new int [] {imageIcon.getIconWidth(), cetroTelaX, imageIcon.getIconWidth()}, new int [] {0, cetroTelaY, imageIcon.getIconHeight()}, 3);
		g2D.draw(imagemDireito);
		
		Polygon imagemSuperior = new Polygon(new int [] {0, cetroTelaX, imageIcon.getIconWidth()}, new int [] {0, cetroTelaY, 0}, 3);
		g2D.draw(imagemSuperior);
		Polygon imagemInferior = new Polygon(new int [] {0, cetroTelaX, imageIcon.getIconWidth()}, new int [] {imageIcon.getIconHeight(), cetroTelaY, imageIcon.getIconHeight()}, 3);
		g2D.draw(imagemInferior);
		
		g.setColor(Color.red);
		for (Rectangle face : tela.faces) {
			g2D.draw(face);
		}
		
		float verdeX = tela.verdeX.getValue() / 100.0F;
		float verdeY = tela.verdeY.getValue() / 100.0F;
		float cinza = tela.cinza.getValue() / 100.0F;
		
		int contadorEsquerdo = 0;
		int contadorDireito = 0;
		
		int contadorSuperior = 0;
		int contadorInferior = 0;
		
		for (Rectangle olho : tela.olhos) {
			
			int cetroOlhoX = olho.x + (int)(olho.width*verdeX);
			int cetroOlhoY = olho.y + (int)(olho.height*verdeY);
			
			g.setColor(Color.green);
			Polygon olhoEsquerdo = new Polygon(new int [] {olho.x, cetroOlhoX, olho.x}, new int [] {olho.y, cetroOlhoY, olho.y+olho.height}, 3);
			g2D.draw(olhoEsquerdo);
			Polygon olhoDireito = new Polygon(new int [] {olho.x+olho.width, cetroOlhoX, olho.x +olho.width}, new int [] {olho.y, cetroOlhoY, olho.y+olho.height}, 3);
			g2D.draw(olhoDireito);
			
			Polygon olhoSuperior = new Polygon(new int [] {olho.x, cetroOlhoX, olho.x+olho.width}, new int [] {olho.y, cetroOlhoY, olho.y}, 3);
			g2D.draw(olhoSuperior);
			Polygon olhoInferior = new Polygon(new int [] {olho.x, cetroOlhoX, olho.x+olho.width}, new int [] {olho.y+olho.height, cetroOlhoY, olho.y+olho.height}, 3);
			g2D.draw(olhoInferior);
			
			g.setColor(Color.yellow);
			g2D.draw(olho);
			
			g.setColor(Color.gray);
			Rectangle olhoZonaMorta = new Rectangle(cetroOlhoX - (int)(olho.width*cinza/2), cetroOlhoY - (int)(olho.height*cinza/2), (int)(olho.width*cinza), (int)(olho.height*cinza));
			g2D.draw(olhoZonaMorta);
			
			for (ShapeEllipse elipse : tela.elipses) {
				if(!olhoZonaMorta.contains(elipse.getCentro())) {
					if(olhoEsquerdo.contains(elipse.getCentro())) {
						contadorEsquerdo++;
					}
					else if(olhoDireito.contains(elipse.getCentro())) {
						contadorDireito++;
					}
					
					else if(olhoSuperior.contains(elipse.getCentro())) {
						contadorSuperior++;
					}
					else if(olhoInferior.contains(elipse.getCentro())) {
						contadorInferior++;
					}
				}
			}
			
			for (ShapeEllipse pupila : tela.publilas) {
				if(olhoEsquerdo.contains(pupila.getCentro())) {
					contadorEsquerdo++;
				}
				else if(olhoDireito.contains(pupila.getCentro())) {
					contadorDireito++;
				}
				
				else if(olhoSuperior.contains(pupila.getCentro())) {
					contadorSuperior++;
				}
				else if(olhoInferior.contains(pupila.getCentro())) {
					contadorInferior++;
				}
			}
		}
		
		if(contadorEsquerdo >= 2) {
			esquerdo = true;
			direito = false;
			
			superior = false;
			inferior = false;
		}
		else if(contadorDireito >= 2) {
			esquerdo = false;
			direito = true;
			
			superior = false;
			inferior = false;		
		}
		
		else if(contadorSuperior >= 2) {
			esquerdo = false;
			direito = false;
			
			superior = true;
			inferior = false;
		}
		else if(contadorInferior >= 2) {
			esquerdo = false;
			direito = false;
			
			superior = false;
			inferior = true;
		}
		
		g.setColor(new Color(255, 255, 255, 127));
		if(esquerdo) {
			g2D.fill(imagemEsquerdo);
		}
		else if(direito) {
			g2D.fill(imagemDireito);			
		}
		
		else if(superior) {
			g2D.fill(imagemSuperior);
		}
		else if(inferior) {
			g2D.fill(imagemInferior);
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
