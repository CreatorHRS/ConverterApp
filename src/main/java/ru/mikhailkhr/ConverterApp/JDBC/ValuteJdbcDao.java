package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.mikhailkhr.ConverterApp.entity.Valute;

/**
 * Valute calss that interact with database
 * @author mikhailkhr
 *
 */
@Repository
public class ValuteJdbcDao {

	private final String SELECT_VALUTES = "SELECT val.numcode, val.charcode, vali.info as name, vali.locale FROM converterappschema.valute as val\n "
										+ "INNER JOIN converterappschema.valute_info as vali\n "
										+ "ON val.numcode = vali.valute_numcode\n "
										+ "WHERE vali.locale = ?;";
	
	// Standard sql database formatter
	DateTimeFormatter postresFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * Select all valutes from database by date
	 * @param date {@code LocalDate}
	 * @return list of found valutes
	 */
	public List<Valute> getAllValuteByLocale(Locale locale) {
		
		// Processing from SQL Injection 
		PreparedStatementCreator selectValutesPSCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_VALUTES, Statement.RETURN_GENERATED_KEYS);
				
				String localeTag = locale.toLanguageTag();
				System.out.println("getLanguage = " + locale.getLanguage());
				System.out.println("getDisplayLanguage = " + locale.getDisplayLanguage());
				String localeRegex = "(\\w\\w).*";
				Pattern pattern = Pattern.compile(localeRegex);
				Matcher match = pattern.matcher(localeTag);
				match.find();
				String language = match.group(1);
				ps.setString(1, language);
				return ps;
			}
		};
		
		RowMapper<Valute> selectValutesRowMapper = new RowMapper<Valute>() {
			@Override
			public Valute mapRow(ResultSet rs, int rowNum) throws SQLException {
				Valute valute = new Valute();
				valute.setName(rs.getString(3));
				valute.setCharCode(rs.getString(2));
				valute.setNumCode(rs.getInt(1));
				return valute;
			}

		};
		
		List<Valute> valutes = jdbcTemplate.query(selectValutesPSCreator, selectValutesRowMapper);
		return valutes;
	}
}
