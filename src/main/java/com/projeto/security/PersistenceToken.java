package com.projeto.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.model.PersistentTokenLogin;
import com.projeto.repository.PersistenceTokenRepository;

@Service
@Transactional
public class PersistenceToken implements PersistentTokenRepository {

	@Autowired
	private PersistenceTokenRepository persistenceTokenRepository;
	
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
	
		PersistentTokenLogin logins = new PersistentTokenLogin();
		logins.setSeries(token.getSeries());
		logins.setUsername(token.getUsername());
		logins.setToken(token.getTokenValue());
		logins.setLastUsed(token.getDate());
		persistenceTokenRepository.save(logins);
		
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		PersistentTokenLogin logins = persistenceTokenRepository.getOne(series);
		logins.setToken(tokenValue);
		logins.setLastUsed(lastUsed);
		persistenceTokenRepository.save(logins);
	}

	@Override
	@Transactional(readOnly = true)
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		Optional<PersistentTokenLogin> logins = persistenceTokenRepository.findById(seriesId);
		if (logins.isPresent()) {
			return new PersistentRememberMeToken( logins.get().getUsername(),
												  logins.get().getSeries(),
												  logins.get().getToken(),
												  logins.get().getLastUsed()); 
		}
		
		return null;
	}

	@Override
	@Transactional
	public void removeUserTokens(String username) {
		persistenceTokenRepository.deletePersistenceTokenLogin(username);
	}

}
