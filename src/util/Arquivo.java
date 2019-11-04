package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

public class Arquivo {
	public static void escrevendoPaleta (Set<Integer> paleta, String arquivo) throws FileNotFoundException, IOException{	
		FileOutputStream file = new FileOutputStream(arquivo);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream (file);
		objectOutputStream.writeObject(paleta);
		objectOutputStream.close();
	}

	public static Set<Integer> lendoPaleta (String arquivo) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream objectInputStream = new ObjectInputStream (new FileInputStream(arquivo));
		
		@SuppressWarnings("unchecked")
		Set<Integer> registro = (Set<Integer>) objectInputStream.readObject();
		objectInputStream.close();
		return registro;
	}
}
