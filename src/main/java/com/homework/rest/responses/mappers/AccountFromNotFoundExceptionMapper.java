package com.homework.rest.responses.mappers;

import com.homework.exceptions.AccountFromNotFoundException;
import com.homework.rest.responses.BankResponse;
import static com.homework.rest.responses.BankResponse.Status;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountFromNotFoundExceptionMapper implements ExceptionMapper<AccountFromNotFoundException> {

    @Override
    public Response toResponse(AccountFromNotFoundException exception) {
        return Response.status(404)
                .entity(new BankResponse<>(Status.ERROR, exception.getMessage()))
                .type(MediaType.APPLICATION_JSON).build();
    }

}
