package com.nebarrow.filter;

import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.*;

import com.nebarrow.validation.ParametersValidator;

import java.io.IOException;

public class CurrencyFilter extends HttpFilter {
    private static final String ALLOWED_METHODS = "GET";
    private static final String EMPTY_CODE_ERROR = "Code cannot be empty";

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

    private void handleGetRequest(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo.isEmpty()) {
            HttpErrorSender.sendError(response, EMPTY_CODE_ERROR, SC_BAD_REQUEST);
        }
        var code = pathInfo.substring(1);
        var errorMessage = ParametersValidator.checkCode(code);
        if (!errorMessage.isEmpty()) {
            HttpErrorSender.sendError(response, errorMessage, SC_BAD_REQUEST);
            return;
        }
        request.setAttribute("currency", code);
        chain.doFilter(request, response);
    }
}
