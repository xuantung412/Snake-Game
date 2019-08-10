import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Login extends JFrame implements ActionListener, Runnable
{

	//Create button, textField and label for UI
	JButton loginButton;
	JPanel panel;
	JLabel userNameLabel;
	JLabel passwordLabel;
	JTextField userNameTextBox;
	JPasswordField passwordTextBox;
	UserAccountManagement accountManagement;
	int maxPLayer;
	LoginManagement logMan;

	//Constructor
	Login(UserAccountManagement accountManagement, int max, LoginManagement logMan)
	{
		this.logMan = logMan;
		this.maxPLayer = max;
		this.accountManagement = accountManagement;
		setTitle("Log In");
		setLayout(new BorderLayout());
		setSize(200,100);
		
		// Centering the Lobby Window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((screenSize.getWidth() - getWidth()) / 2);
	    int y = (int) ((screenSize.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	}

	public void actionPerformed(ActionEvent ae)
	{
		String userName=userNameTextBox.getText();
		String password=passwordTextBox.getText();
		UserAccount newAccount = new UserAccount(userName, password);
		if (this.accountManagement.allowLogin(newAccount)) {
			JOptionPane.showMessageDialog(this,"Log In successful",
					"Hi, "+userName+"\n Welcome to Game",JOptionPane.ERROR_MESSAGE);
			this.dispose();
			if(logMan.playerCount == 0) {
				logMan.player1.setText(newAccount.getUserName());
				logMan.playerCount++;
				logMan.rewriteLobbyCounter();
			} else if(logMan.playerCount == 1) {
				logMan.player2.setText(newAccount.getUserName());
				logMan.playerCount++;
				logMan.rewriteLobbyCounter();
			} else if(logMan.playerCount == 2) {
				logMan.player3.setText(newAccount.getUserName());
				logMan.playerCount++;
				logMan.rewriteLobbyCounter();
			}  else if(logMan.playerCount == 3) {
				logMan.player4.setText(newAccount.getUserName());
				logMan.playerCount++;
				logMan.rewriteLobbyCounter();
			}
		}
		else{
			System.out.println("Invalid account.");
			JOptionPane.showMessageDialog(this,"Invalid account",
					"Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void run() {
		//Create Label UserName and TextField.
		this.userNameLabel = new JLabel();
		this.userNameLabel.setText("Username:");
		this.userNameTextBox = new JTextField(99);

		//Create Label Password and TextField.
		this.passwordLabel = new JLabel();
		this.passwordLabel.setText("Password:");
		this.passwordTextBox = new JPasswordField(15);

		loginButton=new JButton("Log In");

		panel=new JPanel(new GridLayout(3,1));
		panel.add(userNameLabel);
		panel.add(passwordLabel);
		panel.add(userNameTextBox);
		panel.add(passwordTextBox);
		panel.add(loginButton);
		add(panel,BorderLayout.CENTER);
		loginButton.addActionListener(this);
		setTitle("Log In to Server.");
		setVisible(true);
	}

}
