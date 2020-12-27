package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.mikhailkhr.ConverterApp.entity.Valute;

/**
 * Vlute calss that interact with database
 * @author mikhailkhr
 *
 */
@Repository
public class ValuteJdbcDao {

	private final String INSERT_SQL = "INSERT INTO my_schema.valute (name, numCode, charCode, valute_value, nominal, date) VALUES (?, ?, ?, ?, ?, ?);";
	private final String SELECT_ALL_VALUTES_BY_DATE = "SELECT * FROM my_schema.valute WHERE date = '%s'";
	/*
	 * Standard sql database formatter
	 */
	DateTimeFormatter postresFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * Put all valutes in list to the database
	 * @param list {@code List<Valute>}
	 */
	public void putAllValute(List<Valute> list) {

		jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				Valute valute = list.get(i);
				ps.setString(1, valute.getName());
				ps.setString(2, valute.getNumCode());
				ps.setString(3, valute.getCharCode());
				ps.setDouble(4, valute.getValue());
				ps.setInt(5, valute.getNominal());
				ps.setDate(6, Date.valueOf(valute.getDate()));
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return list.size();
			}
		});
		

	}
	/**
	 * Select all valutes from database by date
	 * @param date {@code LocalDate}
	 * @return list of found valutes
	 */
	public List<Valute> getAllValuteByDate(LocalDate date) {
		String dateStr = date.format(postresFormatter);
		List<Valute> valutes = jdbcTemplate.query(
				String.format(SELECT_ALL_VALUTES_BY_DATE, date.format(postresFormatter)), new RowMapper<Valute>() {

					@Override
					public Valute mapRow(ResultSet rs, int rowNum) throws SQLException {
						Valute valute = new Valute();
						valute.setId(String.valueOf(rs.getInt("id")));
						valute.setName(rs.getString("name"));
						valute.setCharCode(rs.getString("charcode"));
						valute.setNumCode(rs.getString("numcode"));
						valute.setNominal(rs.getInt("nominal"));
						valute.setValue(rs.getDouble("valute_value"));
						valute.setDate(rs.getDate("date").toLocalDate());
						return valute;
					}

				});
		return valutes;
	}
}
