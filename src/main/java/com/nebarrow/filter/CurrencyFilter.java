package com.nebarrow.filter;

import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.nebarrow.validation.ParametersValidator;

import java.io.IOException;

public class CurrencyFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equals("GET")) {
            String code = req.getPathInfo().substring(1);
            var errorMessage = ParametersValidator.checkCode(code);
            if (!errorMessage.isEmpty()) {
                HttpErrorSender.sendError(res, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
            }
            chain.doFilter(req, res);
        }
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
