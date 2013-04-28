package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;


/**
 * hasMany: ChessPieces, Players
 * belongsTo: Room
 * @author chtran
 *
 */
public class ChessBoard extends Model {
	private ChessPiece[][] board;
	private Player white;
	private Player black;
	
	public ChessBoard(int id) {
		super(id);
		this.board = new ChessPiece[8][8];
		populateBoard();
	}
	
	private void populateBoard() {
		board[0][0] = new ChessPiece.Builder().rook().white().build();
		board[1][0] = new ChessPiece.Builder().knight().white().build();
		board[2][0] = new ChessPiece.Builder().bishop().white().build();
		board[3][0] = new ChessPiece.Builder().queen().white().build();
		board[4][0] = new ChessPiece.Builder().king().white().build();
		board[5][0] = new ChessPiece.Builder().bishop().white().build();
		board[6][0] = new ChessPiece.Builder().knight().white().build();
		board[7][0] = new ChessPiece.Builder().rook().white().build();
		
		board[0][7] = new ChessPiece.Builder().rook().black().build();
		board[1][7] = new ChessPiece.Builder().knight().black().build();
		board[2][7] = new ChessPiece.Builder().bishop().black().build();
		board[3][7] = new ChessPiece.Builder().queen().black().build();
		board[4][7] = new ChessPiece.Builder().king().black().build();
		board[5][7] = new ChessPiece.Builder().bishop().black().build();
		board[6][7] = new ChessPiece.Builder().knight().black().build();
		board[7][7] = new ChessPiece.Builder().rook().black().build();
		for (int i =0; i<8; i++) {
			board[i][1] = new ChessPiece.Builder().pawn().white().build();
			board[i][6] = new ChessPiece.Builder().pawn().black().build();
		}

	}
	
	public boolean isOccupied(int x, int y) {
		return (this.board[x][y]!=null);
	}
	
	public ChessPiece move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException {
		if (!isOccupied(from_x, from_y)) throw new IllegalMoveException();
		if (isOccupied(to_x,to_y)) {
			if (board[from_x][from_y].isWhite()==board[to_x][to_y].isWhite()) {
				throw new IllegalMoveException();
			}
		}
		if (!board[from_x][from_y].canMove(from_x, from_y, to_x, to_y)) throw new IllegalMoveException();
		ChessPiece captured = board[to_x][to_y];
		board[to_x][to_y] = board[from_x][from_y];
		board[from_x][from_y]=null;
		return captured;
	}
	public void put(ChessPiece piece, int x, int y) throws IllegalPlacementException {
		if (board[x][y]!=null) throw new IllegalPlacementException();
		board[x][y]=piece;
		
	}
	public ChessPiece getPiece(int x, int y) {
		return board[x][y];
	}

	public Player getWhitePlayer() {
		return this.white;
	}
	public Player getBlackPlayer() {
		return this.black;
	}
}
