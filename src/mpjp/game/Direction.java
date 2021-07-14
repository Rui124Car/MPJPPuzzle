package mpjp.game;

public enum Direction{
	EAST(1,0), NORTH(0,-1), WEST(-1,0), SOUTH(0,1);
	
	final int X;
	final int Y;
	
	Direction(final int X, final int Y){
		this.X = X;
		this.Y = Y;
	}

	Integer getSignalX() {
		return X;
	}

	Integer getSignalY() {
		return Y;
	}
}
