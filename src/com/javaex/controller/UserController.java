package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//action을 꺼낸다
		String action = request.getParameter("action");
		
		if("joinForm".equals(action)) {//회원가입폼
			System.out.println("UserController>joinForm");
			//회원가입 폼 포워드
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinForm.jsp");
		
		}else if("join".equals(action)) {//회원가입
			System.out.println("UserController>join");
			
			//파라미터 꺼내기*4
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			//Vo만들기
			UserVo userVo = new UserVo(id, name, password, gender);
			System.out.println(userVo);
			
			//Dao를 이용해서 저장하기
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			//포워드
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinOk.jsp");
		
		}else if("loginForm".equals(action)) {//로그인 폼
			System.out.println("UserController>loginForm");
		
			//포워드
			WebUtil.forword(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		}else if("login".equals(action)) {//로그인
			System.out.println("UserController>login");
			
			//파라미터 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			//Vo만들기
			UserVo userVo = new UserVo();
			userVo.setId(id);
			userVo.setPassword(password);
			
			//dao
			UserDao userDao = new UserDao();
			UserVo authUser =  userDao.getUser(userVo); //id, password --> user 전체
			
			//authUser 주소값이 있으면 --> 로그인 성공
			//authUSer null이면 --> 로그인 실패
			if(authUser == null) {
				System.out.println("로그인 실패");
			}else {
				System.out.println("로그인 성공");
				
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authUser);
				
				//메인 리다이렉트
				WebUtil.redirect(request, response, "/mysite2/main");
				
			}
			
		}else if("logout".equals(action)) { //로그아웃
			System.out.println("UserController>logout");
			
			//세션값을 지운다
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			//메인으로 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/main");
		
		}else if("modifyForm".equals(action)) { //수정폼
			System.out.println("UserController>modifyForm");
		
			//로그인한 사용자의  no 값을 세션에서 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int no = authUser.getNo();
			
			//no 로 사용자 정보 가져오기
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.getUser(no);  //no id password name gender
			
			//request 의 attribute 에 userVo 는 넣어서 포워딩
			request.setAttribute("userVo", userVo);
			WebUtil.forword(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}