package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Currency {
	DBConnect connection = new DBConnect();
	Statement st = connection.listConnect();
	Connection con = connection.connectDB();
	
	private ResultSet rs;
	private ResultSet rs1; 
    
	String CurrencyCode;
	String CentralBankName;
	int CurrencyValue;
	String Currencyname;
	
        
    
    public Currency(String CurrencyCode, String CentralBankName, int CurrencyValue, String Currencyname) {
    	this.CurrencyCode = CurrencyCode;
    	this.CentralBankName = CentralBankName;
    	this.CurrencyValue = CurrencyValue;
    	this.Currencyname = Currencyname;
    } 
    
    public Currency() {
    	
    } 
    
    //checked && perfect statement    
    public ResultSet getCurrencyData() {
    	rs = null;
    	
    	try {
			
			String query = "SELECT * from currency";
			rs = st.executeQuery(query);
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
    	
    	return rs;
	}
    
    //checked && perfect statement	
	public void addCurrency(Currency cur) throws ClassNotFoundException, SQLException {
		
		int id = 0;
		String code = cur.CurrencyCode;
		String central_bank = cur.CentralBankName;
		int value_in_huf = cur.CurrencyValue;
		String name = cur.Currencyname;
		
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO currency VALUES(" + id + ",?,?," + value_in_huf + ",?);");
		
		prep.setString(1, code);
		prep.setString(2, central_bank);
		prep.setString(3, name);
		prep.execute();
		
		JOptionPane.showMessageDialog(null, "A new currency added with the code of: " + code);
	}
	
	//checked && perfect statement
	public void deleteCurrency(int curId[]) throws SQLException {
		BankAccount acc = new BankAccount();
		PreparedStatement prep;
		String curCode ="";
		
		for(int i = 0; i<curId.length; i++) {
			int id = 0;
			rs = acc.getAccountIdCurrency(curId[i]);
			
			
			while(rs.next()) {
				id = rs.getInt("id");
				
				
			}
			
			if(id>0) {
				rs1 = getCurDataByID(curId[i]);
				while(rs1.next()) {
					curCode =rs1.getString("code");
				}
				JOptionPane.showMessageDialog(null, "The selected currency with the code of " + curCode + " can not be deleted, while there are"
						+ "active accounts related to it!");
				
			}
			
			else {
				rs1 = getCurDataByID(curId[i]);
				while(rs1.next()) {
					curCode =rs1.getString("code");
				}
				prep = con.prepareStatement("DELETE FROM currency WHERE id = " + curId[i]);
				prep.execute();
				
				JOptionPane.showMessageDialog(null, "The selected currency with the code of " + curCode + " has been deleted!");
				
			}
			
			
		}
		
	}
	
	//checked && perfect statement
	public void updateCurrency(int currencyID, String code, String central_bank, String value_in_huf, String name) {
		PreparedStatement prep;
		
		try {
			prep = con.prepareStatement("UPDATE currency SET code=?, central_bank=?, value_in_huf=?, name=? WHERE id=" + currencyID);
			prep.setString(1, code);
			prep.setString(2, central_bank);
			prep.setString(3, value_in_huf);
			prep.setString(4, name);
			prep.execute();
			
			JOptionPane.showMessageDialog(null, "Currency updated with the code of: " + code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//checked && perfect statement
	public int getCurrencyIdByCode (String currencyCode) {
		int id = 0 ;
				
		try {
			
			String query = "SELECT id FROM currency WHERE code=?";
			PreparedStatement prep = con.prepareStatement(query);
 			prep.setString(1, currencyCode);
			rs = prep.executeQuery();
						
			while(rs.next()) {
				id = rs.getInt("id");
				
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return id;
		
	}
	
	//checked && perfect statement
	public ResultSet getCurDataByID (int currencyId) {
		
		rs = null;
				
		try {
			
			String query = "SELECT * from currency WHERE id = " + currencyId;
			rs = st.executeQuery(query);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rs;
		
		
	}

	public double getCurrencyCrossRate (int senderCurrencyId, int receiverCurrencyId) {
		
		int senderCurId = senderCurrencyId;
		int receiverCurId = receiverCurrencyId;
		double crossrate = 0;
		int senderValue = 0;
		int receiverValue = 1;
				
		try {
			
			String query = "SELECT value_in_huf from currency WHERE id = " + senderCurId;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				senderValue = rs.getInt("value_in_huf");
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		try {
			
			String query = "SELECT value_in_huf from currency WHERE id = " + receiverCurId;
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				receiverValue = rs.getInt("value_in_huf");
			}
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		crossrate = (double) senderValue / receiverValue;
		
		return crossrate;
		
		
	}
	
	//checked && perfect statement
	public String getCurrencyCode(int cur_id) {
		String currencyCode = "";
		
		try {
			
			String query = "SELECT code from currency WHERE id = " + cur_id;
			rs = st.executeQuery(query);
			while(rs.next()) {
				currencyCode = rs.getString("code");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return currencyCode;
	}
}
