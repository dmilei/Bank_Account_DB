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
import db.Currency;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.SwingConstants;

public class AccountCreateUpdate extends JFrame {
	private int selectedAccount;
	private Currency cur = new Currency();
	private BankAccount acc = new BankAccount();
	
	private JPanel contentPane;
	private JLabel lblHeader;
	private JLabel lblCode;
	private JLabel lblLname;
	private JLabel lblBalance;
	private JLabel lblFName;
	private JTextField textFieldCode;
	private JTextField textFieldLName;
	private JTextField textFieldBalance;
	private JTextField textFieldFName;
	private JButton btnAction;
	private JLabel lblError;
	private JLabel lblCodeError;
	private JLabel lblValueError;
	private JButton btnCancel;
	private JTextField textFieldPin;
	private JLabel labelErrorPin;
	
	private void fillForm() throws SQLException {
		if(selectedAccount == 0) {
			lblHeader.setText("Adding New Account");
			btnAction.setText("Add Account");
		}
		
		else {
			lblHeader.setText("Update Account");
			btnAction.setText("Update");
			ResultSet rs = acc.getAccData(selectedAccount);
			
			while(rs.next()) {
				textFieldFName.setText(rs.getString("fname"));
				textFieldLName.setText(rs.getString("lname"));
				textFieldBalance.setText(rs.getString("balance"));
				textFieldCode.setText(rs.getString("Currency_Code"));
				textFieldCode.setEditable(false);
				textFieldPin.setText("****");
				textFieldPin.setEditable(false);
			}
		}
	}
	
