package collectivesim.visualization;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import net.sf.epsgraphics.ColorMode;
import net.sf.epsgraphics.EpsGraphics;
import ptolemy.util.StringUtilities;


import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Displays a view in a JPanel and updates it.
 * 
 * @author Pablo Chacin
 *
 */
public class ViewPanel extends JInternalFrame implements Runnable {

	
	private View view;

	private JMenuBar menuBar; 


	public ViewPanel(View view){
		super(view.getName(),true, false, true, true );

		this.view = view;

		setSize(new Dimension(view.getHeight(),view.getWidth()));
		//place visual elements in the frame
		getContentPane().add(view.getViewableContent());
	
		menuBar = buildMenuBar();	

		setJMenuBar(menuBar);

		setVisible(true);
	}

	public View getView(){
		return this.view;
	}
	
	
	/**
	 * Takes a snapshot of the view to a file;
	 * 
	 * @param format type of format
	 * 
	 * @param file file to export to
	 * 
	 * @return true if the export was successful, false otherwise
	 */
	public boolean takeSnapshot(Viewer.EXPORT_FORMAT format,File file){
		
		if(file == null){
			return false;
		}

		try {
			FileOutputStream  out = new FileOutputStream(file);
			
			switch(format){
			case EPS:
				exportEPS(out);
				break;
				
			case JPEG:
				exportJpeg(out);
				break;
			}
			
			out.flush();
			out.close();
			return true;
		}
		catch(Exception e){
			String message = "Export failed: " + e.getMessage();
			JOptionPane.showMessageDialog(this, message,
					"Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
			
	}
	
	/**
	 * Builds the Menu bar for the Viewer. 
	 * 
	 * Default implementation creates a file menu.
	 * 
	 * Subclasses can override this method to add new menus to the bar.
	 * @return
	 */
	protected JMenuBar buildMenuBar(){
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(buildFileMenu());

		return menuBar;
	}

	
	


	/**  
	 * Builds the File Menu
	 * @return
	 */
	protected JMenu buildFileMenu(){

		JMenu fileMenu = new JMenu("File");

		// File menu
		JMenuItem[] fileMenuItems = { new JMenuItem("Export EPS", KeyEvent.VK_E),
									  new JMenuItem("Export Image", KeyEvent.VK_E),
									  new JMenuItem("Close", KeyEvent.VK_C) };

		ActionListener fml = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JMenuItem target = (JMenuItem) e.getSource();
				String actionCommand = target.getActionCommand();

				try {
					if (actionCommand.equals("Export EPS" )) {
						takeSnapshot(Viewer.EXPORT_FORMAT.EPS,choseFile("eps", "EPS File"));
					}else if (actionCommand.equals("Export Image" )) {
						takeSnapshot(Viewer.EXPORT_FORMAT.JPEG,choseFile("jpeg", "Jpeg imange File"));
					}else if (actionCommand.equals("Print")) {
						//print();
					} else if (actionCommand.equals("Close")) {
						//close();
					}
				} catch (Exception exception) {
					// If we do not catch exceptions here, then they
					// disappear to stdout, which is bad if we launched
					// where there is no stdout visible.
					JOptionPane.showMessageDialog(null, "File Menu Exception:\n"
							+ exception.toString(), "Ptolemy Plot Error",
							JOptionPane.WARNING_MESSAGE);
				}

				// NOTE: The following should not be needed, but there jdk1.3beta
				// appears to have a bug in swing where repainting doesn't
				// properly occur.
				repaint();
			}


		};

		// Set the action command and listener for each menu item.
		for (int i = 0; i < fileMenuItems.length; i++) {
			fileMenuItems[i].setActionCommand(fileMenuItems[i].getText());
			fileMenuItems[i].addActionListener(fml);
			fileMenu.add(fileMenuItems[i]);
		}

		return fileMenu;
	}



	/** Export to a file in EPS format
	 * @throws IOException 
	 */
	protected synchronized void exportEPS(OutputStream out) throws IOException {


			EpsGraphics g = new EpsGraphics(view.getName(),out,0,0,view.getHeight(),view.getWidth(),ColorMode.GRAYSCALE);
			view.print(g);


	}

	protected synchronized void exportJpeg(OutputStream out) throws ImageFormatException, IOException{


			BufferedImage awtImage = new BufferedImage(view.getHeight(),view.getWidth(),BufferedImage.TYPE_INT_RGB);

			Graphics g = awtImage.getGraphics();
			view.print(g);

			JPEGCodec.createJPEGEncoder(out).encode(awtImage);

			

	}

	
	/**
	 * Chose a File of a given extension
	 * 
	 * @param extension
	 * @param description
	 * @return
	 */
	protected File choseFile(String extension,String description) {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.addChoosableFileFilter(new ExtensionFileFilter(extension,description));
		fileDialog.setDialogTitle("Select " + description + "file ...");

		// The default on Windows is to open at user.home, which is
		// typically an absurd directory inside the O/S installation.
		// So we use the current directory instead.
		String cwd = StringUtilities.getProperty("user.dir");

		if (cwd != null) {
			fileDialog.setCurrentDirectory(new File(cwd));
		}


		fileDialog.setSelectedFile(new File(fileDialog.getCurrentDirectory(),
				"view."+ extension));

		int returnVal = fileDialog.showDialog(this, "Export");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			return file;

		}

		return null;
	}
	@Override
	public void run() {

		view.refresh();

	}


	/**
	 * Filter to select files based on an extension.
	 *
	 */
	static class ExtensionFileFilter extends FileFilter {

		private String[] extensions;

		private String description;



		ExtensionFileFilter(String[] extensions,String description){
			this.extensions = extensions;
			this.description = description;
		}

		/**
		 * Convenience constructor with only one extension
		 * @param extension
		 * @param description
		 */
		ExtensionFileFilter(String extension,String description){
			String[] extensions = {extension};
			this.extensions = extensions;
			this.description = description;
		}


		public boolean accept(File fileOrDirectory) {
			if (fileOrDirectory.isDirectory()) {
				return true;
			}

			String fileOrDirectoryName = fileOrDirectory.getName();
			int dotIndex = fileOrDirectoryName.lastIndexOf('.');

			if (dotIndex == -1) {
				return false;
			}

			String extension = fileOrDirectoryName.substring(dotIndex);

			if (extension == null)
				return false;

			for(String e: extensions){   
				if (extension.equalsIgnoreCase("."+e)) {
					return true;
				}
			}

			return false;
		}

		/**  The description of this filter */
		public String getDescription() {
			return description;
		}
	}


}
