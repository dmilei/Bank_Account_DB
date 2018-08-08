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

public class CurrencyCreateUpdate extends JFrame {
	private int selectedCurrency;
	private Currency cur = new Currency();
	
	private JPanel contentPane;
	private JLabel lblHeader;
	private JLabel lblCode;
	private JLabel lblCBank;
	private JLabel lblValue;
	private JLabel lblName;
	private JTextField textFieldCode;
	private JTextField textFieldCBank;
	private JTextField textFieldValue;
	private JTextField textFieldName;
	private JButton btnAction;
	private JLabel lblError;
	private JLabel lblCodeError;
	private JLabel lblValueError;
	private JButton btnCancel;
	
	private void fillForm() throws SQLException {
		if(selectedCurrency == 0) {
			lblHeader.setText("Adding New Currency");
			btnAction.setText("Add Currency");
		}
		
		else {
			lblHeader.setText("Update Currency");
			btnAction.setText("Update");
			ResultSet rs = cur.getCurDataByID(selectedCurrency);
			
			while(rs.next()) {
				textFieldCode.setText(rs.getString("code"));
				textFieldCode.setEditable(false);
				textFieldCBank.setText(rs.getString("central_bank"));
				textFieldValue.setText(rs.getString("value_in_huf"));
				textFieldName.setText(rs.getString("name"));
			}
		}
	}
	
	public void newCurrency(ActionEvent e) throws ClassNotFoundException, SQLException {
		String code = textFieldCode.getText();
		String cBank = textFieldCBank.getText();
		String value = textFieldValue.getText();
		String name = textFieldName.getText();
		if(textFieldCode.getText().isEmpty() || textFieldName.getText().isEmpty() 
				|| textFieldCBank.getText().isEmpty() || textFieldValue.getText().isEmpty()) {
			lblError.setText("Please fill all text field to add a new currency!");
		}
		else {
			if(cur.getCurrencyIdByCode(code) != 0) {
				JOptionPane.showMessageDialog(null, "There is a currency with this code in the database! You can't add two currencies with the same code!");
			}
			else if(!isCodeOk(code)){
				JOptionPane.showMessageDialog(null, "The code must be made of 3 Capital Letters!");
			}
			else if(!isNum(value) || Integer.parseInt(value)<1){
				JOptionPane.showMessageDialog(null, "The currency value must be a positive number!");
			}
			else {
				int action = JOptionPane.showConfirmDialog(null, "Do you want to add this new currency?", "Add new currency", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					Currency newCurrency = new Currency(code, cBank, Integer.parseInt(value), name);
					cur.addCurrency(newCurrency);
					JComponent comp = (JComponent) e.getSource();
					Window win = SwingUtilities.getWindowAncestor(comp);
					win.dispose();
				}
				
			}
		}
	}
	
	public void updateCurrency(ActionEvent e) throws ClassNotFoundException, SQLException {
		String code = textFieldCode.getText();
		String cBank = textFieldCBank.getText();
		String value = textFieldValue.getText();
		String name = textFieldName.getText();
		if(textFieldCode.getText().isEmpty() || textFieldName.getText().isEmpty() 
				|| textFieldCBank.getText().isEmpty() || textFieldValue.getText().isEmpty()) {
			lblError.setText("Please fill all text field to update the currency!");
		}
		else {
			if(cur.getCurrencyIdByCode(code) != 0 && cur.getCurrencyIdByCode(code) !=selectedCurrency) {
				JOptionPane.showMessageDialog(null, "There is a currency with this code in the database! You can't add two currencies with the same code!");
			}
			else if(!isCodeOk(code)){
				JOptionPane.showMessageDialog(null, "The code must be made of 3 Capital Letters!");
			}
			else if(!isNum(value) || Integer.parseInt(value)<1){
				JOptionPane.showMessageDialog(null, "The currency value must be a positive number!");
			}
			else {
				int action = JOptionPane.showConfirmDialog(null, "Do you want to update the selected currency?", "Update Currency", JOptionPane.YES_NO_OPTION);
				if(action==0) {
					cur.updateCurrency(selectedCurrency, code, cBank, value, name);
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
	public CurrencyCreateUpdate(int currencyId) throws SQLException {
		selectedCurrency = currencyId;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 319);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCurimg = new JLabel("");
		lblCurimg.setBounds(296, 11, 128, 128);
		Image img = new ImageIcon(this.getClass().getResource("/dollar.png")).getImage();
		lblCurimg.setIcon(new ImageIcon(img));
		contentPane.add(lblCurimg);
		
		lblHeader = new JLabel("");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(92, 0, 226, 33);
		contentPane.add(lblHeader);
		
		lblCode = new JLabel("Code:");
		lblCode.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCode.setBounds(10, 50, 105, 14);
		contentPane.add(lblCode);
		
		lblCBank = new JLabel("Central Bank:");
		lblCBank.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCBank.setBounds(10, 95, 105, 14);
		contentPane.add(lblCBank);
		
		lblValue = new JLabel("Value in HUF:");
		lblValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblValue.setBounds(10, 140, 105, 14);
		contentPane.add(lblValue);
		
		lblName = new JLabel("Currency Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblName.setBounds(10, 185, 105, 14);
		contentPane.add(lblName);
		
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
		textFieldCode.setBounds(125, 48, 161, 20);
		contentPane.add(textFieldCode);
		textFieldCode.setColumns(10);
		
		textFieldCBank = new JTextField();
		textFieldCBank.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
		});
		textFieldCBank.setColumns(10);
		textFieldCBank.setBounds(125, 93, 161, 20);
		contentPane.add(textFieldCBank);
		
		textFieldValue = new JTextField();
		textFieldValue.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
			@Override
			public void keyReleased(KeyEvent e) {
				String value = textFieldValue.getText();
				if(isNum(value) == false && textFieldValue.getText().isEmpty()==false) {
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
		textFieldValue.setColumns(10);
		textFieldValue.setBounds(125, 138, 161, 20);
		contentPane.add(textFieldValue);
		
		textFieldName = new JTextField();
		textFieldName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				lblError.setText("");
			}
		});
		textFieldName.setColumns(10);
		textFieldName.setBounds(125, 179, 161, 20);
		contentPane.add(textFieldName);
		
		btnAction = new JButton("");
		btnAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedCurrency == 0) {
					try {
						newCurrency(e);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				else {
					try {
						updateCurrency(e);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnAction.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAction.setBounds(175, 221, 111, 23);
		contentPane.add(btnAction);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblError.setBounds(10, 256, 276, 14);
		contentPane.add(lblError);
		
		lblCodeError = new JLabel("");
		lblCodeError.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCodeError.setForeground(Color.RED);
		lblCodeError.setBounds(30, 71, 256, 14);
		contentPane.add(lblCodeError);
		
		lblValueError = new JLabel("");
		lblValueError.setHorizontalAlignment(SwingConstants.RIGHT);
		lblValueError.setForeground(Color.RED);
		lblValueError.setBounds(60, 160, 226, 14);
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
		btnCancel.setBounds(10, 221, 89, 23);
		contentPane.add(btnCancel);
		
		fillForm();
	}
}
