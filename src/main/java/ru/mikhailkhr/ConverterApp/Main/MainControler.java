package ru.mikhailkhr.ConverterApp.Main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ru.mikhailkhr.ConverterApp.JDBC.HistoryEntryJdbcDao;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteJdbcDao;
import ru.mikhailkhr.ConverterApp.apihandler.ValuteApiRequester;
import ru.mikhailkhr.ConverterApp.entity.HistoryEntry;
import ru.mikhailkhr.ConverterApp.entity.Valute;
import ru.mikhailkhr.ConverterApp.security.SecurityUser;

@Controller
public class MainControler {

	@Autowired
	ValuteJdbcDao valuteJdbcDao;
	@Autowired
	ValuteConverter convertintValutes;
	@Autowired
	HistoryEntryJdbcDao historyEntryJdbcDao;
	@Autowired
	ValuteApiRequester valuteApiRequester;
	DateTimeFormatter postresFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Main controller that return basic valutes, if valutes not in the database
	 * call the api and write in to the database
	 * 
	 * @param model
	 * @param request
	 * @param authentication
	 * @return
	 */
	@GetMapping("/main")
	public String main(Model model, HttpServletRequest request, Authentication authentication) {

		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		LocalDate todayDate = LocalDate.now();
		// tries to get valutes from database
		List<Valute> list = valuteJdbcDao.getAllValuteByDate(todayDate);
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		// if new values not in the database treis to get the valutes using api
		if (list.size() < 1) {
			list = valuteApiRequester.getVaulter();
			Valute valute = list.get(0);
			/*
			 * BAG REPORT: I don't not if it suppose to work this way, bus sometimes the api
			 * returns the previous day value, but what more important previous day date in
			 * the xml file and I need to check if the valutes with this date already in the
			 * database
			 */
			if (valute.getDate().isEqual(todayDate)) {
				valuteJdbcDao.putAllValute(list);
			} else {
				todayDate = valute.getDate();
				int listSize = valuteJdbcDao.getAllValuteByDate(todayDate).size();
				if (listSize < 1) {
					valuteJdbcDao.putAllValute(list);
				}
			}

		}

		// set model attributes to user in thymeleaf
		model.addAttribute("date", todayDate.format(postresFormatter));
		model.addAttribute("Valutes", list);
		model.addAttribute("ShowResult", false);
		model.addAttribute("name", userDetails.getUsername());
		return "mainApp";
	}

	/**
	 * Controller that actually manage a convertion and return result
	 * 
	 * @param request
	 * @param model
	 * @param authentication
	 * @return
	 */
	@PostMapping("/main")
	public String mainConvert(HttpServletRequest request, Model model, Authentication authentication) {

		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();

		// get all needed attributes from request

		String charCodeFrom = request.getParameter("firstCurency");
		String charCodeTo = request.getParameter("secondCurency");

		// Get the date of last updated valutes
		LocalDate localDate = LocalDate.parse(request.getParameter("date"), postresFormatter);
		double number = Double.valueOf(request.getParameter("number"));

		// convert the valute
		String user_id = userDetails.getId();
		List<Valute> list = valuteJdbcDao.getAllValuteByDate(localDate);

		double fromValue = 0.0;
		double fromNominal = 0.0;
		double toValue = 0.0;
		double toNominal = 0.0;
		String fromId = "";
		String toId = "";

		// if value if RUB user default values

		if ("RUB".equals(charCodeFrom)) {

			fromNominal = 1.0;
			fromValue = 1.0;
			fromId = "1";
		}

		// if value if RUB user default values

		if ("RUB".equals(charCodeTo)) {
			toNominal = 1.0;
			toValue = 1.0;
			toId = "1";
		}

		// Tries to find propriate values by char code

		for (Valute valute : list) {
			if (valute.getCharCode().equals(charCodeFrom)) {
				fromNominal = (double) valute.getNominal();
				fromValue = valute.getValue();
				fromId = valute.getId();
			}
			if (valute.getCharCode().equals(charCodeTo)) {
				toNominal = (double) valute.getNominal();
				toValue = valute.getValue();
				toId = valute.getId();
			}
		}

		double result = convertintValutes.convertValultes(fromValue, fromNominal, toValue, toNominal, number);

		// create history entry
		HistoryEntry historyEntry = new HistoryEntry(charCodeFrom, charCodeTo, number, result, LocalTime.now(),
				LocalDate.now(), fromId, toId);

		// put history entry to database
		historyEntryJdbcDao.insertHistoryEntry(historyEntry, user_id);

		// set model attributes to user in thymeleaf

		model.addAttribute("Valutes", list);
		model.addAttribute("ShowResult", true);
		model.addAttribute("result", result);
		model.addAttribute("toChar", charCodeTo);
		model.addAttribute("fromChar", charCodeFrom);
		model.addAttribute("number", number);
		model.addAttribute("date", localDate.format(postresFormatter));
		model.addAttribute("name", userDetails.getUsername());
		return "mainApp";
	}

	// Controller for history page

	@GetMapping("/history")
	public String getHistory(HttpServletRequest request, Model model, Authentication authentication) {

		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		List<HistoryEntry> history = null;

		String date = request.getParameter("sort_date");
		if (date != null) {
			//if date is not null, search history by date
			DateTimeFormatter dateChoserFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateGetBy = LocalDate.parse(date, dateChoserFormatter);

			// get history by user id and by date
			history = historyEntryJdbcDao.selectHistoryByUserIdAndData(userDetails.getId(), dateGetBy);

		} else {
			// get history by user id
			history = historyEntryJdbcDao.selectHistoryByUserId(userDetails.getId());

			// set model attributes to user in thymeleaf

		}
		model.addAttribute("history", history);
		model.addAttribute("name", userDetails.getUsername());

		return "history";
	}

	@GetMapping("/")
	public String dafaultRedirect() {
		return "redirect:/main";
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		} else {
			return "redirect:/";
		}

	}

}
