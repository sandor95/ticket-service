//package hu.otp.partner.security;
//
//import java.util.Set;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
////import org.springframework.security.authentication.BadCredentialsException;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.authority.AuthorityUtils;
//
//@Slf4j
//public class AuthenticationService {
//
//    private static final String API_NAME_HEADER_NAME = "x-api-name";
//
//    private static final String API_KEY_HEADER_NAME = "x-api-key";
//
//    // NOTE for review: Base64 encoded value of 0cbc285c-p@rTn3rApIk3y-ee8c7aef538f
//    private static final String API_KEY = "MGNiYzI4NWMtcEByVG4zckFwSWszeS1lZThjN2FlZjUzOGY=";
//
//    private static final Set<String> APPROVED_CALLERS = Set.of("TICKET-API");
//
//    public static Authentication getAuthentication(HttpServletRequest request) {
//        String apiKey = request.getHeader(API_KEY_HEADER_NAME);
//        String apiName = request.getHeader(API_NAME_HEADER_NAME);
//        if (!StringUtils.equals(apiKey, API_KEY) || StringUtils.isBlank(apiName) || !APPROVED_CALLERS.contains(apiName)) {
//            throw new BadCredentialsException("Invalid API Key");
//        }
//        log.info("Caller's API key valid!");
//
//        return new ApiKeyAuthentication(apiKey, apiName, AuthorityUtils.NO_AUTHORITIES);
//    }
//}