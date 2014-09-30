package org.docsapss.mobapps.systemdashboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;

/**
 * @class StorageUtils
 * @brief Provides utility methods for reading/writing to files
 */
public class StorageUtils {
	
	public static String readFirstLineFromFile(Context context, String fileName) {
		String firstLine=null;
		if (checkFileExists(context,fileName)) {
			try {
				FileInputStream fis=context.openFileInput(fileName);
				InputStreamReader isr= new InputStreamReader(fis);
				BufferedReader br=new BufferedReader(isr);
				firstLine=br.readLine().toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "FileReadError";
			}
			return firstLine;
		}
		return "FileReadError";
	}
	
	public static boolean writeStringToFile(Context context, String fileName, String stringToWrite) {
		OutputStreamWriter osw;
		try {
			osw = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
			osw.write(stringToWrite);
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static boolean checkFileExists(Context context, String fileName) {
		File file=context.getFileStreamPath(fileName);
		return file.exists();
	}
}