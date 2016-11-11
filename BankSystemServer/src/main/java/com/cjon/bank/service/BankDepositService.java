package com.cjon.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;

public class BankDepositService implements BankService {

	@Override
	public void execute(Model model) {

		// 입력값 가져오기
		HttpServletRequest request = (HttpServletRequest) model.asMap().get("request");
		String memberId = request.getParameter("memberId");
		String memberBalance = request.getParameter("memberBalance");

		DataSource dataSource = (DataSource) model.asMap().get("dataSource");
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			BankDAO dao = new BankDAO(con);
			boolean result = dao.updateDeposit(memberId, memberBalance);
			
			if( result ) {
				// 입금에 성공했을 경우 커밋 후, 입금 history에 작성
				con.commit();				
				boolean result2 = dao.insertHistory(memberId, "deposit" ,memberBalance);
				if( result2 ) {
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
