package com.kopo.project1;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

/**
 * SQLite 데이터베이스와의 모든 상호작용을 관리하는 데이터 접근 객체(DAO) 클래스.
 * 데이터베이스 연결, 테이블 생성, 사용자 CRUD(생성, 읽기, 수정, 삭제) 및
 * 기타 데이터 관련 작업을 처리하는 메소드를 제공합니다.
 */
public class DB {
	private Connection connection;

	/**
	 * 클래스가 메모리에 로드될 때 SQLite JDBC 드라이버를 로드합니다.
	 */
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * SQLite 데이터베이스 파일에 대한 연결을 엽니다.
	 */
	private void open() {
		try {
			String dbFileName = "T:/저장소/sqliteDB/project1.sqlite";
			SQLiteConfig config = new SQLiteConfig();
			this.connection = DriverManager.getConnection("jdbc:sqlite:/" + dbFileName, config.toProperties());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 현재 열려있는 데이터베이스 연결을 닫습니다.
	 */
	private void close() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 지정된 이름의 테이블이 데이터베이스에 존재하는지 확인합니다.
	 *
	 * @param tableName 확인할 테이블의 이름
	 * @return 테이블이 존재하면 true, 그렇지 않으면 false
	 */
	public boolean isTableExists(String tableName) {
		this.open();
		boolean exists = false;
		String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
		try (Statement statement = this.connection.createStatement();
			 ResultSet rs = statement.executeQuery(query)) {
			if (rs.next()) {
				exists = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return exists;
	}

	/**
	 * 사용자 정보를 저장할 'user' 테이블을 생성합니다.
	 * 테이블이 이미 존재하면 예외가 발생할 수 있습니다.
	 */
	public void createTable() {
		this.open();
		String query = "CREATE TABLE user (idx INTEGER PRIMARY KEY AUTOINCREMENT, user_type TEXT, id TEXT, pwd TEXT, name TEXT, phone TEXT, address TEXT, created TEXT, last_updated TEXT);";
		try (Statement statement = this.connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
	}
	
	/**
	 * 주어진 문자열을 SHA-512 알고리즘으로 해싱하여 반환합니다.
	 *
	 * @param input 해싱할 원본 문자열
	 * @return SHA-512로 해싱된 16진수 문자열
	 * @throws RuntimeException SHA-512 알고리즘을 사용할 수 없는 경우
	 */
	public static String sha512(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-512 알고리즘을 사용할 수 없습니다.", e);
		}
	}
	
	/**
	 * 새로운 사용자 정보를 데이터베이스에 삽입합니다.
	 * 이 과정에서 비밀번호는 SHA-512로 자동 해싱됩니다.
	 *
	 * @param user 삽입할 사용자 정보가 담긴 User 객체
	 */
	public void insertData(User user) {
		this.open();
		String query = "INSERT INTO user (user_type, id, pwd, name, phone, address, created, last_updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setString(1, "guest");
			statement.setString(2, user.id);
			statement.setString(3, sha512(user.pwd)); // 비밀번호 해싱
			statement.setString(4, user.name);
			statement.setString(5, user.phone);
			statement.setString(6, user.address);
			String now = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new java.util.Date());
			statement.setString(7, now);
			statement.setString(8, now);
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
	}
	
	/**
	 * 데이터베이스에 있는 모든 사용자 정보를 조회합니다.
	 *
	 * @return 모든 사용자 정보가 담긴 ArrayList
	 */
	public ArrayList<User> selectAll() {
		this.open();
		ArrayList<User> data = new ArrayList<>();
		String query = "SELECT * FROM user";
		try (PreparedStatement statement = this.connection.prepareStatement(query);
			 ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				data.add(new User(
					result.getInt("idx"),
					result.getString("user_type"),
					result.getString("id"),
					result.getString("pwd"),
					result.getString("name"),
					result.getString("phone"),
					result.getString("address"),
					result.getString("created"),
					result.getString("last_updated")
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return data;
	}

	/**
	 * 사용자의 아이디와 비밀번호를 검증하여 로그인을 처리합니다.
	 * 입력된 비밀번호를 해싱하여 DB에 저장된 해시값과 비교합니다.
	 *
	 * @param user 로그인 시도하는 사용자의 id와 암호화되지 않은 pwd가 담긴 User 객체
	 * @return 로그인 성공 시 해당 사용자의 정보를 담은 User 객체, 실패 시 null
	 */
	public User login(User user) {
		this.open();
		User returnData = null;
		String query = "SELECT * FROM user WHERE id=? AND pwd=?";
		try (PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setString(1, user.id);
			statement.setString(2, sha512(user.pwd)); // 입력된 비밀번호를 해싱하여 비교
			
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					returnData = new User(
						result.getInt("idx"),
						result.getString("user_type"),
						result.getString("id"),
						"", // 비밀번호는 보안상 반환하지 않음
						result.getString("name"),
						result.getString("phone"),
						result.getString("address"),
						result.getString("created"),
						result.getString("last_updated")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return returnData;
	}
	
	/**
	 * 고유 번호(idx)를 사용하여 특정 사용자 정보를 조회합니다.
	 *
	 * @param idx 조회할 사용자의 고유 번호
	 * @return 해당 사용자의 정보를 담은 User 객체, 사용자가 없으면 null
	 */
	public User getUserByIdx(int idx) {
		this.open();
		User user = null;
		String sql = "SELECT * FROM user WHERE idx = ?";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
			pstmt.setInt(1, idx);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					user = new User(
						rs.getInt("idx"),
						rs.getString("user_type"),
						rs.getString("id"),
						rs.getString("pwd"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getString("address"),
						rs.getString("created"),
						rs.getString("last_updated")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return user;
	}
	
	/**
	 * 고유 번호(idx)를 사용하여 특정 사용자를 삭제합니다.
	 *
	 * @param idx 삭제할 사용자의 고유 번호
	 * @return 삭제 성공 시 true, 실패 시 false
	 */
	public boolean deleteUser(int idx) {
		this.open();
		boolean result = false;
		String sql = "DELETE FROM user WHERE idx = ?";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
			pstmt.setInt(1, idx);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return result;
	}

	/**
	 * 특정 사용자(idx 기준)의 이름, 연락처, 주소를 수정합니다. (관리자용)
	 *
	 * @param idx 수정할 사용자의 고유 번호
	 * @param name 새로운 이름
	 * @param phone 새로운 연락처
	 * @param address 새로운 주소
	 */
	public void updateUser(int idx, String name, String phone, String address) {
		this.open();
		String sql = "UPDATE user SET name=?, phone=?, address=?, last_updated=? WHERE idx=?";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, address);
			String now = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new java.util.Date());
			pstmt.setString(4, now);
			pstmt.setInt(5, idx);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
	}
	
	/**
	 * 특정 사용자(id 기준)의 이름, 연락처, 주소를 수정합니다. (마이페이지용)
	 *
	 * @param userId 수정할 사용자의 아이디
	 * @param name 새로운 이름
	 * @param phone 새로운 연락처
	 * @param address 새로운 주소
	 */
	public void updateMyInfo(String userId, String name, String phone, String address) {
		this.open();
		String sql = "UPDATE user SET name = ?, phone = ?, address = ?, last_updated = datetime('now', 'localtime') WHERE id = ?";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, address);
			pstmt.setString(4, userId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
	}

	/**
	 * 사용자 아이디(id)를 사용하여 특정 사용자 정보를 조회합니다.
	 *
	 * @param userId 조회할 사용자의 아이디
	 * @return 해당 사용자의 정보를 담은 User 객체, 사용자가 없으면 null
	 */
	public User selectOne(String userId) {
		this.open();
		User user = null;
		String sql = "SELECT * FROM user WHERE id = ?";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					user = new User(
						rs.getInt("idx"),
						rs.getString("user_type"),
						rs.getString("id"),
						rs.getString("pwd"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getString("address"),
						rs.getString("created"),
						rs.getString("last_updated")
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return user;
	}
	
	/**
	 * 데이터베이스에 저장된 총 회원 수를 조회합니다.
	 *
	 * @return 총 회원 수
	 */
	public int getTotalUserCount() {
		this.open();
		int count = 0;
		String sql = "SELECT COUNT(*) FROM user";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return count;
	}

	/**
	 * 오늘 날짜에 가입한 총 회원 수를 조회합니다.
	 *
	 * @return 오늘 가입한 회원 수
	 */
	public int getTodayUserCount() {
		this.open();
		int count = 0;
		String sql = "SELECT COUNT(*) FROM user WHERE DATE(created) = DATE('now', 'localtime')";
		try (PreparedStatement pstmt = this.connection.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
		return count;
	}
}