package mpjp.game;

import mpjp.TestData;
import mpjp.shared.PuzzleInfo;

public class PuzzleData extends TestData {
	
	/**
	 * Data for testing puzzles
	 *
	 */
	protected enum Puzzle {
		/*    Image             Cutting      Rows  Columns Width Height*/
		P0("exterior2.jpg"	, "Straight"	,   8,  10,     100,   80),
		P1("exterior3.jpg"	, "Triangular"	,   8,  10,     200,  100),
		P2("exterior7.jpg"	, "Standard"	,  10,  10,     300,  300),
		P3(""				, "Straight"	,   2,   3,     800,  800);
		
		String image;
		String cutting;
		int rows;
		int columns;
		double width;
		double height;
		
		Puzzle(String image, String cutting, 
				int rows,int columns,
				double width,double height) {
			this.image = image;
			this.cutting = cutting;
			this.rows = rows;
			this.columns = columns;
			this.width = width;
			this.height = height;
		}
		
		public PuzzleStructure getStructure() {
			return new PuzzleStructure(rows,columns,width,height);
		}
		
		public PuzzleInfo getPuzzleInfo() {
			return new PuzzleInfo(image,cutting,rows,columns,width,height);
		}
	}
	
}
