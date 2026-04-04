package org.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.demo.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String requestURI = request.getRequestURI();

    // 🔍 DEBUG: Log every request
    System.out.println("🔍 FILTER: URI=" + requestURI + ", AuthHeader=" + authHeader);

    // Skip if no auth header
    if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
      System.out.println("⚠️ FILTER: No valid Bearer token, continuing chain");
      filterChain.doFilter(request, response);
      return;
    }

    final String token = authHeader.substring(7);
    System.out.println("🔍 FILTER: Token extracted, length=" + token.length());

    try {
      // ✅ Step 1: Validate token
      boolean isValid = jwtUtil.validateToken(token);
      System.out.println("🔍 FILTER: validateToken() = " + isValid);

      if (!isValid) {
        System.out.println("❌ FILTER: Token validation FAILED");
        filterChain.doFilter(request, response);
        return;
      }

      // ✅ Step 2: Extract username
      String username = jwtUtil.extractUsername(token);
      System.out.println("🔍 FILTER: extractUsername() = " + username);

      if (username == null) {
        System.out.println("❌ FILTER: Username extraction returned null");
        filterChain.doFilter(request, response);
        return;
      }

      // ✅ Step 3: Check if already authenticated
      var existingAuth = SecurityContextHolder.getContext().getAuthentication();
      System.out.println("🔍 FILTER: Existing auth = " + existingAuth);

      if (existingAuth == null) {
        // ✅ Step 4: Create UserDetails
        UserDetails userDetails = new User(
            username,
            "",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        System.out.println("✅ FILTER: Created UserDetails for: " + username);

        // ✅ Step 5: Create authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // ✅ Step 6: CRITICAL - Set in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("✅ FILTER: Authentication SET in SecurityContextHolder");

        // ✅ Verify it was set
        var newAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("✅ FILTER: Verification - new auth = " + newAuth + ", name="
            + (newAuth != null ? newAuth.getName() : "null"));
      }

    } catch (Exception e) {
      System.out.println("❌ FILTER: EXCEPTION = " + e.getClass().getSimpleName() + ": " + e.getMessage());
      e.printStackTrace();
    }

    // ✅ Always continue the chain
    filterChain.doFilter(request, response);
    System.out.println("✅ FILTER: Chain continued for " + requestURI);
  }
}