	public void newAccount(ActionEvent e) throws ClassNotFoundException, SQLException {
		
		String fname = textFieldFName.getText();
		String lname = textFieldLName.getText();
		String balance = textFieldBalance.getText();
		String code = textFieldCode.getText();
		String pin = textFieldPin.getText();
		int curID = cur.getCurrencyIdByCode(code);
		
		if(textFieldCode.getText().isEmpty() || textFieldFName.getText().isEmpty() 
				|| textFieldLName.getText().isEmpty() || textFieldBalance.getText().isEmpty() || textFieldPin.getText().isEmpty()) {
			lblError.setText("Please fill all text field to add a new currency!");
		}
		else {
			if(!isNum(balance) || Integer.parseInt(balance)<1){
				JOptionPane.showMessageDialog(null, "The balance must be a positive number!");
			}
			else if(!isCodeOk(code)){
				JOptionPane.showMessageDialog(null, "The code must be made of 3 Capital Letters!");
			}
			else if(isNum(pin) == false || pin.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN must be made of exactly 4 digits!");
			}
			else if(curID == 0) {
				JOptionPane.showMessageDialog(null, "There isn't any currency found with this code in our database! Please choose an exisiting currency!");
			}
			
			else {
				int action = JOptionPane.showConfirmDialog(null, "Do you want to add this new account?", "Add new account", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					BankAccount newAccount = new BankAccount(fname, lname, pin, curID, Integer.parseInt(balance));
					acc.addAccount(newAccount);
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
			}
		}
	}
	
	public void updateAccount(ActionEvent e) throws ClassNotFoundException, SQLException {
		String fname = textFieldFName.getText();
		String lname = textFieldLName.getText();
		String balance = textFieldBalance.getText();
		
		if(textFieldFName.getText().isEmpty() || textFieldLName.getText().isEmpty() || textFieldBalance.getText().isEmpty()) {
			lblError.setText("Please fill all text field to update the currency!");
		}
		else {
			if(!isNum(balance) || Integer.parseInt(balance)<1){
				JOptionPane.showMessageDialog(null, "The balance must must be a positive number!");
			}
			else {
				int action = JOptionPane.showConfirmDialog(null, "Do you want to update the selected account?", "Update account", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					acc.updateAccount(selectedAccount, fname, lname, balance);
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
			}
		}
	}
	
	
	
	private static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}
	
	private boolean isCodeOk(String code) {
		if(code.length()==3 && Character.isUpperCase(code.charAt(0)) && Character.isUpperCase(code.charAt(1)) && Character.isUpperCase(code.charAt(2))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public AccountCreateUpdate(int accountId) throws SQLException {
		selectedAccount =accountId;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 373);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCurimg = new JLabel("");
		lblCurimg.setBounds(296, 11, 128, 128);
		Image img = new ImageIcon(this.getClass().getResource("/add-new-icon.png")).getImage();
		lblCurimg.setIcon(new ImageIcon(img));
		contentPane.add(lblCurimg);
		
		lblHeader = new JLabel("");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(92, 0, 226, 33);
		contentPane.add(lblHeader);
		
		lblCode = new JLabel("Currency Code:");
		lblCode.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCode.setBounds(10, 185, 105, 14);
		contentPane.add(lblCode);
		
		lblLname = new JLabel("Last Name:");
		lblLname.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblLname.setBounds(10, 95, 105, 14);
		contentPane.add(lblLname);
		
		lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBalance.setBounds(10, 140, 105, 14);
		contentPane.add(lblBalance);
		
		lblFName = new JLabel("First Name:");
		lblFName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFName.setBounds(10, 46, 105, 14);
		contentPane.add(lblFName);
		
		textFieldCode = new JTextField();
		textFieldCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
			@Override
			public void keyReleased(KeyEvent e) {
				String code = textFieldCode.getText();
				if(code.length()>2 && isCodeOk(code)==false) {
					lblCodeError.setText("The code must be made of 3 Capital Letters!");
					
				}
				
				else {
					lblCodeError.setText("");
				}
			}
		});
		textFieldCode.setBounds(125, 183, 161, 20);
		contentPane.add(textFieldCode);
		textFieldCode.setColumns(10);
		
		textFieldLName = new JTextField();
		textFieldLName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
		});
		textFieldLName.setColumns(10);
		textFieldLName.setBounds(125, 93, 161, 20);
		contentPane.add(textFieldLName);
		
		textFieldBalance = new JTextField();
		textFieldBalance.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
			@Override
			public void keyReleased(KeyEvent e) {
				String value = textFieldBalance.getText();
				if(isNum(value) == false && textFieldBalance.getText().isEmpty()==false) {
					lblValueError.setText("Please enter a positive number!");
				}
				else if(isNum(value) == true && Integer.parseInt(value)<1) {
					lblValueError.setText("Please enter a positive number!");
				}
				else {
					lblValueError.setText("");
				}
			}
		});
		textFieldBalance.setColumns(10);
		textFieldBalance.setBounds(125, 138, 161, 20);
		contentPane.add(textFieldBalance);
		
		textFieldFName = new JTextField();
		textFieldFName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
		});
		textFieldFName.setColumns(10);
		textFieldFName.setBounds(125, 44, 161, 20);
		contentPane.add(textFieldFName);
		
		btnAction = new JButton("");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedAccount == 0) {
					try {
						newAccount(e);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				else {
					try {
						updateAccount(e);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnAction.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAction.setBounds(175, 301, 111, 23);
		contentPane.add(btnAction);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblError.setBounds(10, 276, 276, 14);
		contentPane.add(lblError);
		
		lblCodeError = new JLabel("");
		lblCodeError.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCodeError.setForeground(Color.RED);
		lblCodeError.setBounds(30, 209, 256, 14);
		contentPane.add(lblCodeError);
		
		lblValueError = new JLabel("");
		lblValueError.setHorizontalAlignment(SwingConstants.RIGHT);
		lblValueError.setForeground(Color.RED);
		lblValueError.setBounds(71, 160, 215, 14);
		contentPane.add(lblValueError);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancel.setBounds(10, 301, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel labelPin = new JLabel("PIN Code:");
		labelPin.setFont(new Font("Tahoma", Font.BOLD, 12));
		labelPin.setBounds(10, 227, 105, 14);
		contentPane.add(labelPin);
		
		textFieldPin = new JTextField();
		textFieldPin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String PIN = textFieldPin.getText();
				if((isNum(PIN) == true && PIN.length()==4) || PIN.length()<4) {
					labelErrorPin.setText("");
					
				}
				else {
					labelErrorPin.setText("PIN Must be made of exactly 4 digits.");
				}
			}
		});
		textFieldPin.setColumns(10);
		textFieldPin.setBounds(125, 225, 161, 20);
		contentPane.add(textFieldPin);
		
		labelErrorPin = new JLabel("");
		labelErrorPin.setHorizontalAlignment(SwingConstants.RIGHT);
		labelErrorPin.setForeground(Color.RED);
		labelErrorPin.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelErrorPin.setBounds(10, 251, 276, 14);
		contentPane.add(labelErrorPin);
		
		fillForm();
	}
}