package com.nebarrow.filter;

import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.nebarrow.validation.ParametersValidator;

import java.io.IOException;

public class ExchangeFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equals("GET")) {

            var baseCode = req.getParameter("from");
            var targetCode = req.getParameter("to");
            var amount = req.getParameter("amount");

            var baseCodeMessageError = ParametersValidator.checkCode(baseCode);
            var targetCodeMessageError = ParametersValidator.checkCode(targetCode);
            var amountMessageError = ParametersValidator.checkAmount(amount);

            if (!baseCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, baseCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!targetCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, targetCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!amountMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, amountMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            chain.doFilter(req, res);
        }
    }
}
