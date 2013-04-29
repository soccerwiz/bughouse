package edu.brown.cs32.bughouse.models;

/**
 * belongsTo: ChessBoard, Player
 * @author chtran
 *
 */
public class ChessPiece extends Model {
	private static final int PAWN = 1;
	private static final int KNIGHT = 2;
	private static final int BISHOP = 3;
	private static final int ROOK = 4;
	private static final int QUEEN = 5;
	private static final int KING = 6;
	
	private final boolean isWhite;
	private final int type;

	private ChessPiece(Builder builder) {
		super();
		this.type = builder.type;
		this.isWhite = builder.isWhite;
	}

	/****************************************************************************************************************
	 * Builder
	 * To get new piece do something like:
	 * ChessPiece p = new ChessPiece.Builder(10).white().pawn().build();
	 * - chtran
	 ****************************************************************************************************************/ 
	public static class Builder {
		private boolean isWhite;
		private int type;

		public Builder() {
		}
		

		public Builder black() {
			this.isWhite = false;
			return this;
		}
		public Builder white() {
			this.isWhite = true;
			return this;
		}
		public Builder setWhite(boolean isWhite) {
			this.isWhite = isWhite;
			return this;
		}
		public Builder king() {
			this.type = KING;
			return this;
		}
		public Builder queen() {
			this.type = QUEEN;
			return this;
		}
		public Builder rook() {
			this.type = ROOK;
			return this;
		}
		public Builder bishop() {
			this.type = BISHOP;
			return this;
		}
		public Builder knight() {
			this.type = KNIGHT;
			return this;
		}
		public Builder pawn() {
			this.type = PAWN;
			return this;
		}
		public Builder setType(int type) {
			this.type = type;
			return this;
		}
		public ChessPiece build() {
			return new ChessPiece(this);
		}
	}

	public boolean isWhite() {
		return this.isWhite;
	}
	
	public boolean canMove(int from_x, int from_y, int to_x, int to_y) {
		//TODO: fill
		if (to_x>=8 || to_y>=8 || to_x<0 || to_y<0) return false;
		switch (type) {
			case PAWN:
				return (from_x==to_x) && (to_y==from_y+1 || to_y==from_y-1);
			case KNIGHT:
				return true;
			case BISHOP:
				return true;
			case ROOK:
				return true;
			case QUEEN:
				return true;
			case KING:
				return true;
			default:
				return false;
			
		}
	}
	
	public int getType() {
		return this.type;
	}
	public static String getName(int type) {
		switch(type) {
			case 1:
				return "PAWN";
			case 2:
				return "KNIGHT";
			case 3:
				return "BISHOP";
			case 4:
				return "ROOK";
			case 5:
				return "QUEEN";
			case 6:
				return "KING";
		}
		return null;
	}
	public String getName() {
		return ChessPiece.getName(type);
	}
	public boolean isKing() {
		return (this.type==KING);
	}

	@Override
	public String toString() {
		String toReturn="";
		switch(type) {
		case 1:
			toReturn+="P";
			break;
		case 2:
			toReturn+="K";
			break;
		case 3:
			toReturn+="B";
			break;
		case 4:
			toReturn+="R";
			break;
		case 5:
			toReturn+="Q";
			break;
		case 6:
			toReturn+="X";
			break;
		}
		toReturn+= isWhite() ? "[W]" : "[B]";
		return toReturn;
	}
	
}
