package mpjp.game;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.List;

import mpjp.shared.geom.CurveTo;
import mpjp.shared.geom.LineTo;
import mpjp.shared.geom.PieceShape;
import mpjp.shared.geom.Point;
import mpjp.shared.geom.QuadTo;
import mpjp.shared.geom.Segment;

public class ShapeChanger {
	
	/**
	 * Convert a PieceShape into a {@code java.asw.Shape},
	 * useful for displaying using AWT and use its features 
	 * 
	 * @param pieceShape
	 * @return
	 */
	public static Shape getShape(PieceShape pieceShape) {
		GeneralPath path = new GeneralPath();
		Point start = pieceShape.getStartPoint();
		
		path.moveTo(start.getX(), start.getY());
		
		for(Segment segment: pieceShape.getSegments()) {
			if(segment instanceof LineTo) {
				LineTo lineTo = (LineTo) segment;
				Point endPoint = lineTo.getEndPoint();
				
				path.lineTo(endPoint.getX(), endPoint.getY());
			} else if(segment instanceof QuadTo) {
				QuadTo quadTo = (QuadTo) segment;
				Point endPoint = quadTo.getEndPoint();
				Point controlPoint = quadTo.getControlPoint();
				
				path.quadTo(
						controlPoint.getX(),controlPoint.getY(),
						endPoint.getX(), endPoint.getY());
			} else if(segment instanceof CurveTo) {
				CurveTo curveTo = (CurveTo) segment;
				Point endPoint = curveTo.getEndPoint();
				Point controlPoint1 = curveTo.getControlPoint1();
				Point controlPoint2 = curveTo.getControlPoint2();
				
				path.curveTo(
						controlPoint1.getX(),controlPoint1.getY(),
						controlPoint2.getX(),controlPoint2.getY(),
						endPoint.getX(), endPoint.getY());
			}
		}
		
		
		return path;
	}
	
	
	
	void makeSegment(GeneralPath path,QuadTo quadTo) {
		Point endPoint = quadTo.getEndPoint();
		
		path.lineTo(endPoint.getX(), endPoint.getY());
	}
	
	void makeSegment(GeneralPath path,CurveTo curveTo) {
		Point endPoint = curveTo.getEndPoint();
		
		path.lineTo(endPoint.getX(), endPoint.getY());
	}
	
	
	/**
	 * Shape of piece from a boundary, a list of lists of points
	 * The first element of the list is a list with a single point, the origin.
	 * The following list may have 1, 2 or 3 points:
	 * <ul>
	 *   <li> 1 - a line from the previous end point to the given point;
	 *   <li> 2 - a quadratic curve, where 2 the first is the control point;
	 *   <li> 3 - a Bezier curve with 2 control points.
	 * </ul>
	 * Coordinates in this shape assume coordinate center at piece center.
	 * @param bounday of piece
	 * @return
	 */
	public static Shape getShape(List<List<Point>>  boundary) {
		
		if(boundary.size() < 3)
			throw new RuntimeException("Too few boundary segments on piece : "+boundary.size());
		else {
			GeneralPath path = new GeneralPath();
			List<Point> first = boundary.get(0);
			
			switch(first.size()) {
			case 0:
				throw new RuntimeException("No start point on first segment on piece ");
			case 1:
				break;
			default:
				throw new RuntimeException("Just 1 point expected on first line");
			}
			
			Point start = first.get(0);
			path.moveTo(start.getX(), start.getY());
			
			Point a, b, p;
			int size = boundary.size();
			int count = 0;
			for(List<Point> segment: boundary.subList(1, size)) {
				count++;
				switch(segment.size()) {
				case 0:
					throw new RuntimeException("No points on segment #"+count);
				case 1:
					p = segment.get(0);
					path.lineTo(p.getX(), p.getY());
					break;
				case 2:
					a = segment.get(0);
					p = segment.get(1);
					path.quadTo(a.getX(),a.getY(),p.getX(),p.getY());
					break;
				case 3:
					a = segment.get(0);
					b = segment.get(1);
					p = segment.get(2);
					path.curveTo(a.getX(),a.getY(),b.getX(),b.getY(),p.getX(),p.getY());
					break;
				default:
					throw new RuntimeException("Too many points on segment #"+count);
				}
			}
			return path;
		}
	}

}
