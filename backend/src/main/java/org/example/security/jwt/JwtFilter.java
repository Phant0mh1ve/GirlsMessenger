package org.example.security.jwt;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.repository.RefreshTokenRepository;
import org.example.security.userDetails.CustomUserServiceImpl;
import org.example.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserServiceImpl customUserService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromRequest(request);

        try {
            if (accessToken != null && jwtService.validateAccessToken(accessToken)) {
                String username = jwtService.getUsernameFromAccessToken(accessToken);
                String refreshToken = refreshTokenRepository.getToken(username);

                if (refreshToken != null && jwtService.validateRefreshToken(refreshToken)){
                    setCustomUserDetailsToSecurityContextHolder(accessToken);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            String errorMessage = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Invalid or expired token";
            response.getWriter().write("{\"error\":\"" + errorMessage + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }



    private void setCustomUserDetailsToSecurityContextHolder(String accessToken){
        String username = jwtService.getUsernameFromAccessToken(accessToken);
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
