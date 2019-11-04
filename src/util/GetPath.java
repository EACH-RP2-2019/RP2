package util;

public class GetPath {
	public static String path(String diretorio) {
		String path = GetPath.class.getClassLoader().getResource(diretorio).getPath();
		System.out.println(path);
		return path;
	}
}
