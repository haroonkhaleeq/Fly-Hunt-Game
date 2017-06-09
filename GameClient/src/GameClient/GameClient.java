package GameClient;

import GameInterface2.Constant;
import GameInterface2.JustInterface;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;

import java.awt.event.*;
import java.net.URL;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class GameClient{
	
	final static int WIDTH = 750;
	final static int HEIGHT = 650;
	
	private static BufferedImage image;
	
	
   static JFrame frame;
   static Canvas canvas;
   static BufferStrategy bufferStrategy;
   static Graphics2D g;
	
	static int mouse_clicked_x=0;
	static int mouse_clicked_y=0;
	static int score=0;
	static JustInterface remote;
	//static String players_score;
	static String playerName;
	static ConcurrentHashMap<String, String> players_score = new ConcurrentHashMap<String, String>();
	
		
	public static void main (String[] args) throws RemoteException, NotBoundException {
		
		if(args.length == 0){
			System.out.println("No player found!");
			System.exit(0);
		}
			
					
		playerName = args[0];
		Registry registry = LocateRegistry.getRegistry("localhost",Constant.RMI_PORT);
		remote = (JustInterface) registry.lookup(Constant.RMI_ID);
		remote.login(playerName);
				
		
		class MouseControl implements MouseListener {
		    public void mousePressed(MouseEvent e) {}
		    public void mouseReleased(MouseEvent e) {}
		    public void mouseEntered(MouseEvent e) {}
		    public void mouseExited(MouseEvent e) {}
		    
		    public void mouseClicked(MouseEvent e) {
		    	int mouse_clicked_x = e.getX();
		        int mouse_clicked_y = e.getY();
		        try {
		        	render();
		        	if(remote.checkFlyIsHunted(mouse_clicked_x, mouse_clicked_y, playerName)) {
						render();
						//updateScore(g);
					}
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }		    
	   }
		 frame = new JFrame("Fly Hunting Game!");
		 
	      JPanel panel = (JPanel) frame.getContentPane();
	      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	      panel.setLayout(new FlowLayout());
	      
	      try {                
	          image = ImageIO.read(new File("C:/Users/mano bili/workspace/IGameServer/src/fly1.png"));
	      } catch (IOException ex) {
	    	  System.out.println(ex);
	          // handle exception...
	      }
	      remote.setRandomPosition();
	      
	      canvas = new Canvas();
	      canvas.setBounds(0, 0, WIDTH, HEIGHT);
	      canvas.setIgnoreRepaint(false);
	      	      
	      panel.add(canvas);	      	      
	      canvas.addMouseListener(new MouseControl());
	      
	      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	      frame.pack();
	      frame.setResizable(false);
	      frame.setVisible(true);
	      
	      canvas.createBufferStrategy(2);
	      bufferStrategy = canvas.getBufferStrategy();
	      g = (Graphics2D) bufferStrategy.getDrawGraphics();
	      
	      Toolkit toolkit = Toolkit.getDefaultToolkit();
	      Image image2 = toolkit.getImage("C:/Users/mano bili/workspace/IGameServer/src/flap.png");
	      Cursor c = toolkit.createCustomCursor(image2 , new Point(panel.getX(), 
	    		  panel.getY()), "img");
	      panel.setCursor (c);     
	      
	      canvas.requestFocus();
	      
	      g.drawImage(image, remote.getX(), remote.getY(), null);
	      g.setFont(new Font("default", Font.BOLD, 16));
	      
	      String raw_string = remote.getAllScores();
	      raw_string = raw_string.substring(1, raw_string.length() - 1);
	      String[] parts = raw_string.split(",");	      
	     
	      String[] parts2;
	      for (int i = 0; i < parts.length; i += 1) {
	          parts2 = parts[i].split("=");
	          players_score.put(parts2[0], parts2[1]);
	      }

	      System.out.println(players_score.toString());
	      
	      g.drawString("Players Score ", 20, 15);
	      Iterator it = players_score.entrySet().iterator();
		   int y = 35;
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        g.drawString(pair.getKey() + ": " + pair.getValue(), 20, y);
		        //System.out.println(pair.getKey() + " = " + pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		        y = y + 15;
		    }
	      
	      //g.drawString(playerName + ": " + remote.getScore(playerName), 15, 15);
	      g.dispose();
	      bufferStrategy.show();
	      
	      frame.addWindowListener(new WindowListener() {
	            //I skipped unused callbacks for readability

	            @Override
	            public void windowClosing(WindowEvent e) {
	                if(JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout ?") == JOptionPane.OK_OPTION){
	                    frame.setVisible(false);
	                    frame.dispose();
	                    
	                    try {
							remote.logout(playerName);
							players_score.remove(playerName);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                }
	            }

				@Override
				public void windowActivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					try {
						
						render();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void windowClosed(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeactivated(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowDeiconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowIconified(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void windowOpened(WindowEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	        });
	}
	
	
	public static void render() throws RemoteException {
	     System.out.println("here");
		  g = (Graphics2D) bufferStrategy.getDrawGraphics();
		  g.clearRect(0, 0, WIDTH, HEIGHT);
	      g.drawImage(image, remote.getX(), remote.getY(), null);
	      g.setFont(new Font("default", Font.BOLD, 16));
	      
	      String raw_string = remote.getAllScores();
	      raw_string = raw_string.substring(1, raw_string.length() - 1);
	      String[] parts = raw_string.split(",");	      
	     
	      String[] parts2;
	      for (int i = 0; i < parts.length; i += 1) {
	          parts2 = parts[i].split("=");
	          players_score.put(parts2[0], parts2[1]);
	      }

	      System.out.println(players_score.toString());
	      
	      g.drawString("Players Score ", 20, 15);
	      Iterator it = players_score.entrySet().iterator();
		   int y = 35;
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        g.drawString(pair.getKey() + ": " + pair.getValue(), 20, y);
		        //System.out.println(pair.getKey() + " = " + pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		        y = y + 15;
		    }
	      
	      //g.drawString(playerName + ": " + remote.getScore(playerName), 15, 15);
	      
	      g.dispose();
	      bufferStrategy.show();
		
	   }   
	   
	      
	

}
