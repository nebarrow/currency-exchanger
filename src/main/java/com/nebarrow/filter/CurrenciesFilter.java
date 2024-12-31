package com.nebarrow.filter;

import com.nebarrow.dto.request.CurrencyRequest;
import com.nebarrow.util.HttpErrorSender;
import com.nebarrow.util.Validator;
import com.nebarrow.validation.ValidationResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.*;

import java.io.IOException;

public class CurrenciesFilter extends HttpFilter {
    private static final String ALLOWED_METHODS = "GET, POST";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        switch (req.getMethod()) {
            case "POST" -> handlePostRequest(req, res, chain);
            case "GET" -> chain.doFilter(req, res);
            default -> {
                res.setHeader("Allow", ALLOWED_METHODS);
                res.setStatus(SC_METHOD_NOT_ALLOWED);
            }
        }
    }

    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        CurrencyRequest currency = new CurrencyRequest(
                request.getParameter("code"),
                request.getParameter("name"),
                request.getParameter("sign")
        );
        ValidationResult validationResult = Validator.validate(currency);
        if (validationResult.hasErrors()) {
            HttpErrorSender.sendError(response, validationResult.getMessage(), SC_BAD_REQUEST);
            return;
        }
        request.setAttribute("currencyRequest", currency);
        chain.doFilter(request, response);
    }
}
