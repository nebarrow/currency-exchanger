package com.nebarrow.filter;

import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.nebarrow.validation.ParametersValidator;

import java.io.IOException;

public class ExchangeRateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equals("GET")) {
            var exchangeRateName = req.getPathInfo().substring(1);
            var baseCode = exchangeRateName.substring(0, 3);
            var targetCode = exchangeRateName.substring(3, 6);

            var baseCodeMessageError = ParametersValidator.checkCode(baseCode);
            var targetCodeMessageError = ParametersValidator.checkCode(targetCode);

            if (!baseCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, baseCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!targetCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, targetCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            chain.doFilter(req, res);
            return;
        } else if (req.getMethod().equals("PATCH")) {
            var exchangeRateName = req.getPathInfo().substring(1);
            var baseCode = exchangeRateName.substring(0, 3);
            var targetCode = exchangeRateName.substring(3, 6);
            var baseCodeMessageError = ParametersValidator.checkCode(baseCode);
            var targetCodeMessageError = ParametersValidator.checkCode(targetCode);

            if (!baseCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, baseCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!targetCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, targetCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            chain.doFilter(req, res);
            return;
        }
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}