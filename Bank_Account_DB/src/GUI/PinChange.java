package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import db.BankAccount;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class PinChange extends JFrame {
	private int accId;
	private String ref_num;
	private BankAccount acc = new BankAccount();
	
	
	private JPanel contentPane;
	private JPasswordField pwdOldPin;
	private JPasswordField pwdNewPin1;
	private JPasswordField pwdNewPin2;
	private JTextField txtRefnum;
	private JLabel lblOldpinerror;
	private JLabel lblNewpinerror;
	private JLabel lblNewpinerror1;
	
	private void fillForm() {
		txtRefnum.setText(ref_num);
		txtRefnum.setEditable(false);
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
	
	private void changePin(ActionEvent e) {
		String oldPin = pwdOldPin.getText();
		String newPin1 = pwdNewPin1.getText();
		String newPin2 = pwdNewPin2.getText();
		
		if(pwdOldPin.getText().isEmpty() || pwdNewPin1.getText().isEmpty() || pwdNewPin2.getText().isEmpty()) {
			lblNewpinerror1.setText("Fill all textfields!");
			return;
		} 
		else  {
			if(isNum(oldPin) != true || oldPin.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN Must be 4 digit!");
				return;
			}
			
			if(isNum(newPin1) != true || newPin1.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN Must be 4 digit!");
				return;
			}
			
			if(isNum(newPin2) != true || newPin2.length()!=4) {
				JOptionPane.showMessageDialog(null, "PIN Must be 4 digit!");
				return;
			}
			
			if(Integer.parseInt(newPin1) != Integer.parseInt(newPin2)) {
				JOptionPane.showMessageDialog(null, "The value of the two new PIN is not identical!");
				return;
			}
			
			if(acc.checkLogin(oldPin, this.ref_num) >0) {
				int action = JOptionPane.showConfirmDialog(null, "Do you want to change your PIN number?", "Change PIN", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					acc.changePin(newPin1, this.ref_num);
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
				
			}
			
			else {
				JOptionPane.showMessageDialog(null, "PIN change failed! The old PIN number is not correct!");
			}
		}
		
		
	}

	/**
	 * Create the frame.
	 */
	public PinChange(int accId, String ref_num) {
		this.accId = accId;
		this.ref_num = ref_num;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 250, 455, 329);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelImg = new JLabel("");
		labelImg.setBounds(301, 11, 128, 128);
		Image img = new ImageIcon(this.getClass().getResource("/pinchange_color.png")).getImage();
		labelImg.setIcon(new ImageIcon(img));
		contentPane.add(labelImg);
		
		JLabel lblHeader = new JLabel("Change PIN");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(37, 11, 212, 26);
		contentPane.add(lblHeader);
		
		JLabel lblRefNum = new JLabel("Reference Number:");
		lblRefNum.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRefNum.setBounds(10, 60, 165, 14);
		contentPane.add(lblRefNum);
		
		JLabel lblOldpin = new JLabel("Old PIN Number:");
		lblOldpin.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOldpin.setBounds(10, 110, 165, 14);
		contentPane.add(lblOldpin);
		
		JLabel lblNewPin1 = new JLabel("New PIN Number:");
		lblNewPin1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewPin1.setBounds(10, 160, 165, 14);
		contentPane.add(lblNewPin1);
		
		JLabel lblNewPin2 = new JLabel("Repeat new PIN Number:");
		lblNewPin2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewPin2.setBounds(10, 210, 165, 14);
		contentPane.add(lblNewPin2);
		
		txtRefnum = new JTextField();
		txtRefnum.setBounds(180, 58, 111, 20);
		contentPane.add(txtRefnum);
		txtRefnum.setColumns(10);
		
		pwdOldPin = new JPasswordField();
		pwdOldPin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String pin = pwdOldPin.getText();
				if((isNum(pin) == true && pin.length()==4) || pin.length()<4) {
					lblOldpinerror.setText("");
					
				}
				else {
					lblOldpinerror.setText("PIN is made of 4 digits!");
				}
			}
		});
		pwdOldPin.setBounds(180, 108, 111, 20);
		contentPane.add(pwdOldPin);
		
		pwdNewPin1 = new JPasswordField();
		pwdNewPin1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String pin = pwdNewPin1.getText();
				if((isNum(pin) == true && pin.length()==4) || pin.length()<4) {
					lblNewpinerror.setText("");
					
				}
				else {
					lblNewpinerror.setText("PIN is made of 4 digits!");
				}
			}
		});
		pwdNewPin1.setBounds(180, 158, 111, 20);
		contentPane.add(pwdNewPin1);
		
		pwdNewPin2 = new JPasswordField();
		pwdNewPin2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String firstPin = pwdNewPin1.getText();
				String secondPin = pwdNewPin2.getText();
				if((isNum(firstPin) && isNum(secondPin) && Integer.parseInt(firstPin)==Integer.parseInt(secondPin)) || secondPin.length()<4) {
					lblNewpinerror1.setText("");
					
				}
				else {
					lblNewpinerror1.setText("The value of the two new PIN is not identical!");
				}
			}
		});
		pwdNewPin2.setBounds(180, 208, 111, 20);
		contentPane.add(pwdNewPin2);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancel.setBounds(23, 257, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePin(e);
			}
		});
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSubmit.setBounds(202, 257, 89, 23);
		contentPane.add(btnSubmit);
		
		lblOldpinerror = new JLabel("");
		lblOldpinerror.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOldpinerror.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOldpinerror.setForeground(Color.RED);
		lblOldpinerror.setBounds(148, 139, 143, 14);
		contentPane.add(lblOldpinerror);
		
		lblNewpinerror = new JLabel("");
		lblNewpinerror.setForeground(Color.RED);
		lblNewpinerror.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewpinerror.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewpinerror.setBounds(148, 183, 143, 14);
		contentPane.add(lblNewpinerror);
		
		lblNewpinerror1 = new JLabel("");
		lblNewpinerror1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewpinerror1.setForeground(Color.RED);
		lblNewpinerror1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewpinerror1.setBounds(10, 232, 281, 14);
		contentPane.add(lblNewpinerror1);
		
		fillForm();
	}
}
