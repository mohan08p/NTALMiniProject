
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.naming.directory.SearchResult;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.omg.PortableInterceptor.DISCARDING;

import java.net.*;
import java.util.*;
public class Client
{
    

  public static void main(String[] args) throws Exception {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   
	 SwingUtilities.invokeLater(new Runnable() {
         @Override 
         public void run() {
             final ClientGUI gui = new ClientGUI();
             
         }
     });

    
   // client.initializeClient();
    
         //wait for user action
  //  client.searchServer();
    
     
 
   // System.out.println("1");
    
   // client.connectToServer();
   // System.out.println("2");
  //  client.startService();
  
}}

class ClientMachine
{
	
	String name;                 //general information
	InetAddress ip;
	int  detected;
    InetAddress group; 
    int port ;
    
    int detectedServerCount;
    String serverName;           //server information 
    InetAddress serverIp;
    int serverPort;
    
    Socket clientSocket;          //socket information for tcp-ip
	PrintWriter sendToServer;
	static File[] oldListRoot;
	ClientGUI gui;
	
	void initializeClient(String name) 
	{
		
		
		try{
		this.name = name;
		this.ip = InetAddress.getLocalHost();
		this.detected = 0;
		this.group = InetAddress.getByName("237.0.0.1");
		this.port = 9000;
		System.out.println(this.name);
		}
		catch(Exception e){}
		
	}
	
