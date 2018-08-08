package GUI;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mysql.cj.protocol.Resultset;

import db.BankAccount;
import db.Currency;
import net.proteanit.sql.DbUtils;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class ControlPanel {
	private Currency cur = new Currency();
	private BankAccount acc = new BankAccount();
	private ResultSet rs;
	
	private JFrame frame;
	private JTable tableCurrency;
	private JTable tableAccount;
	private JTextField textFieldSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ControlPanel window = new ControlPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void fillCurrencyTable() {
		rs = cur.getCurrencyData();
		tableCurrency.setModel(DbUtils.resultSetToTableModel(rs));
	}
	
	public void fillAccountTable() {
		rs = acc.getAccData();
		tableAccount.setModel(DbUtils.resultSetToTableModel(rs));
	}
	
	
	/**
	 * Create the application.
	 */
	public ControlPanel() {
		initialize();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 591);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHeader = new JLabel("Welcome to Bank DataBase");
		lblHeader.setBounds(203, 11, 334, 41);
		lblHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
		frame.getContentPane().add(lblHeader);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 56, 714, 491);
		frame.getContentPane().add(tabbedPane);
		
		JPanel Currency = new JPanel();
		tabbedPane.addTab("Currency Operations", null, Currency, null);
		Currency.setLayout(null);
		
		JLabel lblCurimage = new JLabel("");
		lblCurimage.setBounds(499, 27, 200, 200);
		Image img = new ImageIcon(this.getClass().getResource("/currency-icon.png")).getImage();
		lblCurimage.setIcon(new ImageIcon(img));
		Currency.add(lblCurimage);
		
		JLabel lblCurlabel = new JLabel("Currency operations in our Bank");
		lblCurlabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCurlabel.setBounds(119, 11, 267, 36);
		Currency.add(lblCurlabel);
		
		JScrollPane scrollPaneCurrency = new JScrollPane();
		scrollPaneCurrency.setBounds(10, 58, 479, 345);
		Currency.add(scrollPaneCurrency);
		
		tableCurrency = new JTable();
		scrollPaneCurrency.setViewportView(tableCurrency);
		
		JButton btnListCur = new JButton("List all currencies");
		btnListCur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillCurrencyTable();
			}
			
		});
		btnListCur.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnListCur.setBounds(533, 249, 139, 36);
		Currency.add(btnListCur);
		
		JButton btnNewCur = new JButton("New Currency");
		btnNewCur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CurrencyCreateUpdate updateWindow;
				try {
					updateWindow = new CurrencyCreateUpdate(0);
					updateWindow.setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewCur.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewCur.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewCur.setBounds(533, 334, 139, 69);
		Image img2 = new ImageIcon(this.getClass().getResource("/new-icon.png")).getImage();
		btnNewCur.setIcon(new ImageIcon(img2));
		Currency.add(btnNewCur);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tableCurrency.getSelectedRow();
				if(row!=-1) {
					int updateId = (int) tableCurrency.getModel().getValueAt(row, 0);
					CurrencyCreateUpdate updateWindow;
					try {
						updateWindow = new CurrencyCreateUpdate(updateId);
						updateWindow.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Select one row to update!");
				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdate.setBounds(350, 414, 139, 23);
		Currency.add(btnUpdate);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row[] = tableCurrency.getSelectedRows();
				
				if(row.length>0) {
					int deleteId [] = new int[row.length];
					for(int i=0; i<row.length; i++) {
						deleteId [i] = (int) tableCurrency.getModel().getValueAt(row[i], 0);
					}
					
					int action = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected row(s)?", "Delete", JOptionPane.YES_NO_OPTION);
					if(action==0) {
						try {
							cur.deleteCurrency(deleteId);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						fillCurrencyTable();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Select at least one row to delete!");
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDelete.setBounds(10, 414, 132, 23);
		Currency.add(btnDelete);
		
		JPanel Account = new JPanel();
		tabbedPane.addTab("Account List & Operations", null, Account, null);
		Account.setLayout(null);
		
		JScrollPane scrollPaneAccount = new JScrollPane();
		scrollPaneAccount.setBounds(10, 58, 479, 345);
		Account.add(scrollPaneAccount);
		
		tableAccount = new JTable();
		scrollPaneAccount.setViewportView(tableAccount);
		
		JLabel labelAccount = new JLabel("Account operations in our Bank");
		labelAccount.setFont(new Font("Tahoma", Font.BOLD, 15));
		labelAccount.setBounds(123, 11, 241, 36);
		Account.add(labelAccount);
		
		JLabel labelAccountImg = new JLabel("");
		labelAccountImg.setBounds(499, 27, 200, 200);
		Image img3 = new ImageIcon(this.getClass().getResource("/visa-icon.png")).getImage();
		labelAccountImg.setIcon(new ImageIcon(img3));
		Account.add(labelAccountImg);
		
		JButton buttonListAccounts = new JButton("List all accounts");
		buttonListAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillAccountTable();
			}
		});
		buttonListAccounts.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonListAccounts.setBounds(526, 227, 139, 29);
		Account.add(buttonListAccounts);
		
		JButton btnNewAccount = new JButton("New Account");
		btnNewAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AccountCreateUpdate updateWindow;
				try {
					updateWindow = new AccountCreateUpdate(0);
					updateWindow.setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewAccount.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewAccount.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewAccount.setBounds(526, 289, 139, 36);
		Image img4 = new ImageIcon(this.getClass().getResource("/new-acc.png")).getImage();
		btnNewAccount.setIcon(new ImageIcon(img4));
		Account.add(btnNewAccount);
		
		JButton buttonUpdateAcc = new JButton("Update");
		buttonUpdateAcc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tableAccount.getSelectedRow();
				if(row!=-1) {
					int updateId = (int) tableAccount.getModel().getValueAt(row, 0);
					AccountCreateUpdate updateWindow;
					try {
						updateWindow = new AccountCreateUpdate(updateId);
						updateWindow.setVisible(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Select one row to update!");
				}
			}
		});
		buttonUpdateAcc.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonUpdateAcc.setBounds(350, 414, 139, 23);
		Account.add(buttonUpdateAcc);
		
		JButton button = new JButton("Delete");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row[] = tableAccount.getSelectedRows();
				
				if(row.length>0) {
					int deleteId [] = new int[row.length];
					for(int i=0; i<row.length; i++) {
						deleteId [i] = (int) tableAccount.getModel().getValueAt(row[i], 0);
					}
					
					int action = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected row(s)?", "Delete", JOptionPane.YES_NO_OPTION);
					if(action==0) {
						acc.deleteAccount(deleteId);
						fillAccountTable();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Select at least one row to delete!");
				}
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 11));
		button.setBounds(10, 414, 132, 23);
		Account.add(button);
		
		JComboBox comboBoxCriteria = new JComboBox();
		comboBoxCriteria.setFont(new Font("Tahoma", Font.BOLD, 11));
		comboBoxCriteria.setModel(new DefaultComboBoxModel(new String[] {"Search Criteria", "fname", "lname", "ref_num"}));
		comboBoxCriteria.setBounds(537, 358, 115, 23);
		Account.add(comboBoxCriteria);
		
		textFieldSearch = new JTextField();
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String criteria = comboBoxCriteria.getSelectedItem().toString();
					
					if(criteria != "Search Criteria") {
						String keyword = textFieldSearch.getText();
						rs = acc.searchByCriteria(criteria, keyword);
						tableAccount.setModel(DbUtils.resultSetToTableModel(rs));
					}
							
				}catch(Exception error){
					error.printStackTrace();
				}
			}
		});
		textFieldSearch.setBounds(526, 415, 139, 20);
		Account.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		JPanel Transactions = new JPanel();
		tabbedPane.addTab("Transactions", null, Transactions, null);
		Transactions.setLayout(null);
		
		JLabel lblTransactionImg = new JLabel("");
		lblTransactionImg.setBounds(499, 27, 200, 200);
		Image img5 = new ImageIcon(this.getClass().getResource("/money.png")).getImage();
		lblTransactionImg.setIcon(new ImageIcon(img5));
		Transactions.add(lblTransactionImg);
		
		JLabel lblDeposit = new JLabel("");
		lblDeposit.setBounds(66, 44, 128, 128);
		Image img6 = new ImageIcon(this.getClass().getResource("/Deposit-Money-Icon.png")).getImage();
		lblDeposit.setIcon(new ImageIcon(img6));
		Transactions.add(lblDeposit);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransactionIdentification loginWindow = new TransactionIdentification("Deposit");
				loginWindow.setVisible(true);
			}
		});
		btnDeposit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDeposit.setBounds(75, 183, 98, 23);
		Transactions.add(btnDeposit);
		
		JLabel labelWithdraw = new JLabel("");
		labelWithdraw.setBounds(323, 44, 128, 128);
		Image img7 = new ImageIcon(this.getClass().getResource("/withdraw.png")).getImage();
		labelWithdraw.setIcon(new ImageIcon(img7));
		Transactions.add(labelWithdraw);
		
		JButton buttonWithdraw = new JButton("Withdraw");
		buttonWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransactionIdentification loginWindow = new TransactionIdentification("Withdraw");
				loginWindow.setVisible(true);
			}
		});
		buttonWithdraw.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonWithdraw.setBounds(334, 183, 98, 23);
		Transactions.add(buttonWithdraw);
		
		JLabel labelTransfer = new JLabel("");
		labelTransfer.setBounds(66, 269, 128, 128);
		Image img8 = new ImageIcon(this.getClass().getResource("/transfer2.png")).getImage();
		labelTransfer.setIcon(new ImageIcon(img8));
		Transactions.add(labelTransfer);
		
		JLabel lblTransActionsHeader = new JLabel("Customer Transactions");
		lblTransActionsHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransActionsHeader.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTransActionsHeader.setBounds(162, 11, 222, 22);
		Transactions.add(lblTransActionsHeader);
		
		JButton buttonTransfer = new JButton("Transfer");
		buttonTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TransactionIdentification loginWindow = new TransactionIdentification("Transfer");
				loginWindow.setVisible(true);
			}
		});
		buttonTransfer.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonTransfer.setBounds(75, 408, 98, 23);
		Transactions.add(buttonTransfer);
		
		JLabel labelPinChange = new JLabel("");
		labelPinChange.setBounds(323, 269, 128, 128);
		Image img9 = new ImageIcon(this.getClass().getResource("/pin-change.png")).getImage();
		labelPinChange.setIcon(new ImageIcon(img9));
		Transactions.add(labelPinChange);
		
		JButton buttonPinChange = new JButton("Change PIN");
		buttonPinChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TransactionIdentification loginWindow = new TransactionIdentification("PinChange");
				loginWindow.setVisible(true);
			}
		});
		buttonPinChange.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonPinChange.setBounds(334, 408, 98, 23);
		Transactions.add(buttonPinChange);
		
		JLabel labelCurrencyChange = new JLabel("");
		labelCurrencyChange.setBounds(537, 269, 128, 128);
		Image img10 = new ImageIcon(this.getClass().getResource("/currency_change_icon.png")).getImage();
		labelCurrencyChange.setIcon(new ImageIcon(img10));
		Transactions.add(labelCurrencyChange);
		
		JButton buttonCurrencyChange = new JButton("Change Currency");
		buttonCurrencyChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TransactionIdentification loginWindow = new TransactionIdentification("CurrencyChange");
				loginWindow.setVisible(true);
			}
		});
		buttonCurrencyChange.setFont(new Font("Tahoma", Font.BOLD, 11));
		buttonCurrencyChange.setBounds(537, 408, 128, 23);
		Transactions.add(buttonCurrencyChange);
		
		
	}
}
