//package imports
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class GameClient{
	//instance variables
	public Socket socket;
    public Scanner scanner;

    //constructor
    public GameClient(InetAddress serverAddress, int serverPort) throws Exception{
    	this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }//end of constructor(InetAddress, int)

    //main method
    public static void main(String[] args) throws Exception {
        GameClient client = new GameClient(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));        
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();                
    }//end of main

    //start method
    public void start() throws IOException {
        String input;
        while (true) {
            input = scanner.nextLine();
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(input);
            out.flush();
        }
    }//end of start

}//end of game client