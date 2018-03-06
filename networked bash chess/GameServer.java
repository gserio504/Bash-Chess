//imported packages
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer{
	//instance variables
	private ServerSocket serverSocket;
	private String serverIPAddress;
	private String data;



	//constructor
	public GameServer(InetAddress ipAddress) throws Exception{
		
		this.serverIPAddress = InetAddress.getLocalHost().toString();

		if (ipAddress != null) 
          this.serverSocket = new ServerSocket(0, 1, ipAddress);
        else 
          this.serverSocket = new ServerSocket(0, 1, ipAddress);
	}//end of constructor

	//Methods
/*
	//main
	public static void main(String[] args) throws Exception {
        
        GameServer app = new GameServer(args[0]);
        System.out.println("\r\nRunning Server: " + 
                "Host=" + app.getSocketAddress().getHostAddress() + 
                " Port=" + app.getPort());
        
        app.listen();
    }//end of main*/

	//probably need to add another listen(client this.server???) method
	public void listen() throws Exception {
        //String data = null;
        Socket client = this.serverSocket.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));        
        while ( (data = in.readLine()) != null ) {
            System.out.println("\r\nMessage from " + clientAddress + ": " + data);
    	}
    }//end of listen
    
    /*
    private void listen(Socket client) throws Exception{

    }	*/



    public InetAddress getSocketAddress() {
        return this.serverSocket.getInetAddress();
    }//end of get socket address
    
    public int getPort() {
        return this.serverSocket.getLocalPort();
    }//end of get port

    











}//end of GameServer