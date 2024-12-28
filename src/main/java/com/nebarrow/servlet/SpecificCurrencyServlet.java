package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.service.CurrencyService;
import com.nebarrow.util.ServiceLocator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class SpecificCurrencyServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private CurrencyService currencyService;

    @Override
    public void init() {
        objectMapper = ServiceLocator.getService(ObjectMapper.class);
        currencyService = ServiceLocator.getService(CurrencyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getPathInfo().substring(1);
        var currency = currencyService.getCurrency(code);
        objectMapper.writeValue(resp.getWriter(), currency);
    }
}
