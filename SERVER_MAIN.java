import javax.swing.JFrame;
public class SERVER_MAIN {
	public static void main(String[] args){
		Server nik = new Server();
		nik.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nik.startRunning();
		}
}
