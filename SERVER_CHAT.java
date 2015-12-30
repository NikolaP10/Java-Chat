import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SERVER_CHAT extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public SERVER_CHAT(){
		super("Server ChatSpace");
		
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
		add(userText, BorderLayout.NORTH);
		
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(400,300);
		setVisible(true);
	}
	
	public void startRunning(){
		try{
			server = new ServerSocket(6789,100);
			
			while(true){
				try{
					vezaSaNekim();
					parametriZaChat();
					Chatovanje();//prikaz poruka
				}
				catch(EOFException e){
					showMessage("\nServer ended the connection");
				}
				finally{
					closeCrap();
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void vezaSaNekim() throws IOException{
		showMessage("\nCekanje na konekciju..");
		connection = server.accept();
		showMessage("\nKonektovani ste sa "+connection.getInetAddress().getHostName());
	}
	
	public void parametriZaChat() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStrimovi su uspostavljeni");
	}
	
	public void Chatovanje() throws IOException{
		String message = "\nKonektovani ste";
		sendMessage(message);
		ableToType(true);
		
		do{
			try{
			message = (String) input.readObject();
			showMessage("\nCLIENT - "+message);
			}
			catch(ClassNotFoundException e){
				showMessage("\nMessage cant be shown");
			}
		}while(!message.equals("END"));
	}
	
	public void sendMessage(String message){
		try{
			output.writeObject(message);
			output.flush();
			showMessage("\nSERVER - "+message);
		}
		catch(IOException e){
			chatWindow.append("\nCan't send message");
		}
	}
	
	public void closeCrap(){
		showMessage("\nClosing the connection");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
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
