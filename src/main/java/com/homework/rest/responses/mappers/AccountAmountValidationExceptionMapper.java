package com.homework.rest.responses.mappers;

import com.homework.exceptions.AccountAmountValidationException;
import com.homework.rest.responses.BankResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.homework.rest.responses.BankResponse.Status;

@Provider
public class AccountAmountValidationExceptionMapper implements ExceptionMapper<AccountAmountValidationException> {

    @Override
    public Response toResponse(AccountAmountValidationException exception) {
        return Response.status(422)
                .entity(new BankResponse<>(Status.ERROR, exception.getMessage()))
                .type(MediaType.APPLICATION_JSON).build();
    }

}
