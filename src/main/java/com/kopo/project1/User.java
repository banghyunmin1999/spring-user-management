package com.kopo.project1;

/**
 * 사용자 정보를 저장하고 전송하는 데 사용되는 데이터 객체(DTO) 클래스.
 * 데이터베이스의 'user' 테이블과 일대일로 매핑되며, 애플리케이션의 여러 계층 간에 사용자 데이터를 전달하는 역할을 합니다.
 *
 * @author [작성자 이름]
 * @version 1.0
 * @since 2025. 06. 13.
 */
public class User {
	/** 사용자의 고유 번호 (Primary Key) */
	int idx;
	/** 사용자의 아이디 */
	String id;
	/** 사용자의 비밀번호 (DB에서는 해시된 값) */
	String pwd;
	/** 사용자의 유형 (예: admin, guest) */
	String userType;
	/** 사용자의 이름 */
	String name;
	/** 사용자의 연락처 */
	String phone;
	/** 사용자의 주소 */
	String address;
	/** 계정 생성일 */
	String created;
	/** 마지막 정보 수정일 */
	String lastUpdated;
	
	/**
	 * 기본 생성자.
	 */
	User(){
	}
	
	/**
	 * 모든 필드를 초기화하는 생성자.
	 * DB에서 사용자 정보를 조회하여 객체를 생성할 때 주로 사용됩니다.
	 * @param idx 사용자 고유 번호
	 * @param userType 사용자 유형
	 * @param id 사용자 아이디
	 * @param pwd 사용자 비밀번호 (해시값)
	 * @param name 사용자 이름
	 * @param phone 사용자 연락처
	 * @param address 사용자 주소
	 * @param created 계정 생성일
	 * @param lastUpdated 마지막 정보 수정일
	 */
	User(int idx, String userType, String id, String pwd, String name, String phone, String address, String created, String lastUpdated) {
		this.idx = idx;
		this.userType = userType;
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.created = created;
		this.lastUpdated = lastUpdated;
	}
	
	/**
	 * 로그인 시 아이디와 비밀번호를 전달하기 위한 생성자.
	 * @param id 사용자 아이디
	 * @param pwd 사용자가 입력한 비밀번호 (해싱 전)
	 */
	User(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}

	/**
	 * 회원가입 시 사용자 정보를 전달하기 위한 생성자.
	 * @param id 사용자 아이디
	 * @param pwd 사용자 비밀번호
	 * @param name 사용자 이름
	 * @param phone 사용자 연락처
	 * @param address 사용자 주소
	 */
	User(String id, String pwd, String name, String phone, String address) {
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}
	
	/**
	 * 사용자의 고유 번호(idx)를 반환합니다.
	 * @return 사용자 고유 번호
	 */
	public int getIdx() {
		return this.idx;
	}
	
	/**
	 * 사용자의 유형(userType)을 반환합니다.
	 * @return 사용자 유형
	 */
	public String getUserType() {
		return this.userType;
	}
	
	/**
	 * 사용자의 아이디(id)를 반환합니다.
	 * @return 사용자 아이디
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 사용자의 비밀번호(pwd)를 반환합니다.
	 * @return 사용자 비밀번호 (해시값)
	 * 해시된 비밀번호가 유출될 가능성이 있어 주석처리, 상위 계층에서는 해시값만 비교하기 때문에 이제 필요 없음
	 */
//	public String getPwd() {
//		return this.pwd;
//	}

	/**
	 * 사용자의 이름(name)을 반환합니다.
	 * @return 사용자 이름
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 사용자의 연락처(phone)를 반환합니다.
	 * @return 사용자 연락처
	 */
	public String getPhone() {
		return this.phone;
	}
	
	/**
	 * 사용자의 주소(address)를 반환합니다.
	 * @return 사용자 주소
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * 계정 생성일(created)을 반환합니다.
	 * @return 계정 생성일
	 */
	public String getCreated() {
		return this.created;
	}
	
	/**
	 * 마지막 정보 수정일(lastUpdated)을 반환합니다.
	 * @return 마지막 정보 수정일
	 */
	public String getLastUpdated() {
		return this.lastUpdated;
	}
	
}