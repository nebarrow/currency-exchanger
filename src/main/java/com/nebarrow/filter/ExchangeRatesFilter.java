package com.nebarrow.filter;

import com.nebarrow.dto.request.ExchangeRateRequest;
import com.nebarrow.util.HttpErrorSender;
import com.nebarrow.util.Validator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.*;


import java.io.IOException;

public class ExchangeRatesFilter extends HttpFilter {
    private static final String ALLOWED_METHODS = "GET, POST";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        switch (req.getMethod()) {
            case "GET" -> chain.doFilter(req, res);
            case "POST" -> handlePostRequest(req, res, chain);
            default -> {
                res.setHeader("Allow", ALLOWED_METHODS);
                res.setStatus(SC_METHOD_NOT_ALLOWED);
            }
        }
    }

    private void handlePostRequest(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        ExchangeRateRequest exchangeRatesRequestDto = new ExchangeRateRequest(
                req.getParameter("baseCurrencyCode"),
                req.getParameter("targetCurrencyCode"),
                Double.parseDouble(req.getParameter("rate")));
        var validationResult = Validator.validate(exchangeRatesRequestDto);
        if (validationResult.hasErrors()) {
            HttpErrorSender.sendError(res, validationResult.getMessage(), SC_BAD_REQUEST);
            return;
        }
        req.setAttribute("exchangeRates", exchangeRatesRequestDto);
        chain.doFilter(req, res);
    }
}
