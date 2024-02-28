package hu.otp.ticket.service.core.api.tokenvalidation;

import static hu.otp.ticket.service.Const.X_USER_TOKEN;
import static hu.otp.ticket.service.core.api.CoreApiApplication.APP_NAME;

import java.util.Map;

import hu.otp.ticket.service.core.api.tokenvalidation.model.TokenValidationResultDTO;
import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/token")
public class TokenValidationController {

    private final TokenValidationServiceImpl validationService;

    private final JournalService journalService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "The given token is valid", useReturnTypeSchema = true)
    })
    @Operation(operationId = "validate",
            description = "Validates the token given in header",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                        content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = "userId", in = ParameterIn.PATH, description = "User id")}
    )
    @PostMapping("/validate/{userId}")
    public TokenValidationResultDTO validate(@RequestHeader Map<String, String> headers, @PathVariable Long userId) {
        String token = headers.get(X_USER_TOKEN);
        TokenValidationResultDTO validationResultDTO = validationService.validate(userId, token);
        saveJournal(userId, token, validationResultDTO);
        return validationResultDTO;
    }

    private void saveJournal(Long userId, String token, TokenValidationResultDTO validationResultDTO) {
        Journal journal = Journal.builder()
                                .user(userId.toString())
                                .application(APP_NAME)
                                .type(JournalType.TOKEN_VALIDATION)
                                .timestamp(Util.sysdate())
                                .content(createJournalContent(token, validationResultDTO))
                                .build();
        journalService.save(journal);
    }

    private String createJournalContent(String token, TokenValidationResultDTO validationResultDTO) {
        return "Token validation requested with token: [" + token + "]\n"
                .concat("Result: ").concat(validationResultDTO.toString());
    }
}