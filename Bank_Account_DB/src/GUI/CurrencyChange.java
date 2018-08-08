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
import db.Currency;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class CurrencyChange extends JFrame {
	private int accId;
	private int balance;
	private int curId;
	private String ref_num;
	private BankAccount acc = new BankAccount();
	private Currency cur = new Currency();
	private ResultSet rs;
	
	
	private JPanel contentPane;
	private JTextField txtRefnum;
	private JTextField txtCurrencycode;
	private JTextField txtNewBalance;
	private JTextField txtBalance;
	private JPasswordField pwdPin;
	private JLabel lblWelcome;
	private JComboBox<String> comboBoxCurrency;
	
	private void fillForm() throws SQLException {
		rs = acc.getAccData(accId);
		
		while(rs.next()) {
			curId = rs.getInt("currency_id");
			
			lblWelcome.setText("Welcome " + rs.getString("fname") +" " + rs.getString("lname") +" ");
			
			txtCurrencycode.setText(rs.getString("Currency_Code"));
			txtCurrencycode.setEditable(false);
			
			txtBalance.setText(rs.getString("balance"));
			txtBalance.setEditable(false);
			balance = Integer.parseInt(rs.getString("balance"));
			
			comboBoxCurrency.setModel(new DefaultComboBoxModel(new String[] {"Select Currency"}));
			ResultSet rs1 = cur.getCurrencyData();
			while(rs1.next()) {
				comboBoxCurrency.addItem(rs1.getString("code"));
			}
		}
		
		
		
		txtRefnum.setText(ref_num);
		txtRefnum.setEditable(false);
	}
	
	private void fillNewBalance() {
		String selectedItem = comboBoxCurrency.getSelectedItem().toString();
		
		if(selectedItem != "Select Currency") {
			int selectedCurrency = cur.getCurrencyIdByCode(selectedItem);
			
			double crossRate = cur.getCurrencyCrossRate(curId, selectedCurrency);
			
			int newBalance = (int) (crossRate * balance);
			
			txtNewBalance.setText(Integer.toString(newBalance));
		}
		
		else {
			txtNewBalance.setText("");
		}
		
		
	}
	
	private void changeCurrency(ActionEvent e) {
		String pin = pwdPin.getText();
		int selectedCurrency;
		String selectedItem = comboBoxCurrency.getSelectedItem().toString();
		
		if(comboBoxCurrency.getSelectedIndex()>0) {
			selectedCurrency = cur.getCurrencyIdByCode(selectedItem);
		}
		else {
			JOptionPane.showMessageDialog(null, "Please select a currency to change account denomination!");
			return;
		}
		
		if(selectedItem == "Select Currency") {
			JOptionPane.showMessageDialog(null, "Please select a currency to change account denomination!");
			return;
		}
		
		if(selectedCurrency == curId) {
			JOptionPane.showMessageDialog(null, "Please select a currency other then your current to change account denomination!");
			return;
		}
		
		if(isNum(pin) != true || pin.length()!=4) {
			JOptionPane.showMessageDialog(null, "PIN Must be 4 digit!");
			return;
		}
		
		if(acc.checkLogin(pin, this.ref_num) >0) {
			int action = JOptionPane.showConfirmDialog(null, "Do you want to change your Account Denomination?", "Change Account Denomination", JOptionPane.YES_NO_OPTION);
			if(action==0) {
				
				acc.changeCurrency(selectedCurrency, selectedItem, txtNewBalance.getText(), ref_num);
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
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
	
	
	/**
	 * Create the frame.
	 */
	public CurrencyChange(int accId, String ref_num) {
		this.accId = accId;
		this.ref_num = ref_num;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(250, 250, 455, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelImg = new JLabel("");
		labelImg.setBounds(301, 11, 128, 128);
		Image img = new ImageIcon(this.getClass().getResource("/currency_exchange.png")).getImage();
		labelImg.setIcon(new ImageIcon(img));
		contentPane.add(labelImg);
		
		JLabel lblHeader = new JLabel("Change Account Currency");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setBounds(37, 11, 212, 26);
		contentPane.add(lblHeader);
		
		JLabel lblRefNum = new JLabel("Reference Number:");
		lblRefNum.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRefNum.setBounds(10, 87, 165, 14);
		contentPane.add(lblRefNum);
		
		JLabel lblCurrency = new JLabel("Currency Code:");
		lblCurrency.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCurrency.setBounds(10, 127, 165, 14);
		contentPane.add(lblCurrency);
		
		JLabel lblNewCurrency = new JLabel("Selected Currency:");
		lblNewCurrency.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewCurrency.setBounds(10, 207, 165, 14);
		contentPane.add(lblNewCurrency);
		
		JLabel lblNewBalance = new JLabel("Balance in new currency:");
		lblNewBalance.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewBalance.setBounds(10, 247, 165, 14);
		contentPane.add(lblNewBalance);
		
		txtRefnum = new JTextField();
		txtRefnum.setBounds(180, 85, 111, 20);
		contentPane.add(txtRefnum);
		txtRefnum.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComponent comp = (JComponent) e.getSource();
				Window win = SwingUtilities.getWindowAncestor(comp);
				win.dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancel.setBounds(10, 325, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeCurrency(e);
			}
		});
		btnSubmit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSubmit.setBounds(202, 325, 89, 23);
		contentPane.add(btnSubmit);
		
		txtCurrencycode = new JTextField();
		txtCurrencycode.setBounds(178, 125, 113, 20);
		contentPane.add(txtCurrencycode);
		txtCurrencycode.setColumns(10);
		
		comboBoxCurrency = new JComboBox();
		comboBoxCurrency.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				fillNewBalance();
			}
		});
		comboBoxCurrency.setFont(new Font("Tahoma", Font.BOLD, 11));
		comboBoxCurrency.setBounds(178, 204, 113, 22);
		contentPane.add(comboBoxCurrency);
		
		txtNewBalance = new JTextField();
		txtNewBalance.setColumns(10);
		txtNewBalance.setBounds(180, 245, 113, 20);
		contentPane.add(txtNewBalance);
		txtNewBalance.setEditable(false);
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBalance.setBounds(10, 167, 165, 14);
		contentPane.add(lblBalance);
		
		txtBalance = new JTextField();
		txtBalance.setColumns(10);
		txtBalance.setBounds(178, 165, 113, 20);
		contentPane.add(txtBalance);
		
		JLabel lblPin = new JLabel("Enter PIN Number:");
		lblPin.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPin.setBounds(10, 287, 165, 14);
		contentPane.add(lblPin);
		
		pwdPin = new JPasswordField();
		pwdPin.setBounds(180, 285, 111, 20);
		contentPane.add(pwdPin);
		
		lblWelcome = new JLabel("");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(14, 37, 277, 26);
		contentPane.add(lblWelcome);
		
		try {
			fillForm();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
