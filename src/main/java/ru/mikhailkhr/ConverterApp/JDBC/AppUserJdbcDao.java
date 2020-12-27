package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	private final String SELECT_USER_BY_EMAIL = "SELECT * FROM my_schema.ConverterAppUser where usermail = '%s'";
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * Get user by email
	 * @param mail {@code String}
	 * @return ConverterAppUser
	 */
	public ConverterAppUser getUserByMail(String mail) {
		List<ConverterAppUser> users = jdbcTemplate.query(String.format(SELECT_USER_BY_EMAIL, mail), new RowMapper<ConverterAppUser>(){

			@Override
			public ConverterAppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConverterAppUser cau = new ConverterAppUser();
				cau.setId(rs.getString("id"));
				cau.setMail(rs.getString("usermail"));
				cau.setName(rs.getString("username"));
				cau.setPass(rs.getString("userpass"));
				
				return cau;
			}
			
		});
		return users.get(0);
	}
}
