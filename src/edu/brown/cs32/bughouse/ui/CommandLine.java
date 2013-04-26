package edu.brown.cs32.bughouse.ui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import edu.brown.cs32.bughouse.client.BughouseBackEnd;
import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class CommandLine implements FrontEnd{
	BackEnd backend;
	List<Game> currentGames;
	public CommandLine(String host, int port) {
		try {
			this.backend = new BughouseBackEnd(this,host,port);
			this.run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			System.out.println("Server timed out.");
		}
	}
	private void showGames() throws IOException, RequestTimedOutException {
		System.out.println("Showing games");
		List<Game> gameList = backend.getActiveGames();
		
		System.out.println(gameList);
		if (gameList.isEmpty()) System.out.println("No game available.");
		for (Game g: gameList) {
			showGame(g);
		}
	}
	
	private void createGame() throws IOException, RequestTimedOutException {
		backend.createGame();
		System.out.println("Created new game. You are now in game #"+backend.me().getCurrentGame().getId());
	}
	
	private void showPlayers() throws IOException, RequestTimedOutException {
		showGame(backend.me().getCurrentGame());
	}
	
	private void showGame(Game g) throws IOException, RequestTimedOutException {
		System.out.println("Game #"+g.getId());
		System.out.print("Team 1: ");
		for (Player p: g.getPlayersByTeam(1)) System.out.print(p.getName()+" ");
		System.out.println();
		System.out.print("Team 2: ");
		for (Player p: g.getPlayersByTeam(2)) System.out.print(p.getName()+" ");
		System.out.println();
	}
	private void joinGame(String line) throws IOException, RequestTimedOutException, TeamFullException {
		String[] splitted = line.split(" ");
		int gameId = Integer.parseInt(splitted[1]);
		int team = Integer.parseInt(splitted[2]);
		backend.joinGame(gameId, team);
		System.out.printf("Joined game %d on team %d\n",gameId,team);
 	}
	private void startGame() throws IOException, RequestTimedOutException, GameNotReadyException {
		backend.startGame();
	}
	private void move(String line) throws IllegalMoveException, IOException, RequestTimedOutException {
		String[] splitted = line.split(" ");
		int from_x = Integer.parseInt(splitted[1]);
		int from_y = Integer.parseInt(splitted[2]);
		int to_x = Integer.parseInt(splitted[3]);
		int to_y = Integer.parseInt(splitted[4]);
		backend.move(from_x, from_y, to_x, to_y);
	}
 	public void run() throws IOException, RequestTimedOutException {
		System.out.print("Enter your name: ");
		Scanner stdIn = new Scanner(System.in);
		String line = stdIn.nextLine();
		
		backend.joinServer(line);
		System.out.println("Hello "+line);
		while(!line.equals("exit")) {
			try {
				line = stdIn.nextLine();
				String type = line.split(" ")[0];
				switch (type) {
					case "show_games":
						showGames();
						break;
					case "create_game":
						createGame();
						break;
					case "show_players":
						showPlayers();
						break;
					case "join":
						joinGame(line);
						break;
					case "exit":
						break;
					case "start_game":
						startGame();
						break;
					case "move":
						move(line);
						break;
					default:
						System.out.println("Unknown command: "+line);;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RequestTimedOutException e) {
				System.out.println("ERROR: Request timed out.");
			} catch (TeamFullException e) {
				System.out.println("ERROR: Team is full");
			} catch (GameNotReadyException e) {
				System.out.println("ERROR: Game not ready");
			} catch (IllegalMoveException e) {
				System.out.println("ERROR: Move not legal");
			}
		}
		
		System.out.println("Bye.");
		stdIn.close();
		backend.shutdown();
		System.exit(0);
	}
 
	@Override
	public void showEndGameMessage() {
		System.out.println("Game ended.");
	}

	@Override
	public void notifyUserTurn() {
		System.out.println("It's your turn");
	}

	@Override
	public void repaint() {
		return;
	}
	
	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		new CommandLine(host,port);
		
	}
	@Override
	public void addPrisoner(int playerId, ChessPiece piece) {
		String name;
		try {
			name = (new Player(playerId)).getName();
			System.out.printf("%s got a new %s\n",name,piece.getName());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			System.out.println("Request timed out.");
		}
	}
}
