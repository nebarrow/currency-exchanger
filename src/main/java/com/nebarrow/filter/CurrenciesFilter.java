package com.nebarrow.filter;

import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.nebarrow.validation.ParametersValidator;

import java.io.IOException;

public class CurrenciesFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equals("POST")) {
            var name = req.getParameter("name");
            var code = req.getParameter("code");
            var sign = req.getParameter("sign");

            var nameMessageError = ParametersValidator.checkName(name);
            var codeMessageError = ParametersValidator.checkCode(code);
            var signMessageError = ParametersValidator.checkSign(sign);

            if (!nameMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, nameMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!codeMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, codeMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (!signMessageError.isEmpty()) {
                HttpErrorSender.sendError(res, signMessageError, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            chain.doFilter(req, res);
            return;
        } else if (req.getMethod().equals("GET")) {
            chain.doFilter(req, res);
            return;
        }
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
