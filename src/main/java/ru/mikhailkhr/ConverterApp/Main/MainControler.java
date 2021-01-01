package ru.mikhailkhr.ConverterApp.Main;

import java.time.LocalDate;
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
	 * Main controller that return basic valutes,
	 * if valutes not in the database call the api and write in to the database
	 * @param model
	 * @param request
	 * @param authentication
	 * @return
	 */
	@GetMapping("/main")
	public String main(Model model, HttpServletRequest request, Authentication authentication) {
		 	
		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		LocalDate todayDate = LocalDate.now();
		/*
		 * tries to get valutes from database 
		 */
		List<Valute> list = valuteJdbcDao.getAllValuteByDate(todayDate);
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		
		/*
		 * if new values not in the database treis to get the valutes using api 
		 */
		if(list.size() < 1) {
			list = valuteApiRequester.getVaulter();
			Valute valute = list.get(0);
			/*
			 * BAG REPORT: I don't not if it suppose to work this way,
			 * bus sometimes the api returns the previous day value, 
			 * but what more important previous day date in the xml file
			 * and I need to check if the valutes with this date already in the database
			 */
			if(valute.getDate().isEqual(todayDate)) 
			{
				valuteJdbcDao.putAllValute(list);
			}else {
				todayDate = valute.getDate();
				int listSize = valuteJdbcDao.getAllValuteByDate(todayDate).size();
				if(listSize < 1) {
					valuteJdbcDao.putAllValute(list);
				}
			}
			
			
		}
		/*
		 * set model attributes to user in thymeleaf
		 */
		model.addAttribute("date", todayDate.format(postresFormatter));
		model.addAttribute("Valutes", list);
		model.addAttribute("ShowResult", false);
		model.addAttribute("name", userDetails.getUsername());
		return "mainApp";
	}
	
	/**
	 * Controller that actually manage a convertion and return result
	 * @param request
	 * @param model
	 * @param authentication
	 * @return
	 */
	@PostMapping("/main")
	public String mainConvert(HttpServletRequest request, Model model, Authentication authentication) {
		
		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		/*
		 * get all needed attributes from request
		 */
		String charCodeFrom = request.getParameter("firstCurency"); 
		String charCodeTo = request.getParameter("secondCurency");
		/*
		 * Get the date of last updated valutes
		 */
		LocalDate localDate = LocalDate.parse(request.getParameter("date"), postresFormatter); 
		String number = request.getParameter("number");
		/*
		 * convert the valute
		 */
		Double result = convertintValutes.convertValultes(charCodeFrom, charCodeTo,Double.valueOf(number), localDate);
		/*
		 * get valutes for select box from database useing last updated valutes date
		 */
		List<Valute> list = valuteJdbcDao.getAllValuteByDate(localDate);
		/*
		 * set model attributes to user in thymeleaf
		 */
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
	
	/*
	 * Controller for history page
	 */
	@GetMapping("/history")
	public String getHistory(HttpServletRequest request, Model model, Authentication authentication) {
		
		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		/*
		 * get history by user id
		 */
		List<HistoryEntry> history =  historyEntryJdbcDao.selectHistoryByUserId(userDetails.getId());
		/*
		 * set model attributes to user in thymeleaf
		 */
		model.addAttribute("history", history);
		model.addAttribute("name", userDetails.getUsername());
		return "history";
	}
	
	/*
	 * Controller for history page sorted by date
	 */
	@GetMapping("/history_by_date")
	public String getHistoryByDate(HttpServletRequest request, Model model, Authentication authentication) {
		
		DateTimeFormatter dateChoserFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = request.getParameter("sort_date");
		SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		LocalDate dateGetBy = LocalDate.parse(date, dateChoserFormatter);
		/*
		 * get history by user id
		 */
		List<HistoryEntry> history =  historyEntryJdbcDao.selectHistoryByUserIdAndData(userDetails.getId(), dateGetBy);
		/*
		 * set model attributes to user in thymeleaf
		 */
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
	      if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	    	  return "login";
	      }else {
	    	  return "redirect:/";
	      }
	    
	}
	
}
