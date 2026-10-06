package com.snhu.sslserver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class SslServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslServerApplication.class, args);
	}

}

@RestController
class ServerController {

	//set immutable string value first and last name
	private static final String data = "Colleen R Mitchell";
   
	//get request to map /hash endpoint, returns JSON media
	@GetMapping(value = "/hash", produces = MediaType.APPLICATION_JSON_VALUE)
    
	public HashResponse myHash() {
		//try block to create message digest 
		try {
			//create instance of message digest using SHA 256 to hash
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            //create bite array using UTF-8 encoding
            byte[] checksum = md.digest(data.getBytes(StandardCharsets.UTF_8));
            //generate checksum 
            return new HashResponse(data, bytesToHex(checksum));
		} catch(NoSuchAlgorithmException e) {
            //catch block - important if SHA3-256 is unsupported 
			throw new ResponseStatusException(
            	HttpStatus.INTERNAL_SERVER_ERROR,
                "SHA3-256 algorithm unavailable");	
			
		}
	}
	
    //helper method to build hex string
    private static String bytesToHex(byte[] bytes) {
        
    	//map hex array to string by appending
    	StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        //return string format
        return builder.toString();
    }
    
    record HashResponse(String data, String checksum) {}
}
