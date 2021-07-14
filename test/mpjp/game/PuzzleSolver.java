package mpjp.game;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import mpjp.shared.MPJPException;
import mpjp.shared.PieceStatus;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.PuzzleLayout;
import mpjp.shared.PuzzleView;
import mpjp.shared.geom.Point;

/**
 * A widget to solve jigsaw puzzles and test {@link mpjp.game.Workspace}.
 * It can be run as a Java application with test examples.
 * 
 *  @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */
public class PuzzleSolver extends Canvas 
		implements MouseListener,  MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Resource directory pathname
	 */
	private static final String RESOURCE_DIR = "mpjp/resources/";
	/**
	 * Sound played when two or more pieces are connected
	 */
	private static final String CLICK_SOUND_NAME = "click.wav";  
	/**
	 * Sound played when puzzle is solved
	 */
	private static final String SOLVED_SOUND_NAME = "complete.wav";
	/**
	 * Sound played when an error has occurred
	 */
	private static final String ERROR_SOUND_NAME = "error.wav";
	/**
	 * Size of stroke when drawing piece border
	 */
	private static final int PIECE_STROKE_SIZE = 3;
	/**
	 * Medium grey with transparency used as piece border color 
	 */
	private static final Color PIECE_BORDER_COLOR = new Color(150,150,150,150);
	/**
	 * Time in milliseconds to redraw puzzle after completing initializations
	 * (to avoid unloaded images)  
	 */
	private static final long INIT_DELAY = 10L;
	/**
	 * Pooling delay to update layout data and update view
	 */
	private static final long POOLING_DELAY = 60L * 1000L;
	/**
	 * Font for piece labels and footer 
	 */
	private static final String FONT_NAME = "Helvetica";
	/**
	 * Ratio between piece dimensions and font label (when no image is used)
	 */
	private final static int FONT_LABEL_RATIO = 2; 
	/**
	 * Font size of text displayed in the footer (percentage of completion)
	 */
	private static final int FOOTER_FONT_SIZE = 12;
	/**
	 * Size of shade of moving piece whens hovering the other when dragged
	 */
	private static final int SHADE_SIZE = 4;
	
	/**
	 * Time in milliseconds of animation when puzzle is solved
	 */
	private static final long ANIMATION_TIME_IN_MILLISECS = 500L;
	/**
	 * Frames per second when playing animation
	 */
	private static final long FRAMES_PER_SEC = 20L;
	/**
	 * Delay between animation frames (depends on frames per second)
	 */
	private static final long ANIMAMTION_DELAY = 1000L / FRAMES_PER_SEC;
	/**
	 * Total of animation frames (depends of animation duration and FPS) 
	 */
	private static final int TOTAL_ANIMATION_FRAMES = (int)
			(ANIMATION_TIME_IN_MILLISECS * FRAMES_PER_SEC / 1000L);

	
	
	Workspace workspace;
	PuzzleView puzzleView;
	PuzzleLayout puzzleLayout;
	
	Integer selectedId;
	Integer selectedBlockId;
	Point start = null;
	Point delta, diff;
	
	Font labelFont;
	Font footerFont;
	Stroke stroke;
	
	RenderingHints hints;
	
	Image image = null;
	Map<String,Clip> clips = new HashMap<>();
	
	/**
	 * Are initializations complete?
	 */
	boolean initComplete = false;
	
	boolean solveComplete = false;
	
	/**
	 * Allow an empty puzzle solver, defined later with {@link #setWorkspace()}.
	 */
	PuzzleSolver() {}
	
	/**
	 * Start puzzle with given workspace
	 * @param workspace
	 * @throws MPJPException if cutting is invalid
	 */
	PuzzleSolver(Workspace workspace) throws MPJPException {
		initWorkspace(workspace);
	}
	
	/**
	 * Set workspace in viewer and display it
	 * 
	 * @param workspace to solve
	 * @throws MPJPException if cutting is invalid
	 */
	void setWorkspace(Workspace workspace) throws MPJPException {
		initWorkspace(workspace);
		doublePaint();
	}
	
	/**
	 * Initialize workspace
	 * 
	 * @param workspace
	 * @throws MPJPException if cutting is invalid
	 */
	private void initWorkspace(Workspace workspace) throws MPJPException {
		this.initComplete = false;
		this.workspace = workspace;
		puzzleView = workspace.getPuzzleView();
		puzzleLayout = workspace.getCurrentLayout();

		int width = (int) puzzleView.getWorkspaceWidth();
		int height = (int) puzzleView.getWorkspaceHeight();
		
		setSize(width, height);
		setVisible(true);	
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		labelFont = new Font(FONT_NAME,Font.BOLD,computeLabelSize());
		footerFont = new Font(FONT_NAME,Font.BOLD,FOOTER_FONT_SIZE);
		stroke = new BasicStroke(
				PIECE_STROKE_SIZE,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND);
	
		hints = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
		
		loadImage(puzzleView);
		
		if(clips.size() == 0) // avoid reloading clips
			loadSoundClips(CLICK_SOUND_NAME,SOLVED_SOUND_NAME,ERROR_SOUND_NAME);
		
		resetDoubleBuffering();
		
		initComplete = true;
	
		solveComplete = false;
		
		poolAndPaint();
	}
	
	
	Timer poolAndPaintTimer = null;
	
	/**
	 * Pool data (needed on in concurrent solving) and paints the puzzle.
	 * This will update the elapsed time on the footer 
	 * even if the person is idle.
	 */
	private void poolAndPaint() {
		
		if(poolAndPaintTimer != null) 
			poolAndPaintTimer.cancel();
	
		poolAndPaintTimer = new Timer();
		poolAndPaintTimer.schedule(new TimerTask() {
		
			@Override
			public void run() {
				if(solveComplete)
					cancel();
				else {
					puzzleLayout = workspace.getCurrentLayout(); // Unnecessary locally
					doublePaint();
				}
			}
		}, POOLING_DELAY,POOLING_DELAY); 
	}
	
	
	/**
	 * Font size for piece labels, as function of piece dimensions
	 * 
	 * @return size
	 */
	private int computeLabelSize() {
		return (int) (Math.min(
				puzzleView.getPieceWidth(),
				puzzleView.getPieceHeight())) / FONT_LABEL_RATIO;
	}

	/*____________________________________________________________*\
	 *                                                            *
	 *                     RESOURCE LOADING                       *
	\*____________________________________________________________*/

	
	/**
	 * Load image if available
	 * @param puzzleView
	 */
	private void loadImage(PuzzleView puzzleView) {
		String imageName = puzzleView.getImage();
		
		if(imageName == null || "".equals(imageName)) 
			image = null;
		else {
			URL url = ClassLoader.getSystemResource(RESOURCE_DIR+imageName);
			try {
				if(url == null)
					throw new RuntimeException("Image not found: "+imageName);
					
				image = ImageIO.read(url);
			} catch (RuntimeException | IOException e) {
				error(e);
			}
		} 
	}
	
	/**
	 * Load all the sound clips passed as arguments and store them in clips map
	 * 
	 * @param soundNames
	 */
	private void loadSoundClips(String... soundNames) {
		for(String soundName: soundNames) {
			String path = RESOURCE_DIR+soundName;
			InputStream stream = ClassLoader.getSystemResourceAsStream(path);

			try {
				if(stream == null)
					throw new RuntimeException("Sound clip not found: "+soundName);

				AudioInputStream audioInputStream = 
						AudioSystem.getAudioInputStream(stream);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				
				clips.put(soundName, clip);

			} catch (LineUnavailableException | IOException | 
					UnsupportedAudioFileException | RuntimeException e) {
				error(e);
			}
		}
	}
	
	/**
	 * Play a loaded sound clip from the start
	 * @param soundName
	 */
	private void playClip(String soundName) {
		Clip clip = clips.get(soundName); 
		
		clip.setFramePosition(0);
		clip.start();
	}
	
	/*____________________________________________________________*\
	 *                                                            *
	 *                     PAINTING METHODS                       *
	\*____________________________________________________________*/
	
	@Override
	public void paint(Graphics g) {
		
		if(initComplete) { 
			doublePaint();
		} else // make sure this puzzle is drawn only after initializations
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					paint(g);				
				}}, INIT_DELAY);
	}
	
	/**
	 * Repaint with double buffering
	 */
	void doublePaint() {
		
		if(solveComplete) 
			paintFinal();
		else if(puzzleLayout.isSolved()) {
			solveComplete = true;
			animateSolvedPuzzle(); 
		} else 
			doublePaint(getGraphics());
	}
	
	private Graphics2D context = null;
	private Image buffer = null;
	
	/**
	 * Repaint with double buffering with given graphic context 
	 * @param g graphic context
	 */
	void doublePaint(Graphics g) {
		
		initDoubleBuffer();
		paintBlocks(context);
		g.drawImage(buffer, 0, 0, null);
	
	}
	
	
	/**
	 * Initialize double buffering
	 */
	private void initDoubleBuffer() {
		int width = (int) puzzleView.getWorkspaceWidth(); 
		int height = (int) puzzleView.getWorkspaceHeight();
		
		if(context == null) {
			buffer = createImage(width,height);
			context = (Graphics2D) buffer.getGraphics();
		} else
			context.clearRect(0,0,width,height);
	}
	
	/**
	 * If workspace dimensions are changed then the double must be reset
	 */
	private void resetDoubleBuffering() {
		context = null;
	}
	
	
	/**
	 * Animate the complete puzzle (without the pieces borders)
	 * from current position until filling the complete workspace 
	 */
	private void animateSolvedPuzzle() {
		PuzzleSolver solver = this;
		
		int totalWidth  = (int) puzzleView.getWorkspaceWidth();
		int totalHeight = (int) puzzleView.getWorkspaceHeight();

		int puzzleWidth = (int) puzzleView.getPuzzleWidth();
		int puzzleHeight = (int) puzzleView.getPuzzleHeight();

		Map<Integer,PieceStatus> pieces = puzzleLayout.getPieces();
		PieceStatus piece0 = pieces.get(0);
		Point location0 = puzzleView.getStandardPieceLocation(0);

		int x0 = (int) (piece0.getX() - location0.getX()); 
		int y0 = (int) (piece0.getY() - location0.getY());
		
		playClip(SOLVED_SOUND_NAME);
		
		(new Timer()).scheduleAtFixedRate(new TimerTask() {
			private int count = 0;

			@Override
			public void run() {
				Graphics g 	= getGraphics(); // needs to be here!
				int x		= inLine(x0,0);
				int y 		= inLine(y0,0);
				int width 	= inLine(puzzleWidth,totalWidth);
				int height 	= inLine(puzzleHeight,totalHeight);
				
				if(image == null) {
					g.setColor(Color.WHITE);
					g.fillRect(x,y,width,height);
				} else
					g.drawImage(image,x,y,width,height,solver);
				
				Toolkit.getDefaultToolkit().sync();

				if(count++ == TOTAL_ANIMATION_FRAMES)
					cancel();
			}	

			private int inLine(int start, int end) {
				return start +  count * (end - start) / TOTAL_ANIMATION_FRAMES;
			}

		}, 0L, ANIMAMTION_DELAY);

	}
	
	/**
	 * Default paint when puzzle is solved
	 */
	private void paintFinal() {
		Graphics g = getGraphics();
		int totalWidth  = (int) puzzleView.getWorkspaceWidth();
		int totalHeight = (int) puzzleView.getWorkspaceHeight();
		
		if(image == null) {
			g.setColor(Color.WHITE);
			g.fillRect(0,0,totalWidth,totalHeight);
		} else
			g.drawImage(image,0,0,totalWidth,totalHeight,this);
	}
		
	/**
	 * Paint all the blocks, leaving the selected block for last
	 * to make it hover over the others
	 * 
	 * @param g2 graphic context 
	 */
	private void paintBlocks(Graphics2D g2) {
		Map<Integer, List<Integer>> blocks = puzzleLayout.getBlocks();
		
		g2.setRenderingHints(hints);
		
		for(int blockId : blocks.keySet()) {
			if(selectedBlockId != null && blockId == selectedBlockId)
				continue;
			paintBlock(g2,blocks.get(blockId),false);
		}
		
		if(selectedBlockId != null)
			paintBlock(g2,blocks.get(selectedBlockId),true);
		
		showFooter(g2);	
	}
	
	
	/**
	 * Paint a block of connected (thus, non-overlapping) pieces
	 * 
	 * @param g2 graphic context
	 * @param pieceIDs list piece IDs
	 * @param dragging {@true} if user dragging; {@false} otherwise
	 */
	private void paintBlock(Graphics2D g2,List<Integer> pieceIDs,boolean dragging) {
		AffineTransform initialTransform = g2.getTransform();
		
		try {
			if(dragging)
				for(int id: pieceIDs) {
					g2.translate(SHADE_SIZE,SHADE_SIZE);
					paintPiece(g2,id,dragging,true);
					g2.setTransform(initialTransform);
				}
		
			for(int id: pieceIDs) {
				paintPiece(g2,id,dragging,false);
				g2.setTransform(initialTransform);
			}
		} catch(RuntimeException e) {
				error(e);
		}
	}
	
	/**
	 * Paint a single piece in a block, or its shade
	 *  
	 * @param g2 graphic context
	 * @param id of piece being painted
	 * @param dragging {@true} if user dragging; {@false} otherwise
	 * @param shading {@true} to paint shade; {@false} otherwise
	 */
	void paintPiece(Graphics2D g2,int id,boolean dragging,boolean shading) {
		Shape shape  = ShapeChanger.getShape(puzzleView.getPieceShape(id));
		Map<Integer, PieceStatus> pieces = puzzleLayout.getPieces();
		PieceStatus pieceStatus = pieces.get(id);
		Point center =  pieceStatus.getPosition();
		double rotation = pieceStatus.getRotation();

		if(dragging)
			g2.translate(delta.getX(),delta.getY());
		g2.translate(center.getX(),center.getY());
		g2.rotate(rotation);
		
		if(shading)
			paintShade(g2,shape);
		else if(image == null) 
			paintPieceWithLabel(g2,id,shape);
		else 
			paintPieceWithImage(g2,id,shape);
		
	}
	
	/**
	 * Paint shade of a piece given its shape
	 * 
	 * @param g2 graphic context
	 * @param shape of piece
	 */
	private void paintShade(Graphics2D g2,Shape shape) {
		g2.setColor(Color.DARK_GRAY);
		g2.fill(shape);
	}
	
	/**
	 * Paint a piece given its shape its ID as label (rather than an image)
	 * 
	 * @param g2 graphic context
	 * @param id of piece
	 * @param shape of piece
	 */
	private void paintPieceWithLabel(Graphics2D g2, int id,Shape shape) {
		g2.setFont(labelFont);
		
		FontMetrics metrics = g2.getFontMetrics(labelFont);
		String label = Integer.toString(id);
		int x = - metrics.stringWidth(label) / 2;
		int y = metrics.getAscent() / 2;
	
		paintPieceBorder(g2,shape);
		g2.setColor(Color.WHITE);
		g2.fill(shape);
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawString(label,x,y);
	}
	
	/**
	 * Paint a piece given its shape with the image as background
	 * 
	 * @param g2 graphic context
	 * @param id of piece
	 * @param shape of piece
	 */
	private void paintPieceWithImage(Graphics2D g2, int id,Shape shape) {
		Point location = puzzleView.getStandardPieceLocation(id);
		int x = (int) -location.getX();
		int y = (int) -location.getY();
	
		int width = (int) puzzleView.getPuzzleWidth();
		int height = (int) puzzleView.getPuzzleHeight();
		
		paintPieceBorder(g2,shape);
		g2.setClip(shape);
		g2.drawImage(image, x, y, width, height, this);
		g2.setClip(null);
	}
	
	/**
	 * Paint piece border 
	 * 
	 * @param g2 graphic context
	 * @param shape of piece
	 */
	private void paintPieceBorder(Graphics2D g2,Shape shape) {
		g2.setColor(PIECE_BORDER_COLOR);
		g2.setStroke(stroke);
		g2.draw(shape);
	}
	
	/**
	 * Show footer with puzzle status: percentage complete e solving time
	 * @param g2 graphics
	 */
	private void showFooter(Graphics2D g2) {
		int complete = puzzleLayout.getPercentageSolved();
		Date start = puzzleView.getStart();
		int elapsed = (int) ((new Date().getTime() - start.getTime())/60L/1000L);
		int hours  = elapsed / 60;
		int minutes = elapsed % 60;
		String label= String.format("%d%% - %02d:%02d",complete, hours,minutes);
		
		g2.setFont(footerFont);
		
		double width  = puzzleView.getWorkspaceWidth();
		double height = puzzleView.getWorkspaceHeight();
		FontMetrics metrics = g2.getFontMetrics(footerFont);
		int x = (int) ((width - metrics.stringWidth(label)) / 2);
		int y = (int) (height - metrics.getAscent() );
		
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(label,x,y);
	}

	/*____________________________________________________________*\
	 *                                                            *
	 *                     EVENT HANDLING                         *
	\*____________________________________________________________*/
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		start = new Point(e.getX(),e.getY());
		Integer id = workspace.selectPiece(start);

		if(id == null)
			selectedBlockId = null;
		else {
			PieceStatus piece = puzzleLayout.getPieces().get(id);
			selectedId = id;
			selectedBlockId = piece.getBlock();
			delta = new Point(0,0);
			diff  = new Point(e.getX() - piece.getX(),e.getY() - piece.getY());
			doublePaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		
		if(selectedId != null) {
			if(withinWorkspace(event)) {
				int blockCount = puzzleLayout.getBlocks().size();
				double x = event.getX() - diff.getX();
				double y = event.getY() - diff.getY();
				Point point = new Point(x,y);
			
				try {
					puzzleLayout = workspace.connect(selectedId,point);

					if(puzzleLayout.getBlocks().size() < blockCount) 
						playClip(CLICK_SOUND_NAME);
				} catch(MPJPException cause) {
					playClip(ERROR_SOUND_NAME);
				}
		
				start = null;
				selectedId = null;
				selectedBlockId = null;
			}
			
			doublePaint();
		}
	}


	@Override
	public void mouseDragged(MouseEvent event) {
		if(start != null) { 
			int x = event.getX();
			int y = event.getY();
			
			if(withinWorkspace(event))
				delta = new Point(x - start.getX(), y - start.getY());
			doublePaint();
		}
	}
	
	/**
	 * Checks if event is within this workspace. When dragging, events
	 * with coordinates outside the window can be sent to it.
	 * 
	 * @param event to check
	 * @return {@code true} is event is within workspace, false otherwise 
	 */
	private boolean withinWorkspace(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		int width  = (int) puzzleView.getWorkspaceWidth();
		int height = (int) puzzleView.getWorkspaceHeight(); 
		
		return x >= 0 && x <= width && y >= 0 && y <= height;
	}
	
	
	/*____________________________________________________________*\
	 *                                                            *
	 *                     Ignore these events                    *
	\*____________________________________________________________*/
	
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	private static long TOO_FREQUENT_UPDATES = 100L; 
	private static Date lastUpdate = new Date();
	
	/**
	 *           Test by executing as Java application              
	 *           
	 * @throws MPJPException if cutting is invalid 
	 */
	public static void main(String[] args) throws MPJPException {
		
		class MyTestData extends PuzzleData {
			PuzzleInfo getPuzzleInfo(String puzzleId) {
				return Puzzle.valueOf(puzzleId).getPuzzleInfo();
			}
		}
		
		MyTestData data = new MyTestData();
		PuzzleInfo info = data.getPuzzleInfo("P2");
		Workspace workspace = new Workspace(info);
		PuzzleView puzzleView = workspace.getPuzzleView();
		Frame frame = new Frame("Puzzle solver");
		
		System.out.println(frame.getInsets());
		
		frame.add(new PuzzleSolver(workspace));
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			
			/**
			 * Set dimensions only after knowing insets 
			 */
			@Override
			public void windowActivated(WindowEvent e) {
				Dimension dimension = getDimension(frame,puzzleView);
				
				frame.setSize(dimension);
				frame.setMaximumSize(dimension);
				frame.setMinimumSize(dimension);
				
				frame.setVisible(true);
			}

			public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }
	    });    
		
		// Set maximum size doesn't prevent resizing
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				Date now = new Date();
				
				// avoid frequent updates induced by maximizing
				// maximizing events aren't always reported 
				if((now.getTime() - lastUpdate.getTime()) > TOO_FREQUENT_UPDATES) {
					frame.setSize(getDimension(frame,puzzleView));
					lastUpdate = now;
				}
			}
		});
	}
	
	private static Dimension getDimension(Frame frame,PuzzleView puzzleView) {
		Insets insets = frame.getInsets();
		int width 	= (int) puzzleView.getWorkspaceWidth() + 
				insets.left + insets.right;
		int height 	= (int) puzzleView.getWorkspaceHeight() +
				insets.top + insets.bottom;
		return new Dimension(width, height);
	}
	
	private void error(Exception e) {
		e.printStackTrace();
	}
		
}
