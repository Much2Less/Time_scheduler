package testlogin;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton login =  new JButton("login");
	JButton register =  new JButton("register");
	
	public LaunchPage(){
		
		login.setBounds(100, 160, 200, 40);
		login.setFocusable(false);
		login.addActionListener(this);
		
		register.setBounds(100, 120, 200, 40);
		register.setFocusable(false);
		register.addActionListener(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.add(login);
		frame.add(register);
		frame.setResizable(false);
		frame.setTitle("User");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==login) {
			frame.dispose();
			NewWindow myWindow = new NewWindow();
		}else if(e.getSource()==register) {
			frame.dispose();
			newpage mypage = new newpage();
		}
	}
}
