package eu.rogowski.dealer.configuration.jwt;

import eu.rogowski.dealer.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(httpServletRequest);

            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                UserDetails UserDetails = userDetailServiceImpl.loadUserByUsername(jwtUtils.getUsernameByToken(jwt));
                UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        UserDetails.getUsername(), null, UserDetails.getAuthorities()
                );
                passwordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
            }
        }catch (Exception e){
            logger.error("User authentication setting error: {}", e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public String parseJwt(HttpServletRequest httpServletRequest) {
        String headerAuthentication = httpServletRequest.getHeader("Authorization");

        if(StringUtils.hasText(headerAuthentication) && headerAuthentication.startsWith("Bearer ")){
            return headerAuthentication.substring(7);
        }

        return null;
    }
}
