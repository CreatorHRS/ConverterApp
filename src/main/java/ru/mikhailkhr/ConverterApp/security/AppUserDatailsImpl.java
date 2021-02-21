package ru.mikhailkhr.ConverterApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mikhailkhr.ConverterApp.JDBC.AppUserJdbcDao;
import ru.mikhailkhr.ConverterApp.entity.ConverterAppUser;

/**
 * User detail service implementation for security user
 *
 * @author mikhailkhr
 */
@Service
public class AppUserDatailsImpl implements UserDetailsService
{

	@Autowired
	AppUserJdbcDao appUserJdbcDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		ConverterAppUser user = appUserJdbcDao.getUserByMail(username);
		return SecurityUser.formUser(user);
	}

}
