package model;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Properties {
	
	public static void addCityName(String cityName) {
		cityNames.add(cityName);
	}
	
	private static int precision = 50;
	
	/**
	 * @return {@code int} the precision
	 */
	public static int getPrecision() {
		return precision;
	}
	
	/**
	 * @param <b>precision</b> the {@code int} precision to set
	 */
	public static void setPrecision(int precision) {
		if (precision == 0)
			Properties.precision = 1;
		else
			Properties.precision = precision * 5;
	}
	
	/**
	 * @return {@code int} the cityNumber
	 */
	public static int getCityNumber() {
		return cityNumber;
	}
	
	/**
	 * @param <b>cityNumber</b> the {@code int} cityNumber to set
	 */
	public static void setCityNumber(int cityNumber) {
		Properties.cityNumber = cityNumber;
	}
	
	/**
	 * @return {@code String} the initName
	 */
	public static String getInitName() {
		return initName;
	}
	
	/**
	 * @param <b>initName</b> the {@code String} initName to set
	 */
	public static void setInitName(String initName) {
		Properties.initName = initName;
	}
	
	/**
	 * @return {@code boolean} the editable
	 */
	public static boolean isEditable() {
		return editable;
	}
	
	/**
	 * @param <b>editable</b> the {@code boolean} editable to set
	 */
	public static void setEditable(boolean editable) {
		Properties.editable = editable;
	}
	
	/**
	 * @return {@code ArrayList<String>} the cityNames
	 */
	public static Set<String> getCityNames() {
		return cityNames;
	}
	
	/**
	 * @param <b>cityNames</b> the {@code ArrayList<String>} cityNames to set
	 */
	public static void setCityNames(Set<String> cityNames) {
		Properties.cityNames = cityNames;
	}
	
	/**
	 * @return {@code File} the inputFile
	 */
	public static File getInputFile() {
		return inputFile;
	}
	
	/**
	 * @param <b>inputFile</b> the {@code File} inputFile to set
	 */
	public static void setInputFile(File inputFile) {
		Properties.inputFile = inputFile;
	}
	
	private static int cityNumber;
	private static String initName;
	private static boolean editable;
	private static int edgeNumber;
	
	public static int getEdgeNumber() {
		return edgeNumber;
	}
	
	public static void setEdgeNumber(int edgeNumber) {
		Properties.edgeNumber = edgeNumber;
	}
	
	private static Set<String> cityNames = new HashSet<String>();
	private static File inputFile = null;
	private static Map<String, Point> coordinates = new HashMap<String, Point>();
	private static Map<String, Component> uis = new HashMap<String, Component>();
	
	/**
	 * @return {@code Map<String,Component>} the uis
	 */
	public static Map<String, Component> getUis() {
		return uis;
	}
	
	public static Component getUI(String key) {
		if (uis != null) {
			return uis.get(key);
		} else
			return null;
	}
	
	public static void addUI(String key, Component value) {
		uis.put(key, value);
	}
	
	/**
	 * @param <b>uis</b> the {@code Map<String,Component>} uis to set
	 */
	public static void setUis(Map<String, Component> uis) {
		Properties.uis = uis;
	}
	
	/**
	 * @return {@code Map<String,Point>} the coordinates
	 */
	public static Map<String, Point> getCoordinates() {
		return coordinates;
	}
	
	/**
	 * @param <b>coordinates</b> the {@code Map<String,Point>} coordinates to
	 *        set
	 */
	public static void setCoordinates(Map<String, Point> coordinates) {
		Properties.coordinates = coordinates;
	}
}
