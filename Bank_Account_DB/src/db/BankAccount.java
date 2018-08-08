package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class BankAccount{
	DBConnect connection = new DBConnect();
	Statement st = connection.listConnect();
	Connection con = connection.connectDB();
	Currency currency = new Currency();
	
	private ResultSet rs;
	
	private String fname;
	private String lname;
	private String pin;
	private int currency_id;
	private int balance;
	
	
	
	
	public BankAccount (String fname, String lname, String pin, int currency_id, int balance) {
    	this.fname = fname;
    	this.lname = lname;
    	this.pin = pin;
    	this.currency_id = currency_id;
    	this.balance = balance;
    }
	
	public BankAccount () {
    	
    }
	
	//checked && perfect statement
	private String createRefNum() {
		String ref_num ="";
		String fixPreNum = "1107";
		String idBasedEndNum = "";
		String id = "";
		try {
			String query = "SELECT * from bank_account ORDER BY id DESC LIMIT 1";
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				idBasedEndNum = rs.getString("id");
				id = rs.getString("id");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i =4; i>id.length(); i--) {
			idBasedEndNum = "0" + idBasedEndNum;
		}
		
		int randomNum = (int)(Math.random()*9000)+1000;
		String random = String.valueOf(randomNum);
		
		ref_num = fixPreNum + random + idBasedEndNum;
		return ref_num;
	}
	
	//checked && perfect statement
	public void addAccount(BankAccount newAcc) {
		String code = currency.getCurrencyCode(newAcc.currency_id);
		String ref_num ="";
		ref_num = createRefNum();
		
		PreparedStatement prep;
		try {
			prep = con.prepareStatement("INSERT INTO bank_account VALUES(null, ?, ?, ?, ?, ?, ?, ?)");
			prep.setString(1, newAcc.fname);
			prep.setString(2, newAcc.lname);
			prep.setString(3, newAcc.pin);
			prep.setString(4, Integer.toString(newAcc.currency_id));
			prep.setString(5, code);
			prep.setString(6, Integer.toString(newAcc.balance));
			prep.setString(7, ref_num);
			prep.execute();
			
			JOptionPane.showMessageDialog(null, "New account added to the DB with the reference number of: " + ref_num);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//checked && perfect statement
	public void updateAccount(int accId, String fname, String lname, String balance) {
		
		PreparedStatement prep;
		try {
			prep = con.prepareStatement("UPDATE bank_account SET fname=?, lname=?, balance=? WHERE id=" +accId);
			prep.setString(1, fname);
			prep.setString(2, lname);
			prep.setString(3, balance);
			prep.execute();
			
			JOptionPane.showMessageDialog(null, "The account has been updated.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//checked && perfect statement
	public int checkLogin(String pin, String refNum) {
			int accId = 0;
			
			try {
				String query = "SELECT id from bank_account WHERE pin = " + pin + " AND ref_num = " + refNum;
				rs = st.executeQuery(query);
				
				while(rs.next()) {
					if(rs.getInt("id")>0) {
						accId = rs.getInt("id");
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return accId;
	}
	
	

	//checked && perfect statement
	public ResultSet getAccData() {
		rs = null;
		
		try {
			String query = "SELECT id, fname, lname, Currency_Code, balance, ref_num from bank_account";
			rs = st.executeQuery(query);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rs;
	}
	
	//checked && perfect statement
	public ResultSet getAccData(int accId) {
		rs = null;
		
		try {
			String query = "SELECT * from bank_account WHERE id=" + accId;
			rs = st.executeQuery(query);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rs;
	}
	
	//checked && perfect statement
	public ResultSet searchByCriteria(String criteria, String keyword) throws SQLException {
		rs = null;
		keyword = "'%" + keyword + "%'";
			
		try {
			
			String query = "SELECT id, fname, lname, Currency_Code, balance, ref_num from bank_account WHERE "+criteria+" LIKE " + keyword;
			PreparedStatement prep = con.prepareStatement(query);
			rs = prep.executeQuery(query);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rs;
    }
	
	//checked && perfect statement
	public ResultSet searchByRefNum(String keyword) throws SQLException {
		rs = null;
		keyword = "'" + keyword + "%'";
			
		try {
			
			String query = "SELECT id, fname, lname, Currency_Code, balance, ref_num from bank_account WHERE ref_num LIKE " + keyword;
			PreparedStatement prep = con.prepareStatement(query);
			rs = prep.executeQuery(query);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rs;
    }
	
	//checked && perfect statement
	public void deleteAccount(int accId[]) {
		
		PreparedStatement prep;
		
		for(int i=0; i< accId.length; i++) {
			try {
				prep = con.prepareStatement("DELETE FROM bank_account WHERE id = " + accId[i]);
				prep.execute();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	//checked && perfect statement
	public void deposit(int accountId, int depositAmount) {
		
		int depo = depositAmount;
		int oldBalance = getAccountBalance(accountId);
		int newBalance = oldBalance + depo;
		
		
		PreparedStatement prep;
		
		try {
			prep = con.prepareStatement("UPDATE bank_account SET balance = " + newBalance +" WHERE id = " + accountId + ";");
			prep.execute();
			
			JOptionPane.showMessageDialog(null, "Deposit succesfull. New Balance: " + newBalance);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void withdraw(int accountId, int withdrawAmount) {
		
		int oldBalance = getAccountBalance(accountId);
		int newBalance = oldBalance - withdrawAmount;
		PreparedStatement prep;
		
		try {
			prep = con.prepareStatement("UPDATE bank_account SET balance = " + newBalance +" WHERE id = " +  accountId + ";");
			prep.execute();
			JOptionPane.showMessageDialog(null, "Withdrawal succesful! New balance: " + newBalance);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void transfer(int senderId, int receiverId, int transferAmount) {
		
		int sendId = senderId;
		int senderCurrId = getAccountCurrency(sendId);
		String senderCurrencyCode = currency.getCurrencyCode(senderCurrId);
		int transfer = transferAmount;
		int senderOldBalance = getAccountBalance(sendId);
		int senderNewBalance = senderOldBalance - transfer;
		
		
		PreparedStatement prep;
		
		try {
			prep = con.prepareStatement("UPDATE bank_account SET balance = " + senderNewBalance +" WHERE id = " + sendId + ";");
			prep.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int recId = receiverId;
		int recCurrId = getAccountCurrency(recId);
		
		
		double crossrate = currency.getCurrencyCrossRate(senderCurrId, recCurrId);
		int transferAfterConversion = (int) (transfer*crossrate);
		int recOldBalance = getAccountBalance(recId);
		int recNewBalance = recOldBalance + transferAfterConversion;
		
		
		PreparedStatement prep2;
		
		try {
			prep2 = con.prepareStatement("UPDATE bank_account SET balance = " + recNewBalance +" WHERE id = " + recId + ";");
			prep2.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String receiverName = getAccountName(recId);
		
		JOptionPane.showMessageDialog(null, "Thank you, transfer succesful!\n"
				+ transfer + " " + senderCurrencyCode + " has been transfered from your account to: " + receiverName +"\n"
				+ " New balance: "+ senderNewBalance + " "+ senderCurrencyCode);
		
	}
	
	public int getAccountCurrency (int accId) {
		
		int id = accId;
		int currId = 0;
				
		try {
			
			String query = "SELECT currency_id from bank_account WHERE id = " + id;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
			currId = rs.getInt("currency_id");
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return currId;
		
		
	}
	
	//checked && perfect statement
	public int getAccountBalance (int accId) {
		
		int id = accId;
		int balance = 0;
				
		try {
			
			String query = "SELECT balance from bank_account WHERE id = " + id;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
			balance = rs.getInt("balance");
			}
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return balance;
		
		
	}
	
	public String getAccountPin (int accId) {
		
		int id = accId;
		String pin = "";
				
		try {
			
			String query = "SELECT pin from bank_account WHERE id = " + id;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				pin = rs.getString("pin");
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return pin;
		
		
	}
	
	public String getAccountName (int accId) {
		
		int id = accId;
		String name = "";
		String fname = "";
		String lname = "";
				
		try {
			
			String query = "SELECT * from bank_account WHERE id = " + id;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				fname = rs.getString("fname");
				lname = rs.getString("lname");
				name = fname + " " + lname;
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return name;
		
		
	}
	
	public int getAccountId (String fname, String lname, int curId) {
		
		String fName = "'" + fname + "'";
		String lName = "'" + lname + "'";
		
		int cur_id = curId;
		int accId = 0;
				
		try {
			
			String query = "SELECT id from bank_account WHERE fname = " + fName + " AND lname = " + lName + " AND currency_id = " + cur_id;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				accId = rs.getInt("id");
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return accId;
		
		
	}
	
	public int getAccIdByRefNum (String ref_num) {
		
		String refNum = "'" + ref_num + "'";
		
		int accId = 0;
				
		try {
			
			String query = "SELECT id from bank_account WHERE ref_num = " + refNum;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				accId = rs.getInt("id");
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return accId;
		
		
	}

	public ResultSet getAccountIdCurrency (int curId) {
		int cur_id = curId;
		rs = null;
				
		try {
			
			String query = "SELECT id from bank_account WHERE currency_id = " + cur_id;
			rs = st.executeQuery(query);
			
		
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return rs;
	}
	
	public void changePin(String new_pin, String ref_num) {
		
		PreparedStatement prep;
		try {
			prep = con.prepareStatement("UPDATE bank_account SET pin=? WHERE ref_num=?");
			prep.setString(1, new_pin);
			prep.setString(2, ref_num);
			prep.execute();
			
			JOptionPane.showMessageDialog(null, "PIN Succesfully Changed!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//////////not ready
	public void changeCurrency(int newCurId, String newCurrency, String newBalance, String ref_num) {
		
		PreparedStatement prep;
		try {
			prep = con.prepareStatement("UPDATE bank_account SET currency_id="+newCurId+", Currency_Code=?, balance=? WHERE ref_num=?");
			prep.setString(1, newCurrency);
			prep.setString(2, newBalance);
			prep.setString(3, ref_num);
			prep.execute();
			
			JOptionPane.showMessageDialog(null, "Account Currency Succesfully Changed!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
