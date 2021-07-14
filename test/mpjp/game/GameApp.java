package mpjp.game;

import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mpjp.game.cuttings.CuttingFactory;
import mpjp.game.cuttings.CuttingFactoryImplementation;
import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleView;

/**
 * Java application with a graphic user interface (GUI) to perform
 * final tests on the types provided by the {@code mpjp.game} package. 
 * Must be run as a Java application, <b>not</b> as a JUnit test.  
 *
 *  @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class GameApp extends Frame {
	

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_SIZE = 400;
	
	private Map<String,String> data = new HashMap<>();
	private CuttingFactory factory = new CuttingFactoryImplementation();
	private List<String> images = Arrays.asList(
			"exterior2.jpg",
			"exterior3.jpg",
			"exterior7.jpg",
			""
			);	
	private List<String> strutures =  Arrays.asList(
			"1x2",
			"2x3",
			"3x4",
			"4x4",
			"5x6",
			"7x9",
			"8x8",
			"10x10",
			"13x13",
			"15x15"
			);
	private List<String> dimensions =  Arrays.asList(
			"300x300",
			"300x400", 
			"400x400",
			"500x500"
			);
	private PuzzleSolver solver = new PuzzleSolver();
	private Dimension dimension;
	
	/**
	 * Makeshift radio buttons menu using check boxes (AWT doesn't have one) 
	 * 
	 */
	class MyRadioMenu extends Menu implements ItemListener {
		private static final long serialVersionUID = 1L;
		
		MyRadioMenu(String name,List<String> itemNames,int initial) {
			super(name);
			init(name,itemNames,initial);
		}
		
		MyRadioMenu(String name,Set<String> itemNames,int initial) {
			super(name);
			init(name,new ArrayList<>(itemNames),initial);
		}
		
		void init(String name,List<String> itemNames,int initial) {
			int count = 0;
			for(String itemName: itemNames) {
				boolean state = count++ == initial;
				CheckboxMenuItem item = new CheckboxMenuItem(itemName, state); 
			
				add(item);
				item.addItemListener(this);
			}
						
			data.put(name,itemNames.get(initial));
		}
		
		@Override
		public void itemStateChanged(ItemEvent event) {
			CheckboxMenuItem source = (CheckboxMenuItem) event.getSource();
			String menuName = getLabel();
			String itemName = source.getLabel();
			
			data.put(menuName,itemName);
			
			for(int i=0; i< getItemCount(); i++) {
				CheckboxMenuItem item = (CheckboxMenuItem) getItem(i);
				
				item.setState(item.getActionCommand().equals(itemName));
			}
			
			try {
				createPuzzle();
			} catch (MPJPException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Creates a an application instance
	 * 
	 * @throws MPJPException if cutting is invalid
	 */
	GameApp() throws MPJPException {
		MenuBar bar = new MenuBar();
		
		bar.add(new MyRadioMenu("Image",images,0));
		bar.add(new MyRadioMenu("Cutting",factory.getAvaliableCuttings(),2));
		bar.add(new MyRadioMenu("Structure",strutures,0));
		bar.add(new MyRadioMenu("Dimension",dimensions,0));
		
		setMenuBar(bar);
		
		add(solver);

		dimension = new Dimension(DEFAULT_SIZE,DEFAULT_SIZE);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			
			
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});    
		
		// Set maximum size doesn't prevent resizing
		addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	setSize(dimension);
		    }
		});
		
		createPuzzle();
	}
	
	/**
	 * Creates a puzzle with currently selected data
	 * @throws MPJPException if cutting is invalid
	 */
	void createPuzzle() throws MPJPException {
		String		imageName   = data.get("Image");
		String 		cuttingName = data.get("Cutting");
		String[] 	structure	= data.get("Structure").split("x");
		String[] 	dimention	= data.get("Dimension").split("x");
		int 		rows 		= Integer.parseInt(structure[0]);
		int 		columns 	= Integer.parseInt(structure[1]);
		double 		width 		= Double.parseDouble(dimention[0]);
		double 		height 		= Double.parseDouble(dimention[1]);
		
		PuzzleInfo	puzzleInfo	= new PuzzleInfo(
				imageName,
				cuttingName,
				rows,columns,
				width,height
				);
		
		Workspace workspace = new Workspace(puzzleInfo);
		PuzzleView view = workspace.getPuzzleView();
		Insets insets  = getInsets();
		int workWidth  = (int) view.getWorkspaceWidth() + insets.left + insets.right;
		int workHeight = (int) view.getWorkspaceHeight() + insets.top + insets.bottom;
		
		solver.setWorkspace(workspace);
		
		dimension = new Dimension(workWidth,workHeight);
		
		setSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
	}

	public static void main(String[] args) throws MPJPException {
		new GameApp();
	}

}
