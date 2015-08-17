package controller;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class InputFilter extends FileFilter {
	
	public String[] extensionList;
	public final static String txt = "txt";
	public final static String xls = "xls";
	public final static String xlsx = "xlsx";
	public final static String ecdp = "ecdp";
	
	public InputFilter(String s) {
		extensionList = s.split("\\s");
	}
	
	public static String getExtension(File e) {
		String ext = null;
		String s = e.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i + 1).toLowerCase();
		return ext;
	}
	
	@Override
	public boolean accept(File inFile) {
		try {
			if (inFile == null)
				return false;
			if (inFile.isDirectory())
				return true;
			String extension = getExtension(inFile);
			for (String s : extensionList) {
				if (extension.equals(s))
					return true;
			}
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	@Override
	public String getDescription() {
		String s = "";
		for (String x : extensionList) {
			s += "*." + x + ", ";
		}
		s = s.substring(0, s.length() - 2);
		if (extensionList.length == 1) {
			s = "Enhanced Communication DPSO File (" + s + ")";
		}
		return s;
	}
	
}
