package com.cjon.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;

public class BankSearchMemberService implements BankService {

	@Override
	public void execute(Model model) {

		// 입력값 가져오기
		String memberId = (String) model.asMap().get("memberId");
		
		DataSource dataSource = (DataSource) model.asMap().get("dataSource");
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			BankDAO dao = new BankDAO(con);
			ArrayList<BankDTO> list = dao.select(memberId);
			
			if( list != null ) {
				con.commit();
			} else {
				con.rollback();
			}

			model.addAttribute("RESULT", list);
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
