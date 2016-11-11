package com.cjon.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;

public class BankTransferService implements BankService {

	@Override
	public void execute(Model model) {

		// 입력값 가져오기
		HttpServletRequest request = (HttpServletRequest) model.asMap().get("request");
		String sendId = request.getParameter("sendId");
		String receiveId = request.getParameter("receiveId");
		String memberBalance = request.getParameter("memberBalance");

		DataSource dataSource = (DataSource) model.asMap().get("dataSource");
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			BankDAO dao = new BankDAO(con);
			boolean result1 = dao.updateWithdraw(sendId, memberBalance);   //출금
			boolean result2 = dao.updateDeposit(receiveId, memberBalance); //입금
			boolean result = false;	// 두 함수 모두 성공하면 true
			
			if( result1 && result2 ) {
				// 이체에 성공했을 경우 커밋 후, 출금 history에 작성
				con.commit();
				result = true;
				boolean result3 = dao.insertTransferHistory(sendId, receiveId, memberBalance);
				if( result3 ) {
					con.commit();
				} else {
					con.rollback();
				}
			} else {
				con.rollback();
			}

			model.addAttribute("RESULT", result);
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

}
