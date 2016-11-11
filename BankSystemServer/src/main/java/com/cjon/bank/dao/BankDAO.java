package com.cjon.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.cjon.bank.dto.AccountDTO;
import com.cjon.bank.dto.BankDTO;

public class BankDAO {
	
	private Connection con;
	public BankDAO(Connection con) {
		this.con = con;
	}

	 public ArrayList<BankDTO> selectAll() {
		 PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 ArrayList<BankDTO> list=new ArrayList<BankDTO>();
		  
		 try{
			 String sql="select * from bank_member_tb";
			 pstmt= con.prepareStatement(sql);
			 rs=pstmt.executeQuery();
			 
			 while(rs.next()){
				 BankDTO dto=new BankDTO();
				 dto.setMemberId(rs.getString("member_id"));
				 dto.setMemberName(rs.getString("member_name"));
				 dto.setMemberAccount(rs.getString("member_account"));
				 dto.setMemberBalance(rs.getInt("member_balance"));
				 list.add(dto);
			 }
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 rs.close();
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 return list;
	 }
	 
	public ArrayList<BankDTO> select(String memberId) {
		 PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 ArrayList<BankDTO> list=new ArrayList<BankDTO>();
		  
		 try{
			 String sql="select * from bank_member_tb where member_id like ?";
			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, "%"+memberId+"%");
			 rs=pstmt.executeQuery();
			 
			 while(rs.next()){
				 BankDTO dto=new BankDTO();
				 dto.setMemberId(rs.getString("member_id"));
				 dto.setMemberName(rs.getString("member_name"));
				 dto.setMemberAccount(rs.getString("member_account"));
				 dto.setMemberBalance(rs.getInt("member_balance"));
				 list.add(dto);
			 }
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 rs.close();
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 return list;
	}

	public boolean updateDeposit(String memberId, String memberBalance) {
		 PreparedStatement pstmt=null;
		 Boolean result = false;
		  
		 try{
			 String sql="update bank_member_tb set member_balance = member_balance + ? where member_id = ?";
			 pstmt= con.prepareStatement(sql);
			 pstmt.setInt(1, Integer.parseInt(memberBalance));
			 pstmt.setString(2, memberId);
			 
			 int count = pstmt.executeUpdate();			 
			 if( count ==1 ) {
				 result = true;
			 }
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 
		 return result;
	}

	public boolean updateWithdraw(String memberId, String memberBalance) {
		 PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 Boolean result = false;
		  
		 try{
			 String sql="update bank_member_tb set member_balance = member_balance - ? where member_id = ?";
			 pstmt= con.prepareStatement(sql);
			 pstmt.setInt(1, Integer.parseInt(memberBalance));
			 pstmt.setString(2, memberId);
			 
			 int count = pstmt.executeUpdate();			 
			 if( count == 1 ) {
				 // 출금 시 잔액 부족할 경우 예외 처리
				 String sql1 = "select member_balance from bank_member_tb where member_id = ?";
				 PreparedStatement pstmt1 = con.prepareStatement(sql1);
				 pstmt1.setString(1, memberId);
				 rs = pstmt1.executeQuery();	
				 if(rs.next()) {
					 int member_balance = rs.getInt("member_balance");

					 if( member_balance < 0 ){
						 result = false;
					 } else { 
						 result = true;
					 }
				 }
				
				 try {
				 	 rs.close();
				 	 pstmt1.close();
				 } catch (Exception e) {
					 System.out.println(e);
				 }		
			}
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 
		 return result;
	}

	public boolean insertHistory(String memberId, String status, String memberBalance) {
		PreparedStatement pstmt=null;
		 Boolean result = false;
		  
		 try{
			 String sql="insert into bank_statement_tb (member_id, kind ,money, date) values (?, ?, ?, now())";
			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, memberId);
			 pstmt.setString(2, status);
			 pstmt.setInt(3, Integer.parseInt(memberBalance));
			 
			 int count = pstmt.executeUpdate();			 
			 if( count == 1 ) {
				 result = true;
			}
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 
		 return result;
	}

	public boolean insertTransferHistory(String sendId, String receiveId, String memberBalance) {
		PreparedStatement pstmt=null;
		 Boolean result = false;
		  
		 try{
			 String sql="insert into bank_transfer_history_tb (send_member_id, receive_member_id, transfer_money, date) values (?, ?, ?, now())";
			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, sendId);
			 pstmt.setString(2, receiveId);
			 pstmt.setInt(3, Integer.parseInt(memberBalance));
			 
			 int count = pstmt.executeUpdate();			 
			 if( count == 1 ) {
				 result = true;
			}
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 
		 return result;
	}

	public ArrayList<AccountDTO> selectAccount(String memberId) {
		PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 ArrayList<AccountDTO> list=new ArrayList<AccountDTO>();
		  
		 try{
			 String sql="select kind, member_id, money, date from bank_statement_tb where MEMBER_ID = ?";

			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, memberId);
			 rs=pstmt.executeQuery();
			 
			 while(rs.next()){
				 AccountDTO dto=new AccountDTO();
				 
				 System.out.println(rs.getString("kind"));
				 
				 if( rs.getString("kind").equals("deposit") ) {
					 dto.setState("입금");
				 }
				 if( rs.getString("kind").equals("withdraw") ) {
					 dto.setState("출금");
				 }

				 dto.setSendid(""); //본인
				 dto.setReceiveid(""); //본인
				 dto.setMoney(rs.getInt("money"));
				 dto.setDate(rs.getString("date"));
				 
				 list.add(dto);
			 }
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 rs.close();
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 return list;
	}
	
	public ArrayList<AccountDTO> selectAccountTransfer(String memberId) {
		PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 ArrayList<AccountDTO> list=new ArrayList<AccountDTO>();
		  
		 try{
			 String sql="select send_member_id, receive_member_id, transfer_money, date from bank_transfer_history_tb where SEND_MEMBER_ID = ? or RECEIVE_MEMBER_ID = ?";

			 pstmt= con.prepareStatement(sql);
			 pstmt.setString(1, memberId);
			 pstmt.setString(2, memberId);
			 rs=pstmt.executeQuery();
			 
			 while(rs.next()){
				 AccountDTO dto=new AccountDTO();
				 
				 System.out.println(rs.getString("send_member_id"));
				 System.out.println(memberId);
				 
				 // 당사자가 보냈을 때
				 if( rs.getString("send_member_id").equals(memberId) ) {
					 dto.setState("이체입금");
					 dto.setSendid(memberId); //보낸사람
					 dto.setReceiveid(rs.getString("receive_member_id"));
				 }
				 // 당사자가 받았을 때
				 if( rs.getString("receive_member_id").equals(memberId) ) { 
					 dto.setState("이체출금");
					 dto.setSendid(rs.getString("send_member_id")); 
					 dto.setReceiveid(memberId); //받은사람
				 }

				 dto.setMoney(rs.getInt("transfer_money"));
				 dto.setDate(rs.getString("date"));

				 list.add(dto);
			 }
		 }catch(Exception e){
			 System.out.println(e);
		 }finally{
			 try {
				 rs.close();
				 pstmt.close();
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 return list;
	}

}
