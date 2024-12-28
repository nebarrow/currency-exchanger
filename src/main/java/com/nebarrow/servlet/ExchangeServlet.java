package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.service.ExchangeService;
import com.nebarrow.util.ServiceLocator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private ExchangeService exchangeService;

    @Override
    public void init() {
        objectMapper = ServiceLocator.getService(ObjectMapper.class);
        exchangeService = ServiceLocator.getService(ExchangeService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var baseName = req.getParameter("from");
        var targetName = req.getParameter("to");
        var amount = Double.parseDouble(req.getParameter("amount"));
        var exchange = exchangeService.exchangeCurrenciesWithAmount(baseName, targetName, amount);
        objectMapper.writeValue(resp.getWriter(), exchange);
    }
}
