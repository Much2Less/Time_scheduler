import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewWindow implements ActionListener{
	JFrame frame = new JFrame(); 
	JLabel label = new JLabel("logged in");
	JButton back = new JButton("back");
	
	NewWindow(){
		
		label.setBounds(0,0,100,50);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		//frame.setVisible(true);
		frame.setResizable(false);
		frame.add(label);
		frame.add(back);
		frame.setTitle("Account");
		
		back.setFocusable(false);
		back.setBounds(100,100, 25, 25);
		back.addActionListener(this);
		
		String name = JOptionPane.showInputDialog("Userame: ");
		String email = JOptionPane.showInputDialog("email: ");
		
		String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
		//String regex1 = "[a-zA-Z0-9+_.-]+@admin.de";
		boolean result = email.matches(regex);
		if(result==true) {
			JOptionPane.showConfirmDialog(null, "logging..", "Login", JOptionPane.PLAIN_MESSAGE);
			frame.setVisible(true);
		} else {
			int ans = JOptionPane.showConfirmDialog(null, "check login data", "ERROR", JOptionPane.ERROR_MESSAGE);
			if(ans==0) {frame.dispose();
					NewWindow myWindow = new NewWindow();}
			else {
				frame.dispose();
				LaunchPage launchpage = new LaunchPage();
			}
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==back) {
			frame.dispose();
			LaunchPage Newlaunchpage = new LaunchPage();
		}
	}
}
