package com.kopo.project1;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 사용자 관리 및 웹 페이지 요청을 처리하는 메인 컨트롤러 클래스.
 * Spring MVC의 @Controller로 등록되어 있으며, 웹의 모든 요청에 대한 진입점 역할을 합니다.
 * 사용자 회원가입, 로그인, 로그아웃, 회원 목록 조회, 정보 수정 및 마이페이지 관련 기능을 담당합니다.
 */
@Controller
public class HomeController {
	
	/**
	 * 메인 페이지를 표시합니다.
	 * 서버 시작 시 'user' 테이블이 없으면 생성하며, 세션 정보를 기반으로 로그인 상태를 확인합니다.
	 * 전체 사용자 수와 오늘 가입한 사용자 수를 DB에서 조회하여 모델에 추가합니다.
	 * * @param locale 클라이언트의 지역 정보
	 * @param model 뷰에 데이터를 전달하기 위한 모델 객체
	 * @param session 현재 사용자의 세션 객체
	 * @return "index" 뷰 이름
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpSession session) {
		DB db = new DB();
		if (!db.isTableExists("user")) {
			db.createTable();
		}

		// 세션에서 로그인 정보 받아서 모델에 전달
		Boolean isLogin = (Boolean)session.getAttribute("is_login");
		String userId = (String)session.getAttribute("user_id");
		model.addAttribute("isLogin", isLogin != null ? isLogin : false);
		model.addAttribute("userId", userId);

		// DB에서 통계 정보 가져오기
		int totalUsers = db.getTotalUserCount();
		int todayUsers = db.getTodayUserCount();

		// 모델에 통계 정보 추가
		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("todayUsers", todayUsers);

		return "index";
	}
	
	/**
	 * 회원가입 페이지(join.jsp)를 표시합니다.
	 * * @param locale 클라이언트의 지역 정보
	 * @param model 뷰에 데이터를 전달하기 위한 모델 객체
	 * @return "join" 뷰 이름
	 */
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Locale locale, Model model) {
		return "join";
	}

	/**
	 * 회원가입 폼에서 전송된 데이터를 처리합니다.
	 * 전송된 사용자 정보를 DB에 삽입하고 메인 페이지로 리다이렉트합니다.
	 * * @param locale 클라이언트의 지역 정보
	 * @param model 뷰에 데이터를 전달하기 위한 모델 객체
	 * @param request HTTP 요청 객체 (사용자 입력 파라미터 포함)
	 * @return "redirect:/" 메인 페이지로의 리다이렉트 경로
	 */
	@RequestMapping(value = "/join_action", method = RequestMethod.POST)
	public String joinAction(Locale locale, Model model
			, HttpServletRequest request
			) {
		DB db = new DB();
		
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String userType = request.getParameter("userType");
		
		// 관리자 선택 시 비밀번호 확인
		if ("admin".equals(userType)) {
			String adminPassword = request.getParameter("adminPassword");
			if (!"admin1234".equals(adminPassword)) { // 관리자 등록을 위한 비밀번호
				return "redirect:/join?error=admin";
			}
		}
		
		db.insertData(new User(id, pwd, name, phone, address), userType);
		return "redirect:/";
	}

	/**
	 * 로그인 페이지(login.jsp)를 표시합니다.
	 * * @param locale 클라이언트의 지역 정보
	 * @param model 뷰에 데이터를 전달하기 위한 모델 객체
	 * @return "login" 뷰 이름
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		return "login";
	}

	/**
	 * 로그인 폼에서 전송된 데이터를 처리합니다.
	 * DB에서 사용자 정보를 확인하여 로그인 성공 시 세션에 정보를 저장하고, 실패 시 에러 파라미터와 함께 로그인 페이지로 리다이렉트합니다.
	 * * @param locale 클라이언트의 지역 정보
	 * @param model 뷰에 데이터를 전달하기 위한 모델 객체
	 * @param request HTTP 요청 객체 (id, pwd 파라미터 포함)
	 * @param session 로그인 정보를 저장할 세션 객체
	 * @return 로그인 성공 시 "redirect:/", 실패 시 "redirect:/login?error=true"
	 */
	@RequestMapping(value = "/login_action", method = RequestMethod.POST)
	public String loginAction(Locale locale, Model model,
			HttpServletRequest request, HttpSession session) {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		DB db = new DB();
		User loggedUser = db.login(new User(id, pwd));

		if (loggedUser != null) {
			// --- 로그인 성공 시 ---
			session.setAttribute("is_login", true);
			session.setAttribute("user_type", loggedUser.userType);
			session.setAttribute("user_id", loggedUser.id);
			
			return "redirect:/";
			
		} else {
			// --- 로그인 실패 시 ---
			return "redirect:/login?error=true";
		}
	}

	/**
	 * 전체 회원 목록을 조회하여 user_list.jsp에 표시합니다. (관리자 기능)
	 * * @param model 뷰에 회원 목록 데이터를 전달하기 위한 모델 객체
	 * @return "user_list" 뷰 이름
	 */
	@RequestMapping(value="/user_list", method=RequestMethod.GET)
	public String userList(Model model, HttpSession session) {
		// 세션에서 사용자 타입 확인
		String userType = (String)session.getAttribute("user_type");
		
		// 관리자가 아닌 경우 메인 페이지로 리다이렉트
		if (userType == null || !userType.equals("admin")) {
			return "redirect:/";
		}
		
		DB db = new DB();
		ArrayList<User> userList = db.selectAll();
		model.addAttribute("userList", userList);
		return "user_list";
	}
	
	/**
	 * 현재 사용자를 로그아웃 처리합니다.
	 * 세션을 무효화하고 메인 페이지로 리다이렉트합니다.
	 * * @param session 무효화할 세션 객체
	 * @return "redirect:/" 메인 페이지로의 리다이렉트 경로
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/";
	}

	/**
	 * 특정 회원을 삭제합니다. (관리자 기능)
	 * idx에 해당하는 사용자를 DB에서 삭제하고 회원 목록 페이지로 리다이렉트합니다.
	 * * @param idx 삭제할 사용자의 고유 번호(idx)
	 * @return "redirect:/user_list" 회원 목록 페이지로의 리다이렉트 경로
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteUser(@RequestParam("idx") int idx) {
		DB db = new DB();
		db.deleteUser(idx);
		return "redirect:/user_list";
	}
	
	/**
	 * 특정 회원의 정보 수정 페이지(user_edit.jsp)를 표시합니다. (관리자 기능)
	 * idx에 해당하는 사용자 정보를 DB에서 조회하여 모델에 담아 전달합니다.
	 * * @param idx 수정할 사용자의 고유 번호(idx)
	 * @param model 뷰에 사용자 정보를 전달하기 위한 모델 객체
	 * @return "user_edit" 뷰 이름. 사용자가 없으면 "redirect:/user_list"
	 */
	@RequestMapping(value = "/user_edit", method = RequestMethod.GET)
	public String userEdit(@RequestParam("idx") int idx, Model model) {
		DB db = new DB();
		User user = db.getUserByIdx(idx);
		if (user == null) {
			return "redirect:/user_list";
		}
		model.addAttribute("user", user);
		return "user_edit";
	}
	
	/**
	 * 회원 정보 수정 폼에서 전송된 데이터를 처리합니다. (관리자 기능)
	 * idx에 해당하는 사용자의 정보를 DB에서 업데이트하고 회원 목록 페이지로 리다이렉트합니다.
	 * * @param idx 수정될 사용자의 고유 번호(idx)
	 * @param name 수정할 이름
	 * @param phone 수정할 연락처
	 * @param address 수정할 주소
	 * @return "redirect:/user_list" 회원 목록 페이지로의 리다이렉트 경로
	 */
	@RequestMapping(value = "/user_edit_action", method = RequestMethod.POST)
	public String userEditAction(
			@RequestParam("idx") int idx,
			@RequestParam("name") String name,
			@RequestParam("phone") String phone,
			@RequestParam("address") String address) {
		DB db = new DB();
		db.updateUser(idx, name, phone, address);
		return "redirect:/user_list";
	}

	/**
	 * 현재 로그인된 사용자의 마이페이지(mypage.jsp)를 표시합니다.
	 * 세션에서 사용자 ID를 가져와 DB에서 해당 사용자의 모든 정보를 조회하여 모델에 담아 전달합니다.
	 * * @param session 현재 사용자의 세션 객체
	 * @param model 뷰에 사용자 정보를 전달하기 위한 모델 객체
	 * @return "mypage" 뷰 이름. 비로그인 시 "redirect:/login"
	 */
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String mypage(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("user_id");

		if (userId == null) {
			return "redirect:/login";
		}

		DB db = new DB();
		User user = db.selectOne(userId);	

		model.addAttribute("user", user);
		
		return "mypage";
	}

	/**
	 * 마이페이지 수정 폼에서 전송된 데이터를 처리합니다.
	 * 세션에서 사용자 ID를 확인하여 DB에 해당 사용자의 정보를 업데이트하고 마이페이지로 다시 리다이렉트합니다.
	 * * @param session 현재 사용자의 세션 객체
	 * @param name 수정할 이름
	 * @param phone 수정할 연락처
	 * @param address 수정할 주소
	 * @return "redirect:/" 마이페이지로의 리다이렉트 경로. 비로그인 시 "redirect:/login"
	 */
	@RequestMapping(value = "/mypage_action", method = RequestMethod.POST)
	public String mypageAction(HttpSession session,
			@RequestParam("name") String name,
			@RequestParam("phone") String phone,
			@RequestParam("address") String address) {
		
		String userId = (String) session.getAttribute("user_id");

		if (userId == null) {
			return "redirect:/login";
		}

		DB db = new DB();
		db.updateMyInfo(userId, name, phone, address);	

		return "redirect:/";
	}
}