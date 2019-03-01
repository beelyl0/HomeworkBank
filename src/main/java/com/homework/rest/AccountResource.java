package com.homework.rest;

import com.homework.exceptions.AccountFromNotFoundException;
import com.homework.rest.requests.AccountCreateRequest;
import com.homework.rest.requests.TransferAmountRequest;
import com.homework.rest.responses.BankResponse;
import com.homework.services.BankService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Optional;

@Path("/accounts")
public final class AccountResource {

    private final BankService bankService = new BankService();

    @GET
    @Path("/{accountId}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public BankResponse<BigDecimal> getAccount(@PathParam("accountId") String accountId) {
        return new BankResponse<>(BankResponse.Status.SUCCESS, bankService.getAmount(accountId));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BankResponse<String> createAccount(AccountCreateRequest createRequest) {
        BigDecimal amount = Optional.ofNullable(createRequest.getStartAmount()).orElse(BigDecimal.ZERO);
        String accountNumber = bankService.createAccount(amount);
        return new BankResponse<>(BankResponse.Status.SUCCESS, accountNumber);
    }

    @POST
    @Path("/{accountId}/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BankResponse<String> transferAccount(@PathParam("accountId") String accountId, TransferAmountRequest transferRequest) throws AccountFromNotFoundException {
        bankService.transferAmount(accountId, transferRequest.getToAccountNumber(), transferRequest.getAmount());
        return new BankResponse<>(BankResponse.Status.SUCCESS, "Transfer was successful");
    }

}
