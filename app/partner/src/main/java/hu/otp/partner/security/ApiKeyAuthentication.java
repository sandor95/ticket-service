//package hu.otp.partner.security;
//
//import java.util.Collection;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//public class ApiKeyAuthentication extends AbstractAuthenticationToken {
//
//    private final String apiKey;
//
//    private final String apiName;
//
//    public ApiKeyAuthentication(String apiKey, String apiName, Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);
//        this.apiKey = apiKey;
//        this.apiName = apiName;
//        setAuthenticated(true);
//    }
//
//    @Override
//    public Object getCredentials() {
//        return apiName;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return apiKey;
//    }
//}