import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;

public class Game{
	//instance variables
	private static boolean hosting = false;
	private static boolean joining = false;
	private static Scanner sc = new Scanner(System.in);
	private static String response;
	private static String hostLobbyName;
	private static String hostLobbyPort;
	private static BufferedReader in;
	private static GameServer gameServer;
	private static GameClient gameClient;
	private static int port;



	//constructor
	public Game(){

	}//end of constructor

	//main method
	public static void main(String[] args) throws Exception{

		//set up input reader
		in = new BufferedReader(new InputStreamReader(System.in));
		
		//Prompt the player to start matchmaking
		System.out.println("Please Select an option");
		System.out.println("1. Host game");
		System.out.println("2. Join game\r\n");

		//get input from the user
		response = read();		

		//set flags based on input
		if(response.equals("1")){
			hosting = true;
		}

		if (response.equals("2")){
			joining = true;
		}

		else if(response.equals("1") && response.equals("2")){			
			System.out.println("Invalid response. Exiting\r\n");
			timeDelay(4000);
			System.exit(0);
		}

		if(hosting){			
			createLobby();
		}

		if(joining){			
			joinLobby();			
		}

		gameSetup();		
	}//end of main 


	//methods 
	public static void timeDelay(long t) {
    	try {
        	Thread.sleep(t);
    	} catch (InterruptedException e) {}
	}//end of timeDelay url: https://stackoverflow.com/questions/37042149/making-a-delay-without-try-catch-or-having-it-in-one-function

	public static String read(){
		String output = new String();
		try{
			output = in.readLine();
		}
		catch(IOException e){}
		return output;
	}//end of read

	public static void createLobby()throws Exception{

		InetAddress ip = InetAddress.getLocalHost();
		
		/*
		System.out.println("Please input the name of the host lobby\r\n");
		hostLobbyName = read();
		//hostLobbyName = sc.nextLine();
		//sc.reset();
		System.out.println("Please input the password for the host lobby\r\n");
		hostLobbyPassword = read();
		*/
		System.out.println("Creating lobby: " + InetAddress.getLocalHost().toString());

		createGameServer(ip);


		//hostLobbyPassword = sc.nextLine();
		//sc.reset();
		//System.out.println("waiting for player 2 to join\r\n");
	}//end of createLobby

	public static void joinLobby() throws Exception{
		System.out.println("Please enter the ip address you would like to connect to.\r\n");
		hostLobbyName = read();
		//hostLobbyName = sc.nextLine();
		//sc.reset();
		
		System.out.println("Please input the port number for the host lobby\r\n");
		hostLobbyPort = read();
		port = Integer.parseInt(hostLobbyPort);
		
		//hostLobbyPassword = sc.nextLine();
		//sc.reset();
		System.out.printf("Searching for a lobby @ %s\r\n", hostLobbyName);

		joinGameServer(hostLobbyName, port);


	}//end of joinLobby


	public static void createGameServer(InetAddress ip) throws Exception{
				
		//System.out.println("Debug: server is about to be instantiated");
		//gameServer = new GameServer(InetAddress.getLocalHost().toString());
		gameServer = new GameServer(ip);

		//System.out.println("Debug: server has been instantiated");
		//GameServer app = new GameServer(args[0]);
        System.out.println("\r\nRunning Server: " + 
                "Host=" + gameServer.getSocketAddress().getHostAddress() + 
                " Port=" + gameServer.getPort());
        
        System.out.println("Waiting for player 2 to connect");
        gameServer.listen();


	}//end of create game server

	public static void joinGameServer(String ip, int port) throws Exception{
		
		gameClient = new GameClient(InetAddress.getByName(ip), port);
		

		//GameClient client = new GameClient(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));        
        System.out.println("\r\nConnected to Server: " + gameClient.socket.getInetAddress());
        gameClient.start();
		

	}//end of join game server

	public static void gameSetup(){

	}

	







}//end of class