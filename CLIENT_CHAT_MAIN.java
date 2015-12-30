import javax.swing.JFrame;

public class CLIENT_CHAT_MAIN {
	public static void main(String[] args){
		Client chat = new Client("127.0.0.1");
		chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat.Chat();
		}
}
