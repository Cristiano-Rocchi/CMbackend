package pizzamafia.CMbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pizzamafia.CMbackend.entities.User;
import pizzamafia.CMbackend.exceptions.UnauthorizedException;
import pizzamafia.CMbackend.services.UserService;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UserService userService;

    @Autowired
    public JWTCheckFilter(JWTTools jwtTools, UserService userService) {
        this.jwtTools = jwtTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token mancante o malformato nell'header Authorization.");
        }

        String token = authHeader.substring(7);
        jwtTools.verifyToken(token);
        String id = jwtTools.extractIdFromToken(token);

        User user = userService.findById(UUID.fromString(id));

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
