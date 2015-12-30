import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class CLIENT_CHAT extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverIP;
	private String message;
	private Socket connection;
	
	public CLIENT_CHAT(String host){
		super("Client Chat Window");
		serverIP = host;
		
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);
		setSize(400,300);
		setVisible(true);
	}
	
	public void Chat(){
		try{
			vezaSaNekim();
			parametriZaChat();
			Chatovanje();
		}
		catch(IOException e){
			showMessage("\nClient ended the connection");
		}
		finally{
			closeCrap();
		}
	}
	
	public void vezaSaNekim() throws IOException{
		showMessage("\nConnecting...");
		connection = new Socket (InetAddress.getByName(serverIP),6789);
		showMessage("\nYou are now connected with "+connection.getInetAddress().getHostName());
		
	}
	
	public void parametriZaChat() throws IOException{
		
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
		
			showMessage("\nStreams are setUp");
		
	}
	
	public void Chatovanje() throws IOException{
		ableToType(true);
		do{
		try{
			message = (String) input.readObject();
			showMessage("\nSERVER - "+message);
		}
		catch(ClassNotFoundException e){
			showMessage("\nMessage cant be shown");
		}
		}while(!message.equals("END"));
	}
	
	public void sendMessage(String message){
		
		try{
			output.writeObject(message);
			showMessage("\nCLIENT - "+message);
		}
		catch(IOException e){
			chatWindow.append("\nCant send");
		}
	}
	
	public void closeCrap(){
		showMessage("\nClosing down chat");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showMessage(final String text){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(text);
					}
				}
				);
	}
	
	public void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
				);
	}
}
