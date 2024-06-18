package com.analytics.web.security;

import com.analytics.web.dto.Credentials;
import com.analytics.web.dto.User;
import com.analytics.web.utils.CookieUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

  @Autowired
  SecurityService securityService;

  @Autowired
  CookieUtils cookieUtils;
  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    verifyToken(request);
    filterChain.doFilter(request, response);
  }

  private void verifyToken(HttpServletRequest request) {
    String session = null;
    FirebaseToken decodedToken = null;
    Credentials.CredentialType type = null;
    Cookie sessionCookie = cookieUtils.getCookie("session");
    String token = securityService.getBearerToken(request);
    logger.info(token);
    try {
      if (sessionCookie != null) {
        session = sessionCookie.getValue();
        decodedToken =
            FirebaseAuth.getInstance()
                .verifySessionCookie(
                    session, true);
        type = Credentials.CredentialType.SESSION;
      } else {
        if (token != null && !token.equalsIgnoreCase("undefined")) {
          decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
          type = Credentials.CredentialType.ID_TOKEN;
        }
      }
    } catch (FirebaseAuthException e) {
      e.printStackTrace();
      log.error("Firebase Exception:: ", e.getLocalizedMessage());
    }
    User user = firebaseTokenToUserDto(decodedToken);
    if (user != null) {
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              user, new Credentials(type, decodedToken, token, session), null);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  private User firebaseTokenToUserDto(FirebaseToken decodedToken) {
    User user = null;
    if (decodedToken != null) {
      user = new User();
      user.setUid(decodedToken.getUid());
      user.setName(decodedToken.getName());
      user.setEmail(decodedToken.getEmail());
      user.setPicture(decodedToken.getPicture());
      user.setIssuer(decodedToken.getIssuer());
      user.setEmailVerified(decodedToken.isEmailVerified());
    }
    return user;
  }
}
