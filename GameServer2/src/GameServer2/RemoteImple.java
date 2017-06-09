package GameServer2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import GameInterface2.JustInterface;


public class RemoteImple extends UnicastRemoteObject implements JustInterface {

	public static int fly_size_x = 50;
	public static int fly_size_y = 60;		
	public static int fly_pos_x = 0;
	public static int fly_pos_y = 0;
	
	final static int WIDTH = 750;
	final static int HEIGHT = 650;
	
	//Player Attributes	
	//Map<String, Integer> plawyer_list = new HashMap<String, int>();
	static ConcurrentHashMap<String, Integer> player_list = new ConcurrentHashMap<String, Integer>();
	
	protected RemoteImple() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getX () {
		return fly_pos_x;
	}
	public int getY () {
		return fly_pos_y;
	}

	@Override
	public boolean login(String playerName) throws RemoteException {
		int score = 0;
		player_list.put(playerName, score);
		return true;	
		
	}

	@Override
	public boolean logout(String playerName) throws RemoteException {
		player_list.remove(playerName);
		return true;
	}

	public void addScore(String player_name){
		int previous_score = player_list.get(player_name);
		int new_score = previous_score + 1;
		player_list.put(player_name, new_score);
		
	}
	
	public int getScore(String player_name){		
		return player_list.get(player_name);
	}
	
	public String getAllScores(){
		return player_list.toString();		
	}
	
	@Override
	public boolean checkFlyIsHunted(int mouse_clicked_x, int mouse_clicked_y, String playerName){
		   		   
		   System.out.println(player_list);
		   
		   if(((mouse_clicked_x >= fly_pos_x) && (mouse_clicked_x <= (fly_pos_x + fly_size_x)))
				  && ((mouse_clicked_y >= fly_pos_y) && (mouse_clicked_y <= (fly_pos_y + fly_size_y)))) {
			   setRandomPosition();
			   addScore(playerName);
			   System.out.println("Fly is hunted.");
			   return true;
		   }
		   else{
			   System.out.println("Fly is not hunted.");
			   return false;   
		   }	   
		   		   
	   }

	@Override
	public void exit() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void setRandomPosition(){		   
		   
		   Random randomGenerator = new Random();		   
		   int x = randomGenerator.nextInt(WIDTH-fly_size_x);
		   int y = randomGenerator.nextInt(HEIGHT-fly_size_y);
		   
		   fly_pos_x = x;
		   fly_pos_y = y;
	   }
	   

}
