package ru.mikhailkhr.ConverterApp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mikhailkhr.ConverterApp.entity.ConverterAppUser;

import java.util.Collection;
import java.util.List;

/**
 * Security capsule for converterAppUser
 *
 * @author mikhailkhr
 */
public class SecurityUser implements UserDetails
{

	// Actual system user class
	private final ConverterAppUser user;
	private List<SimpleGrantedAuthority> autoritis;

	public int getId()
	{
		return user.getId();
	}

	public SecurityUser(ConverterAppUser user)
	{
		this.user = user;
	}

	public ConverterAppUser getAppUser()
	{
		// TODO Auto-generated method stub
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		// TODO Auto-generated method stub
		return autoritis;
	}

	@Override
	public String getPassword()
	{
		// TODO Auto-generated method stub
		return user.getPass();
	}

	@Override
	public String getUsername()
	{
		// TODO Auto-generated method stub
		return user.getName();
	}

	@Override
	public boolean isAccountNonExpired()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Convert the app user into security user
	 *
	 * @param user
	 * @return
	 */
	public static UserDetails formUser(ConverterAppUser user)
	{
		return new SecurityUser(user);
	}

}
