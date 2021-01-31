package ru.mikhailkhr.ConverterApp.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.mikhailkhr.ConverterApp.Utils.ValutePair;
import ru.mikhailkhr.ConverterApp.entity.Valute;
import ru.mikhailkhr.ConverterApp.entity.ValuteValue;

@Repository
public class ValuteValuesJdbcDao {
	private final String INSERT_SQL = "INSERT INTO converterAppSchema.valute_value (valute_numCode, valute_value, nominal, date) VALUES (?, ?, ?, ?);";
	private final String SELECT_FROME_TO_VALUTE_VALUES = "SELECT val1.id, val1.valute_value , \n"
														+ "val1.valute_numcode, val1.nominal , \n"
														+ "val2.id, val2.valute_value valTo, \n"
														+ "val2.valute_numcode , val2.nominal,\n"
														+ "val1.date\n"
														+ "FROM converterappschema.valute_value as val1\n"
														+ "INNER JOIN converterappschema.valute_value as val2 ON val1.date = val2.date\n"
														+ "WHERE val1.valute_numcode = ? AND val2.valute_numcode = ?\n"
														+ "AND val2.date = ?\n"
														+ "ORDER BY val2.date; ";
	
	private final String SELECT_VALUTE_VALUES_IN_PEREUD = "SELECT val1.id, val1.valute_value ,\n"
														+ "val1.valute_numcode, val1.nominal , \n"
														+ "val2.id, val2.valute_value valTo, \n"
														+ "val2.valute_numcode , val2.nominal,\n"
														+ "val1.date\n"
														+ "FROM converterappschema.valute_value as val1\n"
														+ "INNER JOIN converterappschema.valute_value as val2 ON val1.date = val2.date\n"
														+ "WHERE val1.valute_numcode = ? AND val2.valute_numcode = ?\n"
														+ "AND val2.date >= ? AND val2.date <= ?\n"
														+ "ORDER BY val2.date;";
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void putAllValuteValues() 
	{
		
	}
	
	public void putAllValute(List<ValuteValue> list) {

		jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ValuteValue valuteValue = list.get(i);
				ps.setInt(1, valuteValue.getValuteNumCode());
				ps.setDouble(2, valuteValue.getValue());
				ps.setInt(3, valuteValue.getNominal());
				ps.setDate(4, Date.valueOf(valuteValue.getDate()));
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return list.size();
			}
		});
		

	}
	
	public Map<String, ValuteValue> getFromToValueInformation(int numCodeForm, int numCodeTo)
	{
		 
			// Processing from SQL Injection 
			PreparedStatementCreator selectValutesPSCreator = new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(SELECT_FROME_TO_VALUTE_VALUES, Statement.RETURN_GENERATED_KEYS);
					
					
					ps.setInt(1, numCodeForm);
					ps.setInt(2, numCodeTo);
					ps.setDate(3, Date.valueOf(LocalDate.now()));
					return ps;
				}
			};
			
			RowMapper<Map<String, ValuteValue>> selectValutesRowMapper = new RowMapper<Map<String, ValuteValue>>() {
				@Override
				public Map<String, ValuteValue> mapRow(ResultSet rs, int rowNum) throws SQLException {
					Map<String, ValuteValue> valuteInfo = new HashMap<String, ValuteValue>();
					ValuteValue fromValuteValue = new ValuteValue();
					fromValuteValue.setId(rs.getInt(1));
					fromValuteValue.setValue(rs.getDouble(2));
					fromValuteValue.setValuteNumCode(rs.getInt(3));
					fromValuteValue.setNominal(rs.getInt(4));
					fromValuteValue.setDate(rs.getDate(9).toLocalDate());
					ValuteValue toValuteValue = new ValuteValue();
					toValuteValue.setId(rs.getInt(5));
					toValuteValue.setValue(rs.getDouble(6));
					toValuteValue.setValuteNumCode(rs.getInt(7));
					toValuteValue.setNominal(rs.getInt(8));
					toValuteValue.setDate(rs.getDate(9).toLocalDate());
					valuteInfo.put(ValuteValue.KEY_NAME_FROM, fromValuteValue);
					valuteInfo.put(ValuteValue.KEY_NAME_TO, toValuteValue);
					return valuteInfo;
				}

			};
			List<Map<String, ValuteValue>> valuteInfoList = jdbcTemplate.query(selectValutesPSCreator, selectValutesRowMapper);
			
			Map<String, ValuteValue> valuteInfo;
			if(valuteInfoList.isEmpty()) 
			{
				valuteInfo = new HashMap<String, ValuteValue>();
			}
			else
			{
				valuteInfo = valuteInfoList.get(0);
			}
			
		 
		 return valuteInfo;
	}
	
	public List<ValutePair> getFromToValueInformation(LocalDate dateFrom,LocalDate dateTo, int numCodeFrom, int numCodeTo){
		// Processing from SQL Injection 
		PreparedStatementCreator selectValutesPSCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SELECT_VALUTE_VALUES_IN_PEREUD, Statement.RETURN_GENERATED_KEYS);
				
				ps.setInt(1, numCodeFrom);
				ps.setInt(2, numCodeTo);
				ps.setDate(3, Date.valueOf(dateFrom));
				ps.setDate(4, Date.valueOf(dateTo));
				System.out.println("selectValutesPSCreator = " + ps);
				return ps;
			}
		};
		

		RowMapper<ValutePair> selectValutesRowMapper = new RowMapper<ValutePair>() {
			@Override
			public ValutePair mapRow(ResultSet rs, int rowNum) throws SQLException {
				ValuteValue fromValuteValue = new ValuteValue();
				fromValuteValue.setId(rs.getInt(1));
				fromValuteValue.setValue(rs.getDouble(2));
				fromValuteValue.setValuteNumCode(rs.getInt(3));
				fromValuteValue.setNominal(rs.getInt(4));
				fromValuteValue.setDate(rs.getDate(9).toLocalDate());
				ValuteValue toValuteValue = new ValuteValue();
				toValuteValue.setId(rs.getInt(5));
				toValuteValue.setValue(rs.getDouble(6));
				toValuteValue.setValuteNumCode(rs.getInt(7));
				toValuteValue.setNominal(rs.getInt(8));
				toValuteValue.setDate(rs.getDate(9).toLocalDate());
				ValutePair pair = new ValutePair(fromValuteValue, toValuteValue);
				return pair;
			}

		};
		
		List<ValutePair> valuteInfoList = jdbcTemplate.query(selectValutesPSCreator, selectValutesRowMapper);
		
		
		return valuteInfoList;
		
	}
}
