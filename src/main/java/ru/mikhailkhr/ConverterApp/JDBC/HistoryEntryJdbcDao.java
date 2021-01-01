package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

	private final String SELECT_HISTOTY_BY_USER_ID = "SELECT h.valueToConvert, v1.charCode as charCodeFrom,v1.valute_value as valueFrom, v1.nominal as nominalFrom,"
			+ "v2.charCode as charCodeTo, v2.valute_value as valueTo, v2.nominal as nominalTo, h.date_of_convertion as date, h.time_of_convertion as time "
			+ "FROM my_schema.historyEntries h " + "inner join my_schema.valute v1 on v1.id = h.fromValute_id "
			+ "inner join my_schema.valute v2 on v2.id = h.toValute_id  " + "WHERE h.user_id = ?";

	private final String SELECT_HISTOTY_BY_USER_ID_AND_BY_DATE = "SELECT h.valueToConvert, v1.charCode as charCodeFrom,v1.valute_value as valueFrom, v1.nominal as nominalFrom,"
			+ "v2.charCode as charCodeTo, v2.valute_value as valueTo, v2.nominal as nominalTo, h.date_of_convertion as date, h.time_of_convertion as time "
			+ "FROM my_schema.historyEntries h " + "inner join my_schema.valute v1 on v1.id = h.fromValute_id "
			+ "inner join my_schema.valute v2 on v2.id = h.toValute_id  "
			+ "WHERE h.user_id = ? AND h.date_of_convertion = ?";

	private final String INSERT_HISTORY_ENTRY = "INSERT INTO my_schema.historyEntries (fromValute_id, toValute_id, valueToConvert, user_id, date_of_convertion, time_of_convertion) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	DateTimeFormatter postresFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Find all history entries by user id
	 * 
	 * @param userId {@code String}
	 * @return list of history entry
	 */
	public List<HistoryEntry> selectHistoryByUserId(String userId) {
		/*
		 * Processing from SQL Injection 
		 */
		PreparedStatementCreator selectHistoryPSCreator =  new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_HISTOTY_BY_USER_ID, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, Integer.parseInt(userId));
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
				double valueToConvert = rs.getDouble("valueToConvert");
				double valueFrom = rs.getDouble("valueFrom");
				double valueTo = rs.getDouble("valueTo");
				double nominalForm = rs.getDouble("nominalFrom");
				double nominalTo = rs.getDouble("nominalTo");
				double valueAfterConvert = (valueToConvert * nominalForm * valueFrom) / nominalTo / valueTo; 
				/*
				 * Set rest of information to class
				 */
				historyEntry.setToCharCode(rs.getString("charCodeTo"));
				historyEntry.setFromValue(valueToConvert);
				historyEntry.setFromCharCode(rs.getString("charCodeFrom"));
				historyEntry.setToValue(valueAfterConvert);
				historyEntry.setTime(rs.getTime("time").toLocalTime());
				historyEntry.setDate(rs.getDate("date").toLocalDate());
				return historyEntry;
			}
			
		};
		List<HistoryEntry> history = jdbcTemplate.query(selectHistoryPSCreator, selectHistoryRowMapper);
		/*
		 *sort by date 
		 */
		history.sort(new Comparator<HistoryEntry>() {

			@Override
			public int compare(HistoryEntry o1, HistoryEntry o2) {
				LocalDateTime o1dt = LocalDateTime.of(o1.getDate(), o1.getTime());
				LocalDateTime o2dt = LocalDateTime.of(o2.getDate(), o2.getTime());

				return o2dt.compareTo(o1dt);
			}

		});
		return history;
	}

	/**
	 * Find all history entries by user id and date
	 * @param userId {@code String}
	 * @param date {@code LocalDate}
	 * @return list of history entry 
	 */
	public List<HistoryEntry> selectHistoryByUserIdAndData(String userId, LocalDate date) 
	{
		//String.format(SELECT_HISTOTY_BY_USER_ID_AND_BY_DATE, userId, date.format(postresFormatter)), 
		
		/*
		 * Processing from SQL Injection 
		 */
		PreparedStatementCreator selectHistoryPSCreator =  new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_HISTOTY_BY_USER_ID_AND_BY_DATE);
				ps.setInt(1, Integer.parseInt(userId));
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
				double valueToConvert = rs.getDouble("valueToConvert");
				double valueFrom = rs.getDouble("valueFrom");
				double valueTo = rs.getDouble("valueTo");
				double nominalForm = rs.getDouble("nominalFrom");
				double nominalTo = rs.getDouble("nominalTo");
				double valueAfterConvert = (valueToConvert * nominalForm * valueFrom) / nominalTo / valueTo; 
				/*
				 * Set rest of information to class
				 */
				historyEntry.setToCharCode(rs.getString("charCodeTo"));
				historyEntry.setFromValue(valueToConvert);
				historyEntry.setFromCharCode(rs.getString("charCodeFrom"));
				historyEntry.setToValue(valueAfterConvert);
				historyEntry.setTime(rs.getTime("time").toLocalTime());
				historyEntry.setDate(rs.getDate("date").toLocalDate());
				return historyEntry;
			}
			
		};
		List<HistoryEntry> history = jdbcTemplate.query(selectHistoryPSCreator, selectHistoryRowMapper);
		/*
		 *sort by date 
		 */
		history.sort(new Comparator<HistoryEntry>() {

			@Override
			public int compare(HistoryEntry o1, HistoryEntry o2) {
				LocalDateTime o1dt = LocalDateTime.of(o1.getDate(), o1.getTime());
				LocalDateTime o2dt = LocalDateTime.of(o2.getDate(), o2.getTime());

				return o2dt.compareTo(o1dt);
			}

		});
		return history;
	}

	/**
	 * Insert history entry
	 * 
	 * @param historyEntry {@code HistoryEntry}
	 * @param user_id      {@code String}
	 */
	public void insertHistoryEntry(HistoryEntry historyEntry, String user_id) {
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_HISTORY_ENTRY, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, Integer.parseInt(historyEntry.getFromId()));
				ps.setInt(2, Integer.parseInt(historyEntry.getToId()));
				ps.setDouble(3, historyEntry.getFromValue());
				ps.setInt(4, Integer.parseInt(user_id));
				ps.setDate(5, Date.valueOf(historyEntry.getDate()));
				ps.setTime(6, Time.valueOf(historyEntry.getTime()));
				return ps;
			}
		});
	}

}
