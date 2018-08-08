package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import db.BankAccount;
import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Transfer extends JFrame {
	private BankAccount acc = new BankAccount();
	private int accId;
	private String refNum;
	private int balance;
	
	private JPanel contentPane;
	private JTextField textFieldRefNum;
	private JTextField textFieldBalance;
	private JPasswordField pwdPin;
	private JTextField textFieldReceiverRefNum;
	private JTextField textFieldRecAccCurr;
	private JTextField textFieldRecName;
	private JTextField textFieldTransferAmt;
	private JLabel lblSenderName;
	private JLabel lblPinerror;
	private JLabel lblAmtError;
	
	private static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {

	        Double.parseDouble(strNum);

	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}
	
	private void fillForm() {
		textFieldRecName.setEditable(false);
		textFieldRecAccCurr.setEditable(false);
		
		textFieldRefNum.setText(refNum);
		textFieldRefNum.setEditable(false);
		
		ResultSet rs = acc.getAccData(accId);
		
		try {
			while(rs.next()) {
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");
				this.balance = rs.getInt("balance");
				
				lblSenderName.setText(fname + " " + lname);
				
				textFieldBalance.setText(Integer.toString(balance));
				textFieldBalance.setEditable(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void transfer(ActionEvent e) {
		String pin = pwdPin.getText();
		String recRefNum = textFieldReceiverRefNum.getText();
		String amount = textFieldTransferAmt.getText();
		int receiverId = acc.getAccIdByRefNum(recRefNum);
		
		if(pwdPin.getText().isEmpty() || textFieldReceiverRefNum.getText().isEmpty() || textFieldTransferAmt.getText().isEmpty()) {
			lblAmtError.setText("Fill all textfields!");
			return;
		} 
		else  {
			if(isNum(pin) != true || pin.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN Must be 4 digit!");
				return;
			}
			
			if(isNum(recRefNum) != true || recRefNum.length()!=12) {
				JOptionPane.showMessageDialog(null, "Reference Number is made of 12 digits!");
				return;
			}
			
			if(recRefNum.equals(this.refNum)) {
				JOptionPane.showMessageDialog(null, "You can't make a transfer to your own account!");
				return;
			}
			
			if(receiverId<1) {
				JOptionPane.showMessageDialog(null, "No account found with the given Reference Number.");
				return;
			}
			
			if(isNum(amount) != true || Integer.parseInt(amount)<1) {
				JOptionPane.showMessageDialog(null, "Please enter a positive number to transfer!");
				return;
			}
			
			if(Integer.parseInt(amount)> balance) {
				JOptionPane.showMessageDialog(null, "You can't transfer more then your balance!");
				return;
			}
			
			if(acc.checkLogin(pin, this.refNum) >0) {
				int action = JOptionPane.showConfirmDialog(null, "Do you want to transfer the selected amount to account with the Reference Number of: "+ recRefNum + "?", "Transfer Money", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					acc.transfer(accId, receiverId, Integer.parseInt(amount));
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
				
			}
			
			else {
				JOptionPane.showMessageDialog(null, "Transfer failed! PIN code is not correct!");
			}
			
		}
	}

	/**
	 * Create the frame.
	 */
	public Transfer(int accId, String refNum) {
		this.accId = accId;
		this.refNum = refNum;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 120, 440, 545);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelImg = new JLabel("");
		labelImg.setBounds(289, 11, 128, 128);
		Image img = new ImageIcon(this.getClass().getResource("/transfer1.png")).getImage();
		labelImg.setIcon(new ImageIcon(img));
		contentPane.add(labelImg);
		
		JLabel lblHeader = new JLabel("Transfer money");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(50, 11, 212, 26);
		contentPane.add(lblHeader);
		
		JLabel lblSenderRefNum = new JLabel("Reference Number:");
		lblSenderRefNum.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSenderRefNum.setBounds(10, 85, 128, 14);
		contentPane.add(lblSenderRefNum);
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBalance.setBounds(10, 135, 128, 14);
		contentPane.add(lblBalance);
		
		JLabel lblPin = new JLabel("PIN Number:");
		lblPin.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPin.setBounds(10, 185, 130, 14);
		contentPane.add(lblPin);
		
		textFieldRefNum = new JTextField();
		textFieldRefNum.setBounds(148, 83, 131, 20);
		contentPane.add(textFieldRefNum);
		textFieldRefNum.setColumns(10);
		
		textFieldBalance = new JTextField();
		textFieldBalance.setColumns(10);
		textFieldBalance.setBounds(148, 133, 131, 20);
		contentPane.add(textFieldBalance);
		
		pwdPin = new JPasswordField();
		pwdPin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String pin = pwdPin.getText();
				if((isNum(pin) == true && pin.length()==4) || pin.length()<4) {
					lblPinerror.setText("");
					
				}
				else {
					lblPinerror.setText("PIN is made of 4 digits!");
				}
			}
		});
		pwdPin.setBounds(150, 183, 128, 20);
		contentPane.add(pwdPin);
		
		lblSenderName = new JLabel("");
		lblSenderName.setHorizontalAlignment(SwingConstants.CENTER);
		lblSenderName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSenderName.setBounds(50, 47, 212, 14);
		contentPane.add(lblSenderName);
		
		JLabel lblRecInfo = new JLabel("Receiver Info");
		lblRecInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRecInfo.setBounds(50, 230, 212, 14);
		contentPane.add(lblRecInfo);
		
		JLabel lblReceiverRefNum = new JLabel("Reference Number:");
		lblReceiverRefNum.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReceiverRefNum.setBounds(10, 260, 128, 14);
		contentPane.add(lblReceiverRefNum);
		
		textFieldReceiverRefNum = new JTextField();
		textFieldReceiverRefNum.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					
					String refNum = textFieldReceiverRefNum.getText();
					ResultSet rs = acc.searchByCriteria("ref_num", refNum);
					
					
					while(rs.next()) {
						int id = rs.getInt("id");
						if(id>0) {
							textFieldReceiverRefNum.setText(rs.getString("ref_num"));
						}
					}
					
					
					
							
				}catch(Exception error){
					error.printStackTrace();
				}
			}
		});
		textFieldReceiverRefNum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					
					String refNum = textFieldReceiverRefNum.getText();
					
					if(refNum.length()<6) {
						textFieldRecName.setText("");
						textFieldRecAccCurr.setText("");
						return;
					}
					
					ResultSet rs = acc.searchByRefNum(refNum);
					
					while(rs.next()) {
						int id = rs.getInt("id");
						if(id<1) {
							textFieldRecName.setText("");
							textFieldRecAccCurr.setText("");
							return;
							
							
						}
						
						else {
							String fname = rs.getString("fname");
							String lname = rs.getString("lname");
							String curCode = rs.getString("Currency_Code");
							textFieldRecName.setText(fname + " " + lname);
							textFieldRecAccCurr.setText(curCode);
							
							return;
						}
					}
					
					textFieldRecName.setText("");
					textFieldRecAccCurr.setText("");
					
					
							
				}catch(Exception error){
					error.printStackTrace();
				}
			}
		});
		textFieldReceiverRefNum.setColumns(10);
		textFieldReceiverRefNum.setBounds(148, 258, 131, 20);
		contentPane.add(textFieldReceiverRefNum);
		
		JLabel lblRecName = new JLabel("Receiver's Name:");
		lblRecName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRecName.setBounds(10, 305, 128, 14);
		contentPane.add(lblRecName);
		
		textFieldRecName = new JTextField();
		textFieldRecName.setColumns(10);
		textFieldRecName.setBounds(148, 303, 131, 20);
		contentPane.add(textFieldRecName);
		
		JLabel lblAccountCurrency = new JLabel("Account Currency:");
		lblAccountCurrency.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAccountCurrency.setBounds(10, 350, 128, 14);
		contentPane.add(lblAccountCurrency);
		
		textFieldRecAccCurr = new JTextField();
		textFieldRecAccCurr.setColumns(10);
		textFieldRecAccCurr.setBounds(148, 348, 131, 20);
		contentPane.add(textFieldRecAccCurr);
		
		JLabel lblTransfer = new JLabel("Amount to Transfer");
		lblTransfer.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransfer.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTransfer.setBounds(50, 389, 212, 14);
		contentPane.add(lblTransfer);
		
		JLabel lblTransferAmt = new JLabel("Transfer Amount:");
		lblTransferAmt.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTransferAmt.setBounds(10, 426, 128, 14);
		contentPane.add(lblTransferAmt);
		
		textFieldTransferAmt = new JTextField();
		textFieldTransferAmt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String transferAmt =textFieldTransferAmt.getText();
				if((isNum(transferAmt) && Integer.parseInt(transferAmt)>0 && Integer.parseInt(transferAmt) < balance) 
						|| transferAmt.length()<1) {
					lblAmtError.setText("");
					
				}
				else if (isNum(transferAmt) == false || Integer.parseInt(transferAmt)<0){
					lblAmtError.setText("Please enter a positive number to transfer!");
				}
				
				else if (isNum(transferAmt) && Integer.parseInt(transferAmt)>balance){
					lblAmtError.setText("You can't transfer more then your balance!");
				}
			}
		});
		textFieldTransferAmt.setColumns(10);
		textFieldTransferAmt.setBounds(148, 424, 131, 20);
		contentPane.add(textFieldTransferAmt);
		
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				transfer(arg0);
			}
		});
		btnTransfer.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnTransfer.setBounds(190, 473, 89, 23);
		contentPane.add(btnTransfer);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JComponent comp = (JComponent) arg0.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancel.setBounds(10, 473, 89, 23);
		contentPane.add(btnCancel);
		
		lblPinerror = new JLabel("");
		lblPinerror.setForeground(Color.RED);
		lblPinerror.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPinerror.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPinerror.setBounds(128, 205, 148, 14);
		contentPane.add(lblPinerror);
		
		lblAmtError = new JLabel("");
		lblAmtError.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmtError.setForeground(Color.RED);
		lblAmtError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAmtError.setBounds(10, 448, 269, 14);
		contentPane.add(lblAmtError);
		
		
		fillForm();
		
	}
}
