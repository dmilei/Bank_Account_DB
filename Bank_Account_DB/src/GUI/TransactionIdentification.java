package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import db.BankAccount;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class TransactionIdentification extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldRefNum;
	private JLabel lblRefnumerror;
	private JLabel labelPinError;
	private JPasswordField passwordField;
	private String selectedTransaction;
	
	private BankAccount acc = new BankAccount();
	private JButton buttonCancel;
	
	private static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}
	
	public void loginAuth(ActionEvent e) throws ClassNotFoundException, SQLException {
		
		String refNum = textFieldRefNum.getText();
		String pin = passwordField.getText();
		
		
		if(textFieldRefNum.getText().isEmpty() || passwordField.getText().isEmpty()) {
			labelPinError.setText("Please fill all field to login!");
		}
		else {
			if(isNum(refNum) == false || refNum.length()!=12) {
				JOptionPane.showMessageDialog(null, "The Account number is made of 12 digit!");
			}
			else if(isNum(pin) == false || pin.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN must be made of exactly 4 digits!");
			}
			else {
				int accId = acc.checkLogin(pin, refNum);
				if(accId>0) {
					JOptionPane.showMessageDialog(null, "Login Succesful!");
					if(selectedTransaction == "Deposit") {
						DepositWithdraw depoWindow = new DepositWithdraw(accId, refNum, "Deposit");
						depoWindow.setVisible(true);
						JComponent comp = (JComponent) e.getSource();
						Window win = SwingUtilities.getWindowAncestor(comp);
						win.dispose();
					}
					
					if(selectedTransaction == "Withdraw") {
						DepositWithdraw depoWindow = new DepositWithdraw(accId, refNum, "Withdraw");
						depoWindow.setVisible(true);
						JComponent comp = (JComponent) e.getSource();
						Window win = SwingUtilities.getWindowAncestor(comp);
						win.dispose();
					}
					
					if(selectedTransaction == "Transfer") {
						Transfer transferWindow = new Transfer(accId, refNum);
						transferWindow.setVisible(true);
						JComponent comp = (JComponent) e.getSource();
						Window win = SwingUtilities.getWindowAncestor(comp);
						win.dispose();
					}
					
					if(selectedTransaction == "PinChange") {
						PinChange pinWindow = new PinChange(accId, refNum);
						pinWindow.setVisible(true);
						JComponent comp = (JComponent) e.getSource();
						Window win = SwingUtilities.getWindowAncestor(comp);
						win.dispose();
					}
					
					if(selectedTransaction == "CurrencyChange") {
						CurrencyChange curWindow = new CurrencyChange(accId, refNum);
						curWindow.setVisible(true);
						JComponent comp = (JComponent) e.getSource();
						Window win = SwingUtilities.getWindowAncestor(comp);
						win.dispose();
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Login Failed!");
				}
			}
			
			
		}
	}
	/**
	 * Create the frame.
	 */
	public TransactionIdentification(String selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(260, 260, 438, 244);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIdImage = new JLabel("");
		lblIdImage.setBounds(286, 59, 126, 126);
		Image img = new ImageIcon(this.getClass().getResource("/identification.png")).getImage();
		lblIdImage.setIcon(new ImageIcon(img));
		contentPane.add(lblIdImage);
		
		JLabel lblIDLabel = new JLabel("Please enter your account number and PIN");
		lblIDLabel.setForeground(Color.BLUE);
		lblIDLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblIDLabel.setBounds(32, 12, 297, 35);
		contentPane.add(lblIDLabel);
		
		JLabel lblAccnum = new JLabel("Account Number:");
		lblAccnum.setForeground(Color.BLUE);
		lblAccnum.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAccnum.setBounds(10, 74, 117, 14);
		contentPane.add(lblAccnum);
		
		textFieldRefNum = new JTextField();
		textFieldRefNum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String RefNum = textFieldRefNum.getText();
				if((isNum(RefNum) == true && RefNum.length()==12) || RefNum.length()<12) {
					lblRefnumerror.setText("");
					
				}
				else {
					lblRefnumerror.setText("Account number is made of 12 digits!");
				}
			}
		});
		textFieldRefNum.setBounds(131, 72, 145, 20);
		contentPane.add(textFieldRefNum);
		textFieldRefNum.setColumns(10);
		
		JLabel labelPIN = new JLabel("PIN:");
		labelPIN.setForeground(Color.BLUE);
		labelPIN.setFont(new Font("Tahoma", Font.BOLD, 12));
		labelPIN.setBounds(10, 121, 117, 14);
		contentPane.add(labelPIN);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					loginAuth(e);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSubmit.setBounds(187, 172, 89, 23);
		contentPane.add(btnSubmit);
		
		lblRefnumerror = new JLabel("");
		lblRefnumerror.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRefnumerror.setForeground(Color.RED);
		lblRefnumerror.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRefnumerror.setBounds(52, 99, 223, 14);
		contentPane.add(lblRefnumerror);
		
		labelPinError = new JLabel("");
		labelPinError.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPinError.setForeground(Color.RED);
		labelPinError.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelPinError.setBounds(53, 146, 223, 14);
		contentPane.add(labelPinError);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String pin = passwordField.getText();
				if((isNum(pin) == true && pin.length()==4) || pin.length()<4) {
					labelPinError.setText("");
					
				}
				else {
					labelPinError.setText("PIN is made of 4 digits!");
				}
			}
		});
		passwordField.setBounds(131, 119, 145, 20);
		contentPane.add(passwordField);
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			}
		});
		buttonCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonCancel.setBounds(10, 172, 89, 23);
		contentPane.add(buttonCancel);
	}
}
