package collectivesim.util;

import java.awt.Color;
import java.lang.reflect.Field;

public class GraphicUtils {

	public static Color getColor(String colorName) {
		try {
			// Find the field and value of colorName
			Field field = Class.forName("java.awt.Color").getField(colorName);
			return (Color)field.get(null);
		} catch (Exception e) {
			return null;
		}
	}
}
