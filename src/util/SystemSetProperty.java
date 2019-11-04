package util;

import java.io.File;
import java.lang.reflect.Field;

public class SystemSetProperty {
	/**
	 * Isso permite que o java.library.path seja modificado em tempo de execucao
	 * De um engenheiro da Sun em http://forums.sun.com/thread.jspa?threadID=707176
	 * @param path
	 */
	public static void setProperty(String path) {
		try {
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (String p : paths) {
				if (path.equals(p)) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = path;
			field.set(null, tmp);
			System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + path);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Falha ao obter permiss�es para definir o caminho da biblioteca");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Falha ao obter o identificador de campo para definir o caminho da biblioteca");
		}
	}
}
