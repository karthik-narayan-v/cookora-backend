package com.cookora.security.jwt;

import com.cookora.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtProvider {

    private final ResourceLoader resourceLoader;

    public JwtProvider(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    // 📁 Read public key from file
    private String readKey() throws Exception {
        Resource resource = resourceLoader.getResource(publicKeyPath);
        return new String(resource.getInputStream().readAllBytes());
    }

    // 🔓 Convert PEM → PublicKey
    private PublicKey getPublicKey() throws Exception {

        String keyContent = readKey()
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(keyContent);

        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
    }

    // 🔍 Extract userId
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid JWT");
        }
    }

    // ✅ Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getPublicKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}