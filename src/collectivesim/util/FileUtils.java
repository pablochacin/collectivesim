package collectivesim.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	private static String DEFAULT_DATE_FORMAT = "yyyyMMdd-HHmmss";
	
	/**
	 * Creates a working directory at the given root or
	 * at the default temporary directory if root is null
	 * 
	 * The name of the directory is the current date formatted according to
	 * the formatting string
	 * @param root
	 * @return
	 */
	public static File createWorkingDirectory(String rootPath, String format){
	
		String dirPath;
		if(rootPath != null){
		  dirPath = rootPath;	
		}
		else{
			dirPath = System.getProperty("java.io.tmpdir");
		}
		
		//be sure the path end with "/" or "\" depending on the platform
		if(!dirPath.endsWith(File.separator)){
			dirPath = dirPath + File.separator;
		}
		
		Date date = new Date(System.currentTimeMillis());
		String filename = new SimpleDateFormat(format).format(date);
		
		File root = new File(dirPath+filename);
		if(root.mkdirs()){
			return root;
		}
		else{
			throw new IllegalArgumentException("Can't create woking directory at "+dirPath);
		}
		
	}
	
	public static File createWorkingDirectory(String rootPath){
		return createWorkingDirectory(rootPath,DEFAULT_DATE_FORMAT);
	}
	
	public static void main(String[] args){
		FileUtils.createWorkingDirectory(null, "yyyyMMdd-HHmmss");
	}
}
