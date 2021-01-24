package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mikhailkhr.ConverterApp.Main.ValuteConverter;
import ru.mikhailkhr.ConverterApp.entity.HistoryEntry;



/**
 * History entry class that interact with database
 * 
 * @author mikhailkhr
 *
 */
@Repository
public class HistoryEntryJdbcDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ValuteConverter convertintValutes;
	
	private final String SELECT_HISTOTY_BY_USER_ID = "SELECT from_valute.charcode, from_valute_value.valute_value,\n"
													+ "from_valute_value.nominal, to_valute.charcode ,\n"
													+ "to_valute_value.valute_value, to_valute_value.nominal,\n"
													+ "hist.number, hist.date_of_convertion, hist.time_of_convertion\n"
													+ "FROM converterAppSchema.historyEntries as hist\n"
													+ "INNER JOIN converterAppSchema.valute_value AS from_valute_value\n"
													+ "ON hist.from_valute_value_id = from_valute_value.id\n"
													+ "INNER JOIN converterappschema.valute AS from_valute\n"
													+ "ON from_valute_value.valute_numcode = from_valute.numcode\n"
													+ "INNER JOIN converterappschema.valute_value AS to_valute_value\n"
													+ "ON hist.to_valute_value_id = to_valute_value.id\n"
													+ "INNER JOIN converterappschema.valute AS to_valute\n"
													+ "ON to_valute_value.valute_numcode = to_valute.numcode\n"
													+ "WHERE hist.user_id = ? ORDER BY hist.date_of_convertion  DESC, \n"
													+ "hist.time_of_convertion DESC;";

	private final String SELECT_HISTOTY_BY_USER_ID_AND_BY_DATE = "SELECT from_valute.charcode, from_valute_value.valute_value,\n"
													+ "from_valute_value.nominal, to_valute.charcode ,\n"
													+ "to_valute_value.valute_value, to_valute_value.nominal,\n"
													+ "hist.number, hist.date_of_convertion, hist.time_of_convertion\n"
													+ "FROM converterAppSchema.historyEntries as hist\n"
													+ "INNER JOIN converterAppSchema.valute_value AS from_valute_value\n"
													+ "ON hist.from_valute_value_id = from_valute_value.id\n"
													+ "INNER JOIN converterappschema.valute AS from_valute\n"
													+ "ON from_valute_value.valute_numcode = from_valute.numcode\n"
													+ "INNER JOIN converterappschema.valute_value AS to_valute_value\n"
													+ "ON hist.to_valute_value_id = to_valute_value.id\n"
													+ "INNER JOIN converterappschema.valute AS to_valute\n"
													+ "ON to_valute_value.valute_numcode = to_valute.numcode\n"
													+ "WHERE hist.user_id = ? AND hist.date_of_convertion = ? \n"
													+ "ORDER BY hist.date_of_convertion  DESC, hist.time_of_convertion DESC;";

	private final String INSERT_HISTORY_ENTRY = "INSERT INTO converterAppSchema.historyEntries (from_valute_value_id, to_valute_value_id, number, user_id, date_of_convertion, time_of_convertion) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	DateTimeFormatter postresFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Find all history entries by user id
	 * 
	 * @param userId {@code String}
	 * @return list of history entry
	 */
	public List<HistoryEntry> selectHistoryByUserId(int userId) {
		
		// Processing from SQL Injection 
		PreparedStatementCreator selectHistoryPSCreator =  new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_HISTOTY_BY_USER_ID, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, userId);
				return ps;
			}
		};
		RowMapper<HistoryEntry> selectHistoryRowMapper = new RowMapper<HistoryEntry>(){

			@Override
			public HistoryEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistoryEntry historyEntry = new HistoryEntry();
				/*
				 * Using all needed information from sql result calculate
				 * all values
				 */
				double valueFrom = rs.getDouble(2);
				double nominalForm = rs.getDouble(3);
				double valueTo = rs.getDouble(5);
				double nominalTo = rs.getDouble(6);
				double valueToConvert = rs.getDouble(7);
				double valueAfterConvert =  convertintValutes.convertValultes(valueFrom, nominalForm, valueTo, nominalTo, valueToConvert);
				
				// Set rest of information to class
				historyEntry.setFromCharCode(rs.getString(1));
				historyEntry.setToCharCode(rs.getString(4));
				historyEntry.setFromValue(valueToConvert);
				historyEntry.setToValue(valueAfterConvert);
				historyEntry.setDate(rs.getDate(8).toLocalDate());
				historyEntry.setTime(rs.getTime(9).toLocalTime());
				return historyEntry;
			}
			
		};
		List<HistoryEntry> history = jdbcTemplate.query(selectHistoryPSCreator, selectHistoryRowMapper);

		return history;
	}

	/**
	 * Find all history entries by user id and date
	 * @param userId {@code String}
	 * @param date {@code LocalDate}
	 * @return list of history entry 
	 */
	public List<HistoryEntry> selectHistoryByUserIdAndData(int userId, LocalDate date) 
	{
		//String.format(SELECT_HISTOTY_BY_USER_ID_AND_BY_DATE, userId, date.format(postresFormatter)), 
		
		
		// Processing from SQL Injection 
		PreparedStatementCreator selectHistoryPSCreator =  new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_HISTOTY_BY_USER_ID_AND_BY_DATE);
				ps.setInt(1, userId);
				ps.setDate(2, Date.valueOf(date));
				return ps;
			}
		};
		RowMapper<HistoryEntry> selectHistoryRowMapper = new RowMapper<HistoryEntry>(){

			@Override
			public HistoryEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistoryEntry historyEntry = new HistoryEntry();
				/*
				 * Using all needed information from sql result calculate
				 * all values
				 */
				double valueFrom = rs.getDouble(2);
				double nominalForm = rs.getDouble(3);
				double valueTo = rs.getDouble(5);
				double nominalTo = rs.getDouble(6);
				double valueToConvert = rs.getDouble(7);
				double valueAfterConvert =  convertintValutes.convertValultes(valueFrom, nominalForm, valueTo, nominalTo, valueToConvert);
				
				// Set rest of information to class
				historyEntry.setFromCharCode(rs.getString(1));
				historyEntry.setToCharCode(rs.getString(4));
				historyEntry.setFromValue(valueToConvert);
				historyEntry.setToValue(valueAfterConvert);
				historyEntry.setDate(rs.getDate(8).toLocalDate());
				historyEntry.setTime(rs.getTime(9).toLocalTime());
				return historyEntry;
			}
			
		};
		List<HistoryEntry> history = jdbcTemplate.query(selectHistoryPSCreator, selectHistoryRowMapper);
		
		return history;
	}

	/**
	 * Insert history entry
	 * 
	 * @param historyEntry {@code HistoryEntry}
	 * @param user_id      {@code String}
	 */
	public void insertHistoryEntry(HistoryEntry historyEntry) {
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_HISTORY_ENTRY, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, historyEntry.getFromId());
				ps.setInt(2, historyEntry.getToId());
				ps.setDouble(3, historyEntry.getFromValue());
				ps.setInt(4, historyEntry.getUserId());
				ps.setDate(5, Date.valueOf(historyEntry.getDate()));
				ps.setTime(6, Time.valueOf(historyEntry.getTime()));
				return ps;
			}
		});
	}

}
