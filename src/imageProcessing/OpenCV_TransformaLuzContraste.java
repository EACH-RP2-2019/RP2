package imageProcessing;

import org.opencv.core.Mat;

public class OpenCV_TransformaLuzContraste {
	
	//alpha [1.0-3.0]
	//beta [0-100]
	
	// Valor teste: 3.0, 0
    public static Mat transforma(Mat image, double alpha, int beta) {
        
        Mat newImage = Mat.zeros(image.size(), image.type());
        image.convertTo(newImage, -1, alpha, beta);
        
        //Print da imagem "original"
        //CriaMensagem.messagemImagem(Transforma.transformaEmBufferedImage(image));
        
        return newImage;
    }
}