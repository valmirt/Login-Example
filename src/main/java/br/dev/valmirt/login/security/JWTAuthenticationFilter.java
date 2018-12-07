package br.dev.valmirt.login.security;

import br.dev.valmirt.login.model.User;
import br.dev.valmirt.login.service.UserDetailsServiceImpl;
import br.dev.valmirt.login.system.exception.AuthorizationException;
import br.dev.valmirt.login.system.exception.JWTAuthenticationFailureHandler;
import br.dev.valmirt.login.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private UserDetailsServiceImpl userDetailsService;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            request.setAttribute("email", user.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e){ throw new RuntimeException(e); }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((UserSpringSecurity) authResult.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        userDetailsService.saveNewToken(username, token);
        response.addHeader("Authorization", token);
        response.addHeader("access-control-expose-headers", "Authorization");
    }
}
