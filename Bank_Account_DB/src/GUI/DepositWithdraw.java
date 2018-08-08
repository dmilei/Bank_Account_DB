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
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DepositWithdraw extends JFrame {
	
	private BankAccount acc = new BankAccount();
	
	private int accId;
	private int balance;
	private String ref_num;
	private String selectedTransaction;
	
	private JPanel contentPane;
	private JLabel lblImage;
	private Image img = new ImageIcon(this.getClass().getResource("/deposit.png")).getImage();
	private Image img2 = new ImageIcon(this.getClass().getResource("/withdrawal.png")).getImage();
	private JLabel lblHeader;
	private JLabel lblAccount;
	private JTextField textFieldAccNum;
	private JTextField textFieldBalance;
	private JLabel lblBalance;
	private JTextField textFieldAmount;
	private JLabel labelAmount;
	private JLabel labelPin;
	private JButton btnDeposit;
	private JButton btnCancel;
	private JPasswordField passwordField;
	private JLabel labelPinError;
	private JLabel labelBalanceError;
	
	private static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}
	
	private void deposit(ActionEvent e) {
		String depoAmount = textFieldAmount.getText();
		String pin = passwordField.getText();
		
		if(textFieldAmount.getText().isEmpty() || passwordField.getText().isEmpty()) {
			labelPinError.setText("Fill all textfields!");
		} 
		else  {
			if(!isNum(depoAmount)) {
				JOptionPane.showMessageDialog(null, "Enter a positive number to deposit!");
				return;
			}
			if(isNum(depoAmount)) {
				if(Integer.parseInt(depoAmount) <1) {
					JOptionPane.showMessageDialog(null, "Enter a positive number to deposit!");
					return;
				}
			}
			if(!isNum(pin) || pin.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN is made of 4 digits!");
				return;
			}
			if(acc.checkLogin(pin, this.ref_num) >0) {
				int action = JOptionPane.showConfirmDialog(null, "Do you want deposit the chosen amount into this account?", "Deposit Money", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					acc.deposit(accId, Integer.parseInt(depoAmount));
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
					
			}
			else {
					JOptionPane.showMessageDialog(null, "Login failed! Wrong PIN!");
			}
			
			
		}
		
		
	}
	
	private void withdraw(ActionEvent e) {
		String withdrawAmount = textFieldAmount.getText();
		String pin = passwordField.getText();
		
		if(textFieldAmount.getText().isEmpty() || passwordField.getText().isEmpty()) {
			labelPinError.setText("Fill all textfields!");
		} 
		else  {
			if(!isNum(withdrawAmount)) {
				JOptionPane.showMessageDialog(null, "Enter a positive number to withdraw!");
				return;
			}
			if(isNum(withdrawAmount)) {
				if(Integer.parseInt(withdrawAmount) <1) {
					JOptionPane.showMessageDialog(null, "Enter a positive number to withdraw!");
					return;
				}
				else if(Integer.parseInt(withdrawAmount) > this.balance) {
					JOptionPane.showMessageDialog(null, "Can't withdraw more than your balance!");
					return;
				}
			}
			if(!isNum(pin) || pin.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN is made of 4 digits!");
				return;
			}
			if(acc.checkLogin(pin, this.ref_num) >0) {
				int action = JOptionPane.showConfirmDialog(null, "Do you want withdraw the chosen amount from this account?", "Withdraw Money", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					acc.withdraw(accId, Integer.parseInt(withdrawAmount));
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
					
					
			}
			else {
					JOptionPane.showMessageDialog(null, "Login failed! Wrong PIN!");
			}
			
			
		}
	}
	
	private void fillForm() {
		textFieldAccNum.setText(ref_num);
		textFieldAccNum.setEditable(false);
		textFieldBalance.setText(Integer.toString(balance));
		textFieldBalance.setEditable(false);
		
		if(selectedTransaction == "Deposit") {
			lblImage.setIcon(new ImageIcon(img));
			lblHeader.setText("Deposit to account");
			labelAmount.setText("Deposit amount:");
			btnDeposit.setText("Deposit");
		}
		
		else if(selectedTransaction == "Withdraw") {
			lblImage.setIcon(new ImageIcon(img2));
			lblHeader.setText("Withdraw from account");
			labelAmount.setText("Withdraw amount:");
			btnDeposit.setText("Withdraw");
		}
		
		
	}

	/**
	 * Create the frame.
	 */
	public DepositWithdraw(int accId, String ref_num, String selectedTransaction) {
		this.accId = accId;
		this.ref_num = ref_num;
		this.selectedTransaction = selectedTransaction;
		this.balance = acc.getAccountBalance(accId);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 250, 443, 311);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblImage = new JLabel("");
		lblImage.setBounds(299, 11, 128, 128);
		contentPane.add(lblImage);
		
		lblHeader = new JLabel("");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(37, 11, 212, 26);
		contentPane.add(lblHeader);
		
		lblAccount = new JLabel("Account Number:");
		lblAccount.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAccount.setBounds(10, 62, 111, 14);
		contentPane.add(lblAccount);
		
		textFieldAccNum = new JTextField();
		textFieldAccNum.setBounds(141, 60, 148, 20);
		contentPane.add(textFieldAccNum);
		textFieldAccNum.setColumns(10);
		
		textFieldBalance = new JTextField();
		textFieldBalance.setColumns(10);
		textFieldBalance.setBounds(141, 105, 148, 20);
		contentPane.add(textFieldBalance);
		
		lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBalance.setBounds(10, 107, 111, 14);
		contentPane.add(lblBalance);
		
		textFieldAmount = new JTextField();
		textFieldAmount.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				labelPinError.setText("");
			}
		});
		textFieldAmount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String amount = textFieldAmount.getText();
				if(isNum(amount) || amount.length()==0) {
					labelBalanceError.setText("");
					
				}
				else {
					labelBalanceError.setText("Enter a positive number to deposit!");
				}
			}
		});
		textFieldAmount.setColumns(10);
		textFieldAmount.setBounds(141, 150, 148, 20);
		contentPane.add(textFieldAmount);
		
		labelAmount = new JLabel("");
		labelAmount.setFont(new Font("Tahoma", Font.BOLD, 12));
		labelAmount.setBounds(10, 152, 121, 14);
		contentPane.add(labelAmount);
		
		labelPin = new JLabel("PIN:");
		labelPin.setFont(new Font("Tahoma", Font.BOLD, 12));
		labelPin.setBounds(10, 197, 111, 14);
		contentPane.add(labelPin);
		
		btnDeposit = new JButton("");
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedTransaction == "Deposit") {
					deposit(e);
				}
				else if(selectedTransaction == "Withdraw") {
					withdraw(e);
				}
			}
		});
		btnDeposit.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDeposit.setBounds(188, 239, 101, 23);
		contentPane.add(btnDeposit);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCancel.setBounds(10, 239, 89, 23);
		contentPane.add(btnCancel);
		
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				labelPinError.setText("");
			}
		});
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
		passwordField.setBounds(141, 197, 148, 20);
		contentPane.add(passwordField);
		
		labelPinError = new JLabel("");
		labelPinError.setHorizontalAlignment(SwingConstants.RIGHT);
		labelPinError.setForeground(Color.RED);
		labelPinError.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelPinError.setBounds(72, 222, 217, 14);
		contentPane.add(labelPinError);
		
		labelBalanceError = new JLabel("");
		labelBalanceError.setHorizontalAlignment(SwingConstants.RIGHT);
		labelBalanceError.setForeground(Color.RED);
		labelBalanceError.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelBalanceError.setBounds(72, 172, 217, 14);
		contentPane.add(labelBalanceError);
		
		fillForm();
	}

}