	void searchServer()
	{
		
		
		try
		{
		  MulticastSocket socket = new MulticastSocket(port);
          socket.setInterface(InetAddress.getLocalHost());
          socket.joinGroup(group);

          DatagramPacket packet = new DatagramPacket(new byte[100], 100);
         
          {System.out.println("*");
              socket.receive(packet);
            
              serverName = new String( packet.getData(), 0,
                      packet.getLength() );
  
              System.out.println("Found " + serverName);
              
              serverIp = packet.getAddress();
             
              System.out.println("IP "+ serverIp);
              
              serverPort = packet.getPort();
              
              System.out.println("Port"+ serverPort);
              
              detectedServerCount++;
              
              gui.addServer(serverName, serverIp, serverPort);
              
              
              
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
		
}
	
	
	
	
	
	void connectToServer()throws Exception
	{
		
		 clientSocket = new Socket(serverIp,serverPort);
		 sendToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		 sendToServer.println(this.name);
		 BufferedReader readFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 System.out.println("reply from server"+readFromServer.readLine());
		 System.out.println("23");
		 return ;
			 
	}
	
	void startService()
	{
		  oldListRoot = File.listRoots();
		
		
		
		new Thread(new Runnable(){
			
			public void run(){
			  while (true) {
	                
	                   
	                
	                try{
	                if (File.listRoots().length > oldListRoot.length) {
	                	 sendToServer.println("inserted");
	                    oldListRoot = File.listRoots();
	                    detected++;

	                } else if (File.listRoots().length < oldListRoot.length) {
	                	 sendToServer.println("removed");

	                    oldListRoot = File.listRoots();
	                    detected--;
	                }
	                else
	                {    System.out.println("**");
	                	 Thread.sleep(1000);
	                	 if(clientSocket.isClosed())
	                		 break;
	                }
	                }
	                catch(Exception e)
	                {}

	            }
			}
	    }).start();
	    
			
			
			
		}
	
	
	void getServerSignal()
	{
		 
		 
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			try{
				BufferedReader readFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String signal = readFromServer.readLine();
			
				if(signal.equals("close"))
				{
					gui.serverStoppedAlert();
				}
			}
			catch(Exception e){}
				
			}
		}).start();
		 
		
		
	}
	
	void disconnectClient()
	{try{
		
		this.clientSocket.close();
		this.serverIp=null;
		this.serverName=null;
		this.port=0;
	}
	catch(Exception e){}
		
		
		
	}
		
}


 class ClientGUI {
	 
	 JFrame startupFrame, searchResultsFrame ; JPanel searchResultsPanel ;
	 ClientMachine client ;
		JButton enterButton;
		 
	 
	 public ClientGUI()
	 {	 
		 client = new ClientMachine();
		 client.gui = this;
		 
		
		 
		 startupFrame = new JFrame ("Client");
		 JPanel startupPanel = new JPanel();
		 JLabel nameLabel = new JLabel("Enter name of this machine");
		 JTextField nameField = new JTextField("",20);
		 JButton searchButton = new JButton("SEARCH SERVER");
		 JLabel connectingLabel = new JLabel("searching for server"); 
		 enterButton = new JButton("Enter");
		
		 startupPanel.add(nameLabel);
		 startupPanel.add(nameField);
		 startupPanel.add(enterButton);
		 startupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 startupFrame.add(startupPanel);
		 startupFrame.pack();
		 startupFrame.setLocation(200,200);
		 startupFrame.setVisible(true);
		
		// searchResultsFrame = new JFrame("Client");	 
		 
		 enterButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String name = nameField.getText();
					client.initializeClient(name);
					client.detectedServerCount=0;
					startupPanel.removeAll();
					startupPanel.add(searchButton);
					startupFrame.revalidate();
					startupFrame.repaint();
				searchResultsFrame = new JFrame("Client");
					
				}
			});
			 
			 searchButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
						startupPanel.removeAll();
						 
						startupPanel.add(connectingLabel);
						//startupFrame.pack();
						startupFrame.revalidate();
						
						startupFrame.repaint();
						initializeSearchPanel();
						
						SwingWorker aWorker = new SwingWorker() 
						{

							@Override
							protected Object doInBackground() throws Exception {
								// TODO Auto-generated method stub
									
								client.searchServer();
								return null;
							}
					
						@Override
						protected void done(){
							
							startupFrame.setVisible(false);
							startupFrame.dispose();
							
							searchResultsFrame.setVisible(true);
							
						}
						};
						
					aWorker.execute();
					
						
					}
				});
			 startupFrame.addWindowListener(new WindowAdapter() {

		            public void windowClosing(WindowEvent x) {
		            	System.exit(1);
		            	}
		            });
		

        
		 //searchResultsFrame.pack();
		// searchResultsFrame.setLocation(200,200);
		 
		
		 
		 
		 
	 }
	 
	 void initializeSearchPanel()
	 {
		 
		 
		 searchResultsPanel = new JPanel();
		 searchResultsPanel.setLayout(new GridBagLayout());
		
		 int gridwidth = 1;
			int gridheight = 1;
			Insets insets = new Insets(10,10,0,10);
			int anchor = GridBagConstraints.LINE_START;
			int fill = GridBagConstraints.HORIZONTAL;
			
		 JLabel name = new JLabel("Name");
		 JLabel add = new JLabel("Address");
		 JLabel port = new JLabel("Port");
			
			GridBagConstraints gbc = new GridBagConstraints(0,0,
	                1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
		searchResultsPanel.add(name,gbc);
		 gbc = new GridBagConstraints(1, 0,
                1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
	searchResultsPanel.add(add,gbc);
	 gbc = new GridBagConstraints(2, 0,
            1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
searchResultsPanel.add(port,gbc);
		
	    searchResultsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    searchResultsFrame.addWindowListener(new WindowAdapter() {
        
            public void windowClosing(WindowEvent x) {
            	try{
            	PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(),true);
        		out.println("close");
            	}catch(Exception e){}
        		client.disconnectClient();
                System.exit(1);
            }
        });
		 
		 
		 
		 searchResultsFrame.revalidate();
		 searchResultsFrame.repaint();
		 
		 
		 
		 
	 }
	 
	
	    void addServer(String name , InetAddress ip , int port)
	 {
		//JPanel panel = (JPanel)searchResultsFrame.getContentPane(); 
		 
		//JPanel serverPanel = new JPanel();
		//serverPanel.setLayout(new GridBagLayout());
		JLabel  namefield = new JLabel(name );
		JLabel ipfield = new JLabel(ip.toString().substring(1));
		JLabel portfield = new JLabel(String.valueOf(port));
		JButton connectButton = new JButton("Connect");
		
		int gridwidth = 1;
		int gridheight = 1;
		Insets insets = new Insets(10,10,0,10);
		int anchor = GridBagConstraints.LINE_START;
		int fill = GridBagConstraints.HORIZONTAL;
		
		
		
		 GridBagConstraints gbc = new GridBagConstraints(0, client.detectedServerCount,
	                1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
		 searchResultsPanel.add(namefield,gbc);
		 
	     gbc = new GridBagConstraints(1, client.detectedServerCount,
                gridwidth, gridheight, 1.0D, 1.0D, anchor, fill, insets, 0, 0);
	     searchResultsPanel.add(ipfield,gbc);
	     
	     gbc = new GridBagConstraints(2, client.detectedServerCount,
                gridwidth, gridheight, 1.0D, 1.0D, anchor, fill, insets, 0, 0);
	     searchResultsPanel.add(portfield,gbc);
	     
	     gbc = new GridBagConstraints(3, client.detectedServerCount,
                gridwidth, gridheight, 1.0D, 1.0D, anchor, fill, insets, 0, 0);
	     searchResultsPanel.add(connectButton,gbc);
	     searchResultsFrame.add(searchResultsPanel);
	   // System.out.println("4");
	    searchResultsFrame.revalidate();
	    searchResultsFrame.repaint();
	   searchResultsFrame.pack();
	    searchResultsFrame.setLocation(200,200);
	 
		 connectButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					searchResultsPanel.removeAll();
					JLabel connecting = new JLabel("Connecting");
					
				
					searchResultsPanel.add(connecting);
					
					searchResultsFrame.revalidate();
					searchResultsFrame.repaint();
					searchResultsFrame.pack();
				    searchResultsFrame.setLocation(200,200);
					
					//	System.out.println("1");
					
					try{	
					client.connectToServer();
					
				}
				catch(Exception e){}
					
					System.out.println("1");
					
					client.gui.startService();
					
				}
			});
	 }			
		void startService()
		{
					searchResultsPanel.removeAll();
					searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel,BoxLayout.Y_AXIS));
					JLabel connected = new JLabel("connected to "+client.serverName);
					
					
					searchResultsPanel.add(connected);
					JButton disconnectButton = new JButton("Disconnect");
					searchResultsPanel.add(disconnectButton);
					searchResultsFrame.revalidate();
					searchResultsFrame.repaint();
					searchResultsFrame.pack();
					searchResultsFrame.setLocation(200,200);
			        client.startService();
		            client.getServerSignal();
		
			
	 
	   
		 disconnectButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					try{
						
						PrintWriter out = new PrintWriter(client.clientSocket.getOutputStream(),true);
						out.println("close");
					client.disconnectClient();
					searchResultsPanel.removeAll();
					searchResultsFrame.setVisible(false);
					searchResultsFrame.dispose();
                      startupFrame.setVisible(true);	
                      client.gui.enterButton.doClick();
					}
					catch(Exception e){}
				}
			});
	   }
		
 
		void serverStoppedAlert()
		{
			
			client.disconnectClient();
			
			JFrame alertFrame = new JFrame("Alert");
			
			JPanel alertPanel = new JPanel();
			
			JLabel  alertText= new JLabel("Server has been stopped");
			
			JButton alertButton = new JButton("OK !");
			
			alertPanel.setLayout(new BoxLayout(alertPanel,BoxLayout.Y_AXIS));
			
			alertPanel.add(alertText);
			alertPanel.add(alertButton);
			alertFrame.add(alertPanel);
			alertFrame.setVisible(true);
			alertFrame.pack();
			alertFrame.setLocation(200,200);
			alertFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			 alertFrame.addWindowListener(new WindowAdapter() {

		            public void windowClosing(WindowEvent x) {
		            
		            	searchResultsPanel.removeAll();
						searchResultsFrame.setVisible(false);
					searchResultsFrame.dispose();
						startupFrame.setVisible(true);	
	                     alertFrame.setVisible(false);
	                     alertFrame.dispose();
	                      client.gui.enterButton.doClick();
		            }
		            	
		        });
			
			alertButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				
					searchResultsPanel.removeAll();
					searchResultsFrame.setVisible(false);
					searchResultsFrame.dispose();
                      startupFrame.setVisible(true);	
                     alertFrame.setVisible(false);
                     alertFrame.dispose();
                      client.gui.enterButton.doClick();

				}
			});
			
		}
 }