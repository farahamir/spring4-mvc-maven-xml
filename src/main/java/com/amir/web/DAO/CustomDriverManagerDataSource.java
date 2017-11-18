package com.amir.web.DAO;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Base64;

public class CustomDriverManagerDataSource extends DriverManagerDataSource {

	public CustomDriverManagerDataSource() {
		super();		
	}	
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.AbstractDriverBasedDataSource#setPassword(java.lang.String)
	 */
	public synchronized void setPassword(String encryptedPassword){	
		super.setPassword(base64Decode(encryptedPassword));		
    }
	
	/**
	 * @param token decoded
	 * @return encoded
	 */
	public static String base64Encode(String token) {
		
		String encodedBytes = Base64.getEncoder().encodeToString(token.getBytes());
		return encodedBytes;
	}
 
	/**
	 * @param token encoded
	 * @return decoded token
	 */
	public static String base64Decode(String token) {
		byte[] decodedBytes = Base64.getDecoder().decode(token);
	    return new String(decodedBytes);
	}	
}