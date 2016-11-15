import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {


   

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub


		 SwingUtilities.invokeLater(new Runnable() {
	         @Override 
	         public void run() {
	             final ServerGUI gui = new ServerGUI();
	             
	         }
	     });
		
		
		
		
		
		ServerMachine server = new ServerMachine();
		
		server.initializeServer();
		
		server.startServer();
		
		server.acceptConnections();
	}}


class ServerMachine{
	
	InetAddress ip;
	String name;
	int clientCount;
	InetAddress group;
	int port;
	
	ServerSocket serverSocket;
	HashMap  <Socket,String> clientSocketMap;
	
	ServerGUI gui;
	
	void initializeServer(String name) 
	{
		
	
		try{
		this.ip = InetAddress.getLocalHost(); 
		this.name = name;
	    

		System.out.println(this.name);
		this.clientCount = 0;
		this.group = InetAddress.getByName("237.0.0.1");
		this.port = 9000;
		this.clientSocketMap = new HashMap<Socket,String>();
		}
		catch(Exception e){}
		
	}
	
	void startServer()throws IOException
	{
		
		 serverSocket = new ServerSocket(this.port);
		
		final byte[] bt = this.name.getBytes();
		 new Thread(new Runnable() {
		        @Override
		        public void run() {
		            try {
		                MulticastSocket socket = new MulticastSocket(port);
		                socket.setInterface(InetAddress.getLocalHost());
		                socket.joinGroup(group);

		                
		                byte index = 0;
		                while(true) {
		                   // System.out.println("svr broadcast");
		                    socket.send(new DatagramPacket(bt, bt.length, group, port));
		                   
		                    Thread.sleep(1*1000);
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		    }).start();
		
	}
	
	
	void acceptConnections() {
		
		 new Thread(new Runnable(){
			 
			 public void run(){
			 
				 
				 try
				 {
			 
					 while(true)
					 {System.out.println("&");
					 Socket clientSocket =  serverSocket.accept();
				
				clientCount++;
			 BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		   
			 String name = br.readLine();
			 System.out.println(name+" connected");
			 
			 PrintWriter out = new PrintWriter (clientSocket.getOutputStream(),true);
			 out.println("accepted");
			 gui.addClient(clientSocket,name,clientSocket.getInetAddress(),clientSocket.getPort());
			 clientSocketMap.put(clientSocket,name);
			 startDetecting(clientSocket);
			 
			 Thread.sleep(1000);
			 
			 }}
				 catch (Exception e)
				 {}
				 
			 } 
		 }).start();
		
		 
		
		
	
	}
	
	void startDetecting(Socket clientSocket)
	{
		
		new Thread(new Runnable(){
			
			public void run()
			{
				
				while (true)
				{
					try{
					BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					System.out.println("123");
					String status = br.readLine();
					if(status.equals("close"))
						{System.out.println(clientSocketMap.get(clientSocket)+" disconnected");
						  clientSocketMap.remove(clientSocket);
						  gui.removeClient(clientSocket);
						  clientCount--;
						  clientSocket.close(); 
						  
						  break;
						}
					else if(status.equals("inserted"))
					{
					System.out.println(clientSocketMap.get(clientSocket)+" "+status);
					gui.update(clientSocket,1);
					}
					else
					{
						System.out.println(clientSocketMap.get(clientSocket)+" "+status);
						gui.update(clientSocket,-1);
					}
					
					System.out.println("3425");
					Thread.sleep(1000);}
					
					catch(Exception e){}
					
				}
				
				
				
				
			}
			
			
			
		}).start();
		
		
		
		
	}
	
	void stopServer()
	{
		for(Map.Entry<Socket, String> mapEntry : clientSocketMap.entrySet())
		{
			Socket clientSocket = mapEntry.getKey();
			try{
			PrintWriter sendToClient = new PrintWriter(clientSocket.getOutputStream(),true);
			
			
			
			sendToClient.println("close");
			}
			
			catch(Exception e){}
		}
		
		
	}
	
}

 class ServerGUI
{
	 JFrame startupFrame, clientConnectionsFrame; JPanel clientListPanel;
	 HashMap  <String,Integer> rowMap; //maps clientname with row no. in gridbaglayout 
	 HashMap <Socket, String> clientMap ; //maps clientSocket with clientName
	 ArrayList  <Component[]> compGrid  ; //stroring the components
	 JPanel serverLogPanel ; JTextArea serverLog ;
	JButton enterButton ;
	ServerMachine server;
	DateFormat dateFormat ;
	
	public ServerGUI()
	{   
		
		server = new ServerMachine();
		server.gui = this;
		
		startupFrame = new JFrame ("Server");
		 dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 JPanel startupPanel = new JPanel();
		 JLabel nameField = new JLabel("Enter name of this pc");
		 JTextField nameField2 = new JTextField("",20);
		 JButton startButton = new JButton("START SERVER");
		  enterButton = new JButton("Enter");
		
		 startupPanel.add(nameField);
		 startupPanel.add(nameField2);
		 startupPanel.add(enterButton);
		 startupFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	     startupFrame.addWindowListener(new WindowAdapter() {
	    	 
	    	 public void windowClosing(WindowEvent e)
	    	 {
	    		 
	    		 startupFrame.setVisible(false);
	    		 startupFrame.dispose();
	    		 System.exit(1);
	    		 
	    		 
	    	 }
		});
		 startupFrame.add(startupPanel);
		 startupFrame.pack();
		 startupFrame.setLocation(200,200);
		 startupFrame.setVisible(true);
		 
		 enterButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
					
					
					String name = nameField2.getText();
					server.initializeServer(name);
					
					startupPanel.removeAll();
					startupPanel.add(startButton);
					startupFrame.revalidate();
					startupFrame.repaint();
				
					 startButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								// TODO Auto-generated method stub
								
								clientMap = new HashMap <Socket,String>();
								rowMap = new HashMap <String,Integer>();
								compGrid = new ArrayList<Component[]>();
								
								startupFrame.setVisible(false);
								startupFrame.dispose();
								
								clientConnectionsFrame.setVisible(true);
								try{
								server.startServer();
								server.acceptConnections();
								}
								catch(Exception e){}
								
							}
						});
					
				}
			});
		 
		 
		 
		 clientConnectionsFrame = new JFrame("Server");
		  JPanel clientParentPanel = new JPanel();
			JTabbedPane tab = new JTabbedPane();
		 
			JPanel clientLeftPanel = new JPanel();
			   
			   clientLeftPanel.setLayout(new BoxLayout(clientLeftPanel, BoxLayout.Y_AXIS));
			
		 clientListPanel = new JPanel();
			 clientListPanel.setLayout(new GridBagLayout());
			 
			 int gridwidth = 1;
				int gridheight = 1;
				Insets insets = new Insets(10,10,0,10);
				int anchor = GridBagConstraints.LINE_START;
				int fill = GridBagConstraints.HORIZONTAL;
				
			 JLabel name = new JLabel("Client Name");
			 JLabel add = new JLabel("Address");
			
			 JLabel flashCount = new JLabel("Flash Drive Count");
				
				GridBagConstraints gbc = new GridBagConstraints(0,0,
		                1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
				clientListPanel.add(name,gbc);
			 gbc = new GridBagConstraints(1, 0,
	                1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
			 clientListPanel.add(add,gbc);
		 gbc = new GridBagConstraints(2, 0,
	            1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
		
			 clientListPanel.add(flashCount,gbc);

			 
			 
			 clientLeftPanel.add(clientListPanel) ;
			 
			    JPanel stopServerPanel = new JPanel();
				   JButton stopServerButton = new  JButton("Stop Server");
				   stopServerPanel.add(stopServerButton);
				  // clientConnectionsFrame.add(stopServerPanel);
				 clientLeftPanel.add(stopServerPanel);
				 
				 
				   
		serverLogPanel = new JPanel();
		
		serverLog = new JTextArea(dateFormat.format(new Date())+" : Server Started",3,25);
		
		serverLog.setEditable(false); // set textArea non-editable
	    JScrollPane scroll = new JScrollPane(serverLog);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

	    //Add Textarea in to middle panel
	    serverLogPanel.add(scroll);
		
		//serverLogPanel.add(serverLog);
	
			
		  // clientConnectionsFrame.add(clientListPanel);
		    clientConnectionsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		  
		    tab.addTab("Clients",clientLeftPanel);
		    tab.addTab("Log",serverLogPanel);
			
			 clientParentPanel.add(tab);
			 
			 
			 clientConnectionsFrame.add(clientParentPanel);
			   clientConnectionsFrame.pack();
		   clientConnectionsFrame.setLocation(200,200);
		
			 
	
		 
		  clientConnectionsFrame.addWindowListener(new WindowAdapter() {

	            public void windowClosing(WindowEvent x) {
	            	
	            	stopServerButton.doClick();
	            	
	            }
	        });
		 
		 stopServerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				JFrame alertFrame = new JFrame("Alert");
				
				JPanel alertPanel = new JPanel();
				alertPanel.setLayout(new BoxLayout(alertPanel,BoxLayout.Y_AXIS));
				JLabel  alertText= new JLabel("Do You Want To Stop Server?");
				alertText.setHorizontalAlignment(SwingConstants.LEFT);
				JPanel buttonPanel =new JPanel();
				//buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
				
				JButton alertButton1 = new JButton("Yes");
				JButton alertButton2 =new JButton("No");
				//alertText.setEnabled(false);
			
				
				alertPanel.add(alertText);
				
				buttonPanel.add(alertButton1);
				buttonPanel.add(alertButton2);
				
				alertPanel.add(buttonPanel);
				alertFrame.add(alertPanel);
				alertFrame.setVisible(true);
				alertFrame.pack();
				alertFrame.setLocation(200,200);
				alertFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
				
				alertButton1.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					  server.stopServer();
						clientConnectionsFrame.removeAll();
						clientConnectionsFrame.setVisible(false);
						clientConnectionsFrame.dispose();
	                     	
	                     alertFrame.setVisible(false);
	                     alertFrame.dispose();
	                     System.exit(1);

					}
				});
				

				alertButton2.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					
	                     	
	                     alertFrame.setVisible(false);
	                     alertFrame.dispose();
	                    
					}
				});
				
			}
		});
		 
		}
	
	void addClient(Socket clientSocket , String name , InetAddress ip , int port)
	{
		StringBuilder sb = new StringBuilder(name); 
		int i=1;
		while(rowMap.containsKey(sb.toString()))
		{
			sb = new StringBuilder (new StringBuilder(name).append("("+i+")"));
			i++;
			
			System.out.println(sb.toString());
		}
		
		rowMap.put(sb.toString(),server.clientCount);
		clientMap.put(clientSocket, sb.toString());
		
	
		 int gridwidth = 1;
			int gridheight = 1;
			Insets insets = new Insets(10,10,0,10);
			int anchor = GridBagConstraints.LINE_START;
			int fill = GridBagConstraints.HORIZONTAL;
			
		 JLabel clientName = new JLabel(sb.toString());
		 JLabel clientAdd = new JLabel(ip.toString().substring(1));
		
		 JLabel flashCount = new JLabel("0");
		
		 compGrid.add( new Component [] {clientName , clientAdd , flashCount}); //compGrid[i] stores the compList at row i+1 in grid  
		 
			GridBagConstraints gbc = new GridBagConstraints(0,server.clientCount,
	                1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
			clientListPanel.add(clientName,gbc);
		 gbc = new GridBagConstraints(1, server.clientCount,
             1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
		 clientListPanel.add(clientAdd,gbc);
	 gbc = new GridBagConstraints(2, server.clientCount,
         1, 1, 1.0D, 1.0D, anchor , fill ,insets , 0, 0);
	
		 clientListPanel.add(flashCount,gbc);

		 
	 //   clientListPanel.add(clientPanel);
	    
	   // System.out.println("4");
		   serverLog.setRows(serverLog.getRows()+2);
		   
	    clientConnectionsFrame.revalidate();
	    clientConnectionsFrame.repaint();
	   clientConnectionsFrame.pack();
		
	   serverLog.append("\n"+dateFormat.format(new Date())+" : "+sb.toString()+" connected !!");
       		
	
	}
	
	void removeClient(Socket clientSocket)
	{
		String removedClientName = clientMap.get(clientSocket);
		System.out.println(removedClientName +" removed");
		int rowNo = rowMap.get(removedClientName);
		System.out.println("row no "+rowNo);
		Component above[],curr[];
		curr = compGrid.get(rowNo-1);
	     for(int i=0;i<compGrid.size();i++)
	    	 {System.out.println(((JLabel)compGrid.get(i)[0]).getText()+" "+((JLabel)compGrid.get(i)[1]).getText()+" "+((JLabel)compGrid.get(i)[2]).getText());
		System.out.println();
	    	 }
	     System.out.println("**");
	    	 for(int i=rowNo ;i<server.clientCount ;i++)
		{
			 above = curr;
			 curr = compGrid.get(i);
			 System.out.println(i+"i ");
			   System.out.println("above");
				System.out.println(((JLabel)above[0]).getText()+" "+((JLabel)above[1]).getText()+" "+((JLabel)above[2]).getText());
					System.out.println("curr");
				       System.out.println(((JLabel)curr[0]).getText()+" "+((JLabel)curr[1]).getText()+" "+((JLabel)curr[2]).getText());
			
			((JLabel)above[0]).setText(((JLabel)curr[0]).getText());
			((JLabel)above[1]).setText(((JLabel)curr[1]).getText());
			((JLabel)above[2]).setText(((JLabel)curr[2]).getText());
			
		String currClientName = ((JLabel)curr[0]).getText();
		
		rowMap.replace(currClientName,i);
			
		}
		
		curr  = compGrid.get(server.clientCount-1);
		
		clientListPanel.remove(curr[0]);
		clientListPanel.remove(curr[1]);
		clientListPanel.remove(curr[2]);
		compGrid.remove(compGrid.size()-1);
		
		
		
	   rowMap.remove(removedClientName);
	   clientMap.remove(clientSocket);
	   serverLog.append("\n"+dateFormat.format(new Date())+" : "+removedClientName+" disconnected .");
		
	   serverLog.setRows(serverLog.getRows()-2);
	   clientConnectionsFrame.revalidate();
		clientConnectionsFrame.repaint();
		clientConnectionsFrame.pack();
	   
	}
	
	void update(Socket clientSocket , int action)
	{
		String clientName = clientMap.get(clientSocket);
		int rowNo = rowMap.get(clientName);
		
		JLabel updatedCountField = (JLabel)compGrid.get(rowNo-1)[2];
		updatedCountField.setText(String.valueOf(Integer.valueOf(updatedCountField.getText())+action));
		String status;
		if(action == 1 )
			 status = "inserted";
		else status ="removed";
	
		serverLog.append("\n"+dateFormat.format(new Date())+" : "+clientName+" ** Flash Drive "+status+" **");
		clientConnectionsFrame.revalidate();
		clientConnectionsFrame.repaint();
		clientConnectionsFrame.pack();
		
	}
	
	
}

