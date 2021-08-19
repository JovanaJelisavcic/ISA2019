package com.ISA2020.farmacia.security;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ISA2020.farmacia.repository.PatientRepository;

import net.bytebuddy.utility.RandomString;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
     
	@Autowired
	private PatientRepository patient;
    @Autowired
    private PasswordEncoder passwordEncoder;
     
    @Autowired
    private JavaMailSender mailSender;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
	
		return UserDetailsImpl.build(user);
	}
	
	public void register(User user, String siteURL)
	        throws UnsupportedEncodingException, MessagingException {
	
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	     
	    String randomCode = RandomString.make(64);
	    user.setVerificationCode(randomCode);
	    user.setEnabled(false);
	     
	    userRepository.save(user);
	     
	    sendVerificationEmail(user, siteURL);
	}
	     
	private void sendVerificationEmail(User user, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
	    String toAddress = user.getUsername();
	    String fromAddress = "jelisavcicjovana@gmail.com";
	    String senderName = "Farmacia";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Farmacia.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", patient.findById(user.getUsername()).get().getSurname());
	    String verifyURL = siteURL + "/register/verify?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	     
	}
	public boolean verify(String verificationCode) {
	    User user = userRepository.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        userRepository.save(user);
	         
	        return true;
	    }
	     
	}
	
	public boolean checkIfValidOldPassword(UserDetails user, String oldPassword) {
		User old =userRepository.findByUsername(user.getUsername()).get();
		if(passwordEncoder.matches(oldPassword, old.getPassword())) return true;
		else return false;
		
	}

	public void changeUserPassword(UserDetails user, String password) {
		User old =userRepository.findByUsername(user.getUsername()).get();
		old.setPassword(passwordEncoder.encode(password));
		userRepository.save(old);
		
	}


}
