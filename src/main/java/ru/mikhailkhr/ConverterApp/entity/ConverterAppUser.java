package ru.mikhailkhr.ConverterApp.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent user in app
 *
 * @author mikhailkhr
 */
public class ConverterAppUser
{

	private int id;
	private String name;
	private String mail;
	private String pass;
	private List<SimpleGrantedAuthority> autoritis = getAuth();

	public ConverterAppUser(int id, String name, String mail, String pass)
	{
		super();
		this.id = id;
		this.name = name;
		this.mail = mail;
		this.pass = pass;

	}

	public ConverterAppUser()
	{

	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMail()
	{
		return mail;
	}

	public void setMail(String mail)
	{
		this.mail = mail;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}

	public List<SimpleGrantedAuthority> getAutoritis()
	{
		return autoritis;
	}

	public void setAutoritis(List<SimpleGrantedAuthority> autoritis)
	{
		this.autoritis = autoritis;
	}

	private static List<SimpleGrantedAuthority> getAuth()
	{
		List<SimpleGrantedAuthority> granties = new ArrayList<SimpleGrantedAuthority>();
		return granties;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return name + " has mail = " + mail;
	}

}
