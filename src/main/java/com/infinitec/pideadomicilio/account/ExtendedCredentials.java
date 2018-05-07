package com.infinitec.pideadomicilio.account;


import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import java.io.Serializable;


@Alternative
@Named("credentials")  
@SessionScoped  
public class ExtendedCredentials implements Serializable {

	private String username;

	private String password;



	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }



	public String getPassword() { return password; }

	public void setPassword(String password) { this.password = password; }



	private static final long serialVersionUID = 1L;

}
