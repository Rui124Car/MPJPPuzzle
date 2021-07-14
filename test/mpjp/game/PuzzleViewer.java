package mpjp.game;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import mpjp.game.cuttings.Cutting;
import mpjp.shared.MPJPException;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;


/**
 * Test application for viewing cuttings. It does not depend of the factory
 * and new binary names can be easily added.
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class PuzzleViewer extends Frame {
	private static final long serialVersionUID = 1L;
	
	private static final String[] CUTTING_BINARY_NAMES = {
			"mpjp.game.cuttings.StraightCutting",
			"mpjp.game.cuttings.TriangularCutting",
			"mpjp.game.cuttings.RoundCutting",
			"mpjp.game.cuttings.StandardCutting"
	};
	
	private static final int ROWS = 2;
	private static final int COLUMNS = 3;
	private static final int HEIGHT = 300;
	private static final int WIDTH = 600;

	
	
	public static void main(String[] args) {
		
		new PuzzleViewer(CUTTING_BINARY_NAMES[2],
				new PuzzleStructure(ROWS,COLUMNS,WIDTH,HEIGHT));
	}
	
	private PuzzleStructure structure; 
	private Map<Integer, PieceShape> shapes;
	
	public PuzzleViewer(String name,PuzzleStructure structure)  {	
		Cutting cutting = getCutting(name);
		init(cutting,structure);
	}
	
	
	public PuzzleViewer(Cutting cutting, PuzzleStructure structure)  {	
		init(cutting,structure);
	}
	
	void init(Cutting cutting, PuzzleStructure structure) {
		
		init(cutting.getShapes(structure),structure);
	}
	
	void init(Map<Integer,PieceShape> shapes, PuzzleStructure structure) {
		this.structure = structure;
		this.shapes = shapes;
				
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	    });
	}
	
	private Cutting getCutting(String name) {
		Cutting cutting;
		
		try {
			cutting = (Cutting) ClassLoader
					.getSystemClassLoader()
					.loadClass(name)
					.getDeclaredConstructor((Class<?>[]) null)
					.newInstance((Object[])null);
		} catch (InstantiationException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException cause) {
			
			throw new RuntimeException("Obtaining an instance of "+name,cause);
		}
		
		return cutting;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Insets insets = getInsets();
		
		int width  = (int) structure.getWidth()  + insets.left + insets.right; 
		int height = (int) structure.getHeight() + insets.top  + insets.bottom;
		setSize(width, height);
		g2.translate(insets.left, insets.top);
		
		AffineTransform initialTransform = g2.getTransform();
		for(int id: structure) 
			try {
				Shape shape = getPieceShape(id);
				Point center = structure.getPieceStandardCenter(id); 
				
				g2.translate(center.getX(),center.getY());
				g2.draw(shape);
				g2.setTransform(initialTransform);
				
			} catch(RuntimeException | MPJPException e) {
				System.out.println(e.getMessage());
			}
	}
	
	/**
	 * AWT Shape of piece with given id. Coordinates in this shape assume
	 * coordinate center at piece center.
	 * @param id of piece
	 * @return
	 */
	 private Shape getPieceShape(Integer id) {
		
		try {
			return ShapeChanger.getShape(shapes.get(id));
		} catch(Exception cause) {
			throw new RuntimeException("Error in piece "+id,cause);
		}
	}
	
}
