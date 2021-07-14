package mpjp.game;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Images {
	static Set<String> extensions = new HashSet<>(Arrays.asList("jpg","png"));
	static File imageDirectory = new File("test/mpjp/resources");

	public static void addExtension(String extension) {
		extensions.add(extension);
	}

	static Set<String> getAvailableImages() {		
		return new HashSet<String>(Arrays.asList(imageDirectory.list()));
	}

	public static Set<String> getExtensions() {
		return extensions;
	}

	public static File getImageDirectory() {
		return imageDirectory;
	}

	public static void setImageDirectory(File imageDirectory) {
		Images.imageDirectory = imageDirectory;
	}
}
