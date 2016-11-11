package com.cjon.bank.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjon.bank.dto.AccountDTO;
import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.service.BankCheckMemberService;
import com.cjon.bank.service.BankDepositService;
import com.cjon.bank.service.BankSearchMemberService;
import com.cjon.bank.service.BankSelectAllMemberService;
import com.cjon.bank.service.BankService;
import com.cjon.bank.service.BankTransferService;
import com.cjon.bank.service.BankWithdrawService;

@Controller
public class BankController {
	
	private DataSource dataSource;
	private BankService service;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@RequestMapping(value = "/selectAllMember")	
	public void selectAllMember(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String callback = request.getParameter("callback");
		
		service = new BankSelectAllMemberService();
		model.addAttribute("dataSource", dataSource);
		service.execute(model);
		
		// 결과를 model에 넣은 것은 꺼냄
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT");
		
		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);
			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	@RequestMapping(value = "/searchMember")	
	public void searchMember(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String callback = request.getParameter("callback");
		String memberId = request.getParameter("memberId");
				
		service = new BankSearchMemberService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("memberId", memberId); // 입력받은 아이디 넘김
		service.execute(model);
		
		// 결과를 model에 넣은 것은 꺼냄
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT");
		
		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);
			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	@RequestMapping(value = "/deposit")	
	public void deposit(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String callback = request.getParameter("callback");
		
		service = new BankDepositService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request); // 그냥 request 객체 통째로 넘김 :)
		service.execute(model);
		
		// 결과 처리
		Boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.println(callback + "(" + result + ")");
				out.flush();
				out.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}	
	}
	
	@RequestMapping(value = "/withdraw")	
	public void withdraw(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String callback = request.getParameter("callback");
		
		service = new BankWithdrawService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request); // 그냥 request 객체 통째로 넘김 :)
		service.execute(model);
		
		// 결과 처리
		Boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.println(callback + "(" + result + ")");
				out.flush();
				out.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}			
	}
	
	@RequestMapping(value = "/transfer")	
	public void transfer(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String callback = request.getParameter("callback");
		
		service = new BankTransferService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request); // 그냥 request 객체 통째로 넘김 :)
		service.execute(model);
		
		// 결과 처리
		Boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.println(callback + "(" + result + ")");
				out.flush();
				out.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}			
	}
	
	@RequestMapping(value = "/checkMember")	
	public void checkMember(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String callback = request.getParameter("callback");
		String checkId = request.getParameter("id");
				
		service = new BankCheckMemberService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("checkId", checkId); // 입력받은 아이디 넘김
		service.execute(model);
		
		// 결과를 model에 넣은 것은 꺼냄
		ArrayList<AccountDTO> list = (ArrayList<AccountDTO>) model.asMap().get("RESULT");
		
		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);
			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
	}
	
}
