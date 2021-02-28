package ru.mikhailkhr.ConverterApp.JDBC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mikhailkhr.ConverterApp.entity.Valute;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Valute calss that interact with database
 *
 * @author mikhailkhr
 */
@Repository
public class ValuteJdbcDao
{

	private final String SELECT_VALUTES = "SELECT val.numcode, val.charcode, vali.info as name, vali.locale FROM converterappschema.valute as val\n " + "INNER JOIN converterappschema.valute_info as vali\n " + "ON val.numcode = vali.valute_numcode\n " + "WHERE vali.locale = ?;";

	// Standard sql database formatter
	DateTimeFormatter postresFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * Select all valutes from database by locale
	 *
	 * @param locale {@code Locale}
	 * @return list of found valutes
	 */
	public List<Valute> getAllValuteByLocale(Locale locale)
	{

		String localeTag = locale.toLanguageTag();
		String localeRegex = "(\\w\\w).*";
		Pattern pattern = Pattern.compile(localeRegex);
		Matcher match = pattern.matcher(localeTag);
		match.find();
		String language = match.group(1);

		List<Valute> valutes = getAllValuteByLocale(language);
		return valutes;
	}

	/**
	 * Select all valutes from database by locale
	 *
	 * @param locale {@code Locale}
	 * @return list of found valutes
	 */
	public List<Valute> getAllValuteByLocale(String locale)
	{

		// Processing from SQL Injection
		PreparedStatementCreator selectValutesPSCreator = new PreparedStatementCreator()
		{

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException
			{
				PreparedStatement ps = con.prepareStatement(SELECT_VALUTES, Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, locale);
				return ps;
			}
		};

		RowMapper<Valute> selectValutesRowMapper = new RowMapper<Valute>()
		{
			@Override
			public Valute mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Valute valute = new Valute();
				valute.setName(rs.getString(3));
				valute.setCharCode(rs.getString(2));
				valute.setNumCode(rs.getInt(1));
				return valute;
			}

		};

		List<Valute> valutes = jdbcTemplate.query(selectValutesPSCreator, selectValutesRowMapper);

		if(valutes.size() == 0){
			valutes = getAllValuteByLocale("def");
		}
		return valutes;
	}
}
