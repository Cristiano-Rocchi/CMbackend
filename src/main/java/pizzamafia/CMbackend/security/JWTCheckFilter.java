package pizzamafia.CMbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pizzamafia.CMbackend.entities.User;
import pizzamafia.CMbackend.exceptions.UnauthorizedException;
import pizzamafia.CMbackend.repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserRepository userRepository;

    // ✅ Escludi /auth/**
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // ✅ Estrai l'ID utente dal token
            String token = authHeader.replace("Bearer ", "");
            String userId = jwtTools.extractIdFromToken(token);

            // ✅ Trova l'utente nel DB
            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new UnauthorizedException("Utente non trovato."));

            // ✅ Imposta le authorities (es. ROLE_ADMIN)
            String ruolo = user.getRole().name();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + ruolo);

            // ✅ Autenticazione completa
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            throw new UnauthorizedException("Token non valido: " + ex.getMessage());
        }
    }
}
