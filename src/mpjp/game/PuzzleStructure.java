package mpjp.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import mpjp.shared.MPJPException;
import mpjp.shared.PuzzleInfo;
import mpjp.shared.geom.Point;

public class PuzzleStructure implements Iterable<Integer> {
	protected int rows;
	protected int columns;
	protected double width;
	protected double height;
	
	public PuzzleStructure(int rows, int columns, double width, double height) {
		super();
		this.rows = rows;
		this.columns = columns;
		this.width = width;
		this.height = height;
	}

	public PuzzleStructure(PuzzleInfo info) {
		super();
		this.rows = info.getRows();
		this.columns = info.getColumns();
		this.width = info.getWidth();
		this.height = info.getHeight();
	}

	

	Point getPieceCenterFacing(Direction direction, Point point) {
		double x = point.getX()+direction.getSignalX()*getPieceWidth();
		double y = point.getY()+direction.getSignalY()*getPieceHeight();
		return new Point(x,y);
	}

	public int getPieceColumn(int id) throws MPJPException  {
		if(id < 0 || id >= getPieceCount() )
			throw new MPJPException();
		return id%columns;
	}

	public int getPieceRow(int id) throws MPJPException {
		if(id < 0 || id >= getPieceCount()) 
			throw new MPJPException();
		
		return id/columns;
	}

	public int getPieceCount() {
		return rows*columns;
	}


	public Integer getPieceFacing(Direction direction, int id) throws MPJPException{
		if(getPieceRow(id)==0 && direction == Direction.NORTH)
			return null;
		if(getPieceRow(id)==rows-1 && direction == Direction.SOUTH)
			return null;
		if(getPieceColumn(id)==0 && direction == Direction.WEST)
			return null;
		if(getPieceColumn(id)==columns-1 && direction == Direction.EAST)
			return null;
		return id + direction.getSignalX() + direction.getSignalY()*columns;
	}

	public double getPieceHeight() {
		return height/rows;
	}

	public Point getPieceStandardCenter(int id) throws MPJPException {
		if(id < 0 || id >= getPieceCount() )
			throw new MPJPException();
		// a equação abaixo com 0.5 é a equação PieceColumn*PieceWidth + PieceWidth/2 simplificada
		return new Point((getPieceColumn(id)+0.5)*getPieceWidth(), (getPieceRow(id)+0.5)*getPieceHeight());
	}

	public double getPieceWidth() {
		return width/columns;
	}

	public Set<Integer> getPossiblePiecesInStandarFor(Point point) throws MPJPException {
		Set<Integer> set = new HashSet<Integer>();
		int pointID = getPieceFromPoint(point);
		set.add(pointID);
		for(Direction dir : Direction.values()) {
			if(getPieceFacing(dir, pointID) != null) {
				set.add(getPieceFacing(dir, pointID));
			
			if(dir.equals(Direction.NORTH)) {
				int pointIDNORTH = getPieceFacing(Direction.NORTH, pointID);
				if(getPieceFacing(Direction.WEST, pointIDNORTH) != null)
					set.add(getPieceFacing(Direction.WEST, pointIDNORTH) );
				if(getPieceFacing(Direction.EAST, pointIDNORTH) != null)
					set.add(getPieceFacing(Direction.EAST, pointIDNORTH));
			}
			
			if(dir.equals(Direction.SOUTH)) {
				int pointIDSOUTH = getPieceFacing(Direction.SOUTH, pointID);
				if(getPieceFacing(Direction.WEST, pointIDSOUTH) != null)
					set.add(getPieceFacing(Direction.WEST, pointIDSOUTH));
				if(getPieceFacing(Direction.EAST, pointIDSOUTH) != null)
					set.add(getPieceFacing(Direction.EAST, pointIDSOUTH));
				}
			}
		}
		
		return set;
	}

	public Point getRandomPointInStandardPuzzle() {
		return new Point(Math.random()*width, Math.random()*height);
	}

	public Map<Integer, Point> getStandardLocations() throws MPJPException {
		Map<Integer, Point> map = new HashMap<Integer, Point>();
		for(int i=0; i<getPieceCount(); i++)
			map.put(i,getPieceStandardCenter(i));
		return map;
	}

	@Override
	public Iterator<Integer> iterator() {

		LinkedList<Integer> list = new LinkedList<>();
		for(int i=0; i<getPieceCount(); i++) list.add(i);
		return list.iterator();
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public double getWidth() {
		return width;
	}

	void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	void setHeight(double height) {
		this.height = height;
	}
	
	private Integer getPieceFromPoint(Point point) {
		int row = (int) (point.getX()/getPieceHeight());
		int column = (int) ((point.getY())/getPieceWidth());
		
		return row+column*columns;
	}
	
}
