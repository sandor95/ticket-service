package hu.otp.ticket.service.core.api.tokenvalidation;

import static hu.otp.ticket.service.Const.X_USER_TOKEN;

import java.util.Map;

import hu.otp.ticket.service.core.api.tokenvalidation.model.TokenValidationResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/token")
public class TokenValidationController {

    private final TokenValidationServiceImpl validationService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "The given token is valid", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "Token validation failed, details in response body",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "500", description = "Technical error occurred, details in response body",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @Operation(operationId = "validate",
            description = "Validates the token given in header",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token"),
                          @Parameter(name = "userId", in = ParameterIn.PATH, description = "User id")}
    )
    @PostMapping("/validate/{userId}")
    public TokenValidationResultDTO validate(@RequestHeader Map<String, String> headers, @PathVariable Long userId) {
        String token = headers.get(X_USER_TOKEN);
        return validationService.validate(userId, token);
        // TODO: insert journal record
    }
}