package GameInterface2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

//import com.client.IGameClient;

public interface JustInterface extends Remote {
	public boolean login (String playerName) throws RemoteException;
	public boolean logout(String playerName) throws RemoteException;
	public boolean checkFlyIsHunted(int mouse_clicked_x, int mouse_clicked_y, String playerName) throws RemoteException;
	public void setRandomPosition() throws RemoteException;
	public void addScore(String player_name) throws RemoteException;
	public int getScore(String player_name) throws RemoteException;
	public String getAllScores() throws RemoteException;
	public int getX() throws RemoteException;
	public int getY() throws RemoteException;
	public void exit() throws RemoteException;
}
