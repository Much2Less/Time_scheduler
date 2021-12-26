
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class newpage {
	
	//aJFrame frame = new JFrame();
	
	newpage(){
			
		String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
		//String regex1 = "[a-zA-Z0-9+_.-]+@admin.de";	
		
		String name = JOptionPane.showInputDialog("Userame: ");
		
		String email = JOptionPane.showInputDialog("email: ");
		boolean result = email.matches(regex);
		if(result==true) {
			JOptionPane.showConfirmDialog(null, "registered", "Register", JOptionPane.PLAIN_MESSAGE);
			LaunchPage launchpage = new LaunchPage();
		} else {
			int ans = JOptionPane.showConfirmDialog(null, "check login data", "EMAIL ERROR", JOptionPane.ERROR_MESSAGE);
				if(ans==0){ 
					newpage mypage = new newpage();}
				else {
					LaunchPage launchpage = new LaunchPage();
				}	
		}
		
		
	}
}
