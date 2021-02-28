package ru.mikhailkhr.ConverterApp.Main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mikhailkhr.ConverterApp.JDBC.HistoryEntryJdbcDao;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteJdbcDao;
import ru.mikhailkhr.ConverterApp.JDBC.ValuteValuesJdbcDao;
import ru.mikhailkhr.ConverterApp.Model.ConverterValuteModel;
import ru.mikhailkhr.ConverterApp.Model.JsonChartObjectHandler;
import ru.mikhailkhr.ConverterApp.apihandler.ValuteApiRequester;
import ru.mikhailkhr.ConverterApp.entity.HistoryEntry;
import ru.mikhailkhr.ConverterApp.entity.Valute;
import ru.mikhailkhr.ConverterApp.security.SecurityUser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MainControler
{

	@Autowired
	ValuteValuesJdbcDao valuteValuesJdbcDao;
	@Autowired
	ValuteJdbcDao valuteJdbcDao;
	@Autowired
	ValuteConverter convertintValutes;
	@Autowired
	HistoryEntryJdbcDao historyEntryJdbcDao;
	@Autowired
	ValuteApiRequester valuteApiRequester;
	@Autowired
	ConverterValuteModel converterValuteModel;

	@Autowired
	JsonChartObjectHandler jsonChartObjectHandler;
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
	public String main(Model model, HttpServletRequest request, Authentication authentication)
	{

		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		LocalDate todayDate = LocalDate.now();

		// tries to get valutes from database
		List<Valute> list = valuteJdbcDao.getAllValuteByLocale(request.getLocale());

		// set model attributes to user in thymeleaf
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
	public String mainConvert(HttpServletRequest request, Model model, Authentication authentication)
	{

		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		List<Valute> list = valuteJdbcDao.getAllValuteByLocale(request.getLocale());

		int numCodeFrom = Integer.parseInt(request.getParameter("firstCurency"));
		String charCodeFrom = "";
		int numCodeTo = Integer.parseInt(request.getParameter("secondCurency"));
		String charCodeTo = "";
		double number = Double.parseDouble(request.getParameter("number"));

		for(Valute valute : list)
		{
			if(valute.getNumCode() == numCodeFrom)
			{
				charCodeFrom = valute.getCharCode();
			}
			if(valute.getNumCode() == numCodeTo)
			{
				charCodeTo = valute.getCharCode();
			}
		}

		// set model attributes to user in thymeleaf
		double result = converterValuteModel.convert(numCodeFrom, numCodeTo, number, userDetails.getAppUser().getId());
		model.addAttribute("Valutes", list);
		model.addAttribute("ShowResult", true);
		model.addAttribute("result", result);
		model.addAttribute("toChar", charCodeTo);
		model.addAttribute("fromChar", charCodeFrom);
		model.addAttribute("toNumCode", numCodeFrom);
		model.addAttribute("fromNumCode", numCodeFrom);
		model.addAttribute("number", number);
		model.addAttribute("name", userDetails.getUsername());
		return "mainApp";
	}

	// Controller for history page

	@GetMapping("/history")
	public String getHistory(HttpServletRequest request, Model model, Authentication authentication)
	{

		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		List<HistoryEntry> history = null;

		String date = request.getParameter("sort_date");
		if(date != null)
		{
			// if date is not null, search history by date
			DateTimeFormatter dateChoserFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateGetBy = LocalDate.parse(date, dateChoserFormatter);

			// get history by user id and by date
			history = historyEntryJdbcDao.selectHistoryByUserIdAndData(userDetails.getId(), dateGetBy);

		} else
		{
			// get history by user id
			history = historyEntryJdbcDao.selectHistoryByUserId(userDetails.getId());

			// set model attributes to user in thymeleaf

		}
		model.addAttribute("history", history);
		model.addAttribute("name", userDetails.getUsername());

		return "history";
	}

	@GetMapping("/")
	public String dafaultRedirect()
	{
		return "redirect:/main";
	}

	@GetMapping("/login")
	public String viewLoginPage(HttpServletRequest request)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken)
		{
			return "login";
		} else
		{
			return "redirect:/";
		}

	}

	@GetMapping("/chart")
	public String getChart(HttpServletRequest request, Model model)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String parameters = "?";
		String dateFrom = request.getParameter("date_from");
		String dateTo = request.getParameter("date_to");
		String valuteNumCodeFrom = request.getParameter("valute_from");
		String valuteNumCodeTo = request.getParameter("valute_to");
		String unit = request.getParameter("unit");
		List<Valute> list = valuteJdbcDao.getAllValuteByLocale(request.getLocale());
		if(dateFrom != null)
		{
			parameters += "date_from=" + dateFrom + "&";
		}
		if(dateTo != null)
		{
			parameters += "date_to=" + dateTo + "&";
		}
		if(valuteNumCodeFrom != null)
		{
			parameters += "valute_from=" + valuteNumCodeFrom + "&";
		}
		if(valuteNumCodeTo != null)
		{
			parameters += "valute_to=" + valuteNumCodeTo + "&";
		}
		if(unit != null)
		{
			parameters += "unit=" + unit;
		}
		model.addAttribute("chartParameters", parameters);
		model.addAttribute("Valutes", list);
		model.addAttribute("maxDate", LocalDate.now().format(formatter));
		return "chart";

	}
}
