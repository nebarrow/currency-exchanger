package com.nebarrow.filter;

import com.nebarrow.dto.request.ExchangeRateRequest;
import com.nebarrow.dto.request.ExchangeRatesRequest;
import com.nebarrow.util.HttpErrorSender;
import com.nebarrow.util.Validator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.*;

import java.io.IOException;

public class ExchangeRateFilter extends HttpFilter {
    private static final String ALLOWED_METHODS = "GET, PATCH";
    private static final String EMPTY_CODE_ERROR = "Code cannot be empty";
    private static final String EMPTY_RATE_ERROR = "Rate cannot be empty";


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        switch (req.getMethod()) {
            case "GET" -> handleGetRequest(req, res, chain);
            case "PATCH" -> handlePatchRequest(req, res, chain);
            default -> {
                res.setHeader("Allow", ALLOWED_METHODS);
                res.setStatus(SC_METHOD_NOT_ALLOWED);
            }
        }
    }

    private void handlePatchRequest(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getPathInfo().isEmpty()) {
            HttpErrorSender.sendError(res, EMPTY_CODE_ERROR, SC_BAD_REQUEST);
        }
        var code = req.getPathInfo().substring(1);
        var rateReader = req.getReader().readLine();
        if (rateReader.isEmpty()) {
            HttpErrorSender.sendError(res, EMPTY_RATE_ERROR, SC_BAD_REQUEST);
        }
        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(
                code.substring(0, 3),
                code.substring(3, 6),
                Double.parseDouble(rateReader.replace("rate=","")));
        var validationResult = Validator.validate(exchangeRateRequest);
        if (validationResult.hasErrors()) {
            HttpErrorSender.sendError(res, validationResult.getMessage(), SC_BAD_REQUEST);
            return;
        }
        req.setAttribute("exchangeRate", exchangeRateRequest);
        chain.doFilter(req, res);
    }

    private void handleGetRequest(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if (req.getPathInfo().isEmpty()) {
            HttpErrorSender.sendError(res, EMPTY_CODE_ERROR, SC_BAD_REQUEST);
        }
        var code = req.getPathInfo().substring(1);
        ExchangeRatesRequest exchangeRatesRequest = new ExchangeRatesRequest(
                code.substring(0, 3),  code.substring(3, 6));
        var validationResult = Validator.validate(exchangeRatesRequest);
        if (validationResult.hasErrors()) {
            HttpErrorSender.sendError(res, validationResult.getMessage(), SC_BAD_REQUEST);
            return;
        }
        req.setAttribute("exchangeRate", code);
        chain.doFilter(req, res);
    }
}