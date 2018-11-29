package br.dev.valmirt.login.security;

import br.dev.valmirt.login.service.UserDetailsServiceImpl;
import br.dev.valmirt.login.utils.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;

    private UserDetailsServiceImpl userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);

        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header != null && !header.isEmpty()) {
            UsernamePasswordAuthenticationToken auth = getAuthenticationToken(header);
            if (auth != null){
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        if (jwtUtil.validToken(token)){
            String username = jwtUtil.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByToken(username, token);
            return userDetails != null ?
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) :
                    null;
        }
        return null;
    }
}
