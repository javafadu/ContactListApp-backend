package com.contactlistapp.security.jwt;

import com.contactlistapp.domain.User;
import com.contactlistapp.repository.UserRepository;
import com.contactlistapp.security.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    // filter request with jwt before resources (end-points) for token process
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                Long id = jwtUtils.getIdFromJwtToken(jwt);

                Optional<User> user = userRepository.findById(id);
                // set attribute name as id, request.id as currently logged-in user id
                // getIdFromJwtToken in jwtUtils
                // (after passing token validation)
                request.setAttribute("id", user.get().getId());
                // it will be used in controller with getAttribute to get logged-in userId (first solution)

                // our username is email
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getEmail());

                // create an authentication object here (add into spring security context holder)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // set currently logged-in user (authentication) in Security Context Holder
                // that i can take the currently logged-in user (second solution)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match("/register", request.getServletPath())
                || antPathMatcher.match("/login", request.getServletPath());
    }

    // parse of token method
    private String parseJwt(HttpServletRequest request) {
        // get Authorization in Header
        String headerAuth = request.getHeader("Authorization");
        // if this Authorization Header include a String text and it starts with "Bearer "
        // it means there is a token
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
            // ("Bearer "(Token starts here (7. index) to end)
        }
        return null;
    }
}
