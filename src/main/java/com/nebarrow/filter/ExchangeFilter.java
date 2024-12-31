package com.nebarrow.filter;

import com.nebarrow.dto.request.ExchangeRequest;
import com.nebarrow.util.HttpErrorSender;
import com.nebarrow.util.Validator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.*;

import java.io.IOException;

public class ExchangeFilter extends HttpFilter {
    private static final String ALLOWED_METHODS = "GET";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        switch (req.getMethod()) {
            case "GET" -> handleGetRequest(req, res, chain);
            default -> {
                res.setHeader("Allow", ALLOWED_METHODS);
                res.setStatus(SC_METHOD_NOT_ALLOWED);
            }
        }
    }

    private void handleGetRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ExchangeRequest exchangeRequestDto = new ExchangeRequest(
                request.getParameter("from"),
                request.getParameter("to"),
                Double.parseDouble(request.getParameter("amount")));
        var validationResult = Validator.validate(exchangeRequestDto);
        if (validationResult.hasErrors()) {
            HttpErrorSender.sendError(response, validationResult.getMessage(), SC_BAD_REQUEST);
            return;
        }
        request.setAttribute("exchangeRequest", exchangeRequestDto);
        chain.doFilter(request, response);
    }
}
