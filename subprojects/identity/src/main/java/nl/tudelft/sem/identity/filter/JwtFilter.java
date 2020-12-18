package nl.tudelft.sem.identity.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.tudelft.sem.identity.service.UserService;
import nl.tudelft.sem.identity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
// since it uses a builder pattern, it thinks that a recently defined variable is undefined
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService service;

    /**
     * This method extracts the token from the request header and starts applying filters
     * that validate the token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //extract authorization from request
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String netid = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            //extract token from header
            token = authorizationHeader.substring(7);
            netid = jwtUtil.extractNetid(token);
        }

        if (netid != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //user found in database by netid from token
            UserDetails userDetails = service.loadUserByUsername(netid);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
