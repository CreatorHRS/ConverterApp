package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.mikhailkhr.ConverterApp.entity.ConverterAppUser;

/**
 * User class that interact with database 
 * @author mikhailkhr
 *
 */
@Repository
public class AppUserJdbcDao {
	private final String SELECT_USER_BY_EMAIL = "SELECT * FROM my_schema.ConverterAppUser where usermail = ?";
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * Get user by email
	 * @param mail {@code String}
	 * @return ConverterAppUser
	 */
	public ConverterAppUser getUserByMail(String mail) {
		PreparedStatementCreator selectUserPSCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_USER_BY_EMAIL);
				ps.setString(1, mail);
				return ps;
			}
		};
		
		RowMapper<ConverterAppUser> selectUserRowMapper = new RowMapper<ConverterAppUser>(){

			@Override
			public ConverterAppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConverterAppUser user = new ConverterAppUser();
				user.setId(rs.getString("id"));
				user.setMail(rs.getString("usermail"));
				user.setName(rs.getString("username"));
				user.setPass(rs.getString("userpass"));
				return user;
			}
			
		};
		
		List<ConverterAppUser> users = jdbcTemplate.query(selectUserPSCreator, selectUserRowMapper);
		return users.get(0);
	}
}
