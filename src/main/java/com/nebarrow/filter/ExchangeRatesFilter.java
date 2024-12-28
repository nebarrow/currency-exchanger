package com.nebarrow.filter;

import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.nebarrow.validation.ParametersValidator;

import java.io.IOException;

public class ExchangeRatesFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equals("GET")) {
            chain.doFilter(req, res);
            return;
        } else if (req.getMethod().equals("POST")) {
            var baseCode = req.getParameter("baseCurrencyCode");
            var targetCode = req.getParameter("targetCurrencyCode");
            var rate = Double.parseDouble(req.getParameter("rate"));

            var baseCodeMessageError = ParametersValidator.checkCode(baseCode);
            var targetCodeMessageError = ParametersValidator.checkCode(targetCode);
            var rateMessageError = ParametersValidator.checkRate(String.valueOf(rate));

            if (!baseCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, baseCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!targetCodeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, targetCodeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!rateMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, rateMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            chain.doFilter(req, res);
            return;
        }
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
