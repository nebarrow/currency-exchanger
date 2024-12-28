package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.dto.PostExchangeRatesDto;
import com.nebarrow.entity.Currency;
import com.nebarrow.service.CurrencyService;
import com.nebarrow.service.ExchangeRatesService;
import com.nebarrow.util.ServiceLocator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() {
        objectMapper = ServiceLocator.getService(ObjectMapper.class);
        exchangeRatesService = ServiceLocator.getService(ExchangeRatesService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var exchangeRates = exchangeRatesService.getAll();
        objectMapper.writeValue(resp.getWriter(), exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var baseCode = req.getParameter("baseCurrencyCode");
        var targetCode = req.getParameter("targetCurrencyCode");
        var rate = Double.parseDouble(req.getParameter("rate"));
        var result = exchangeRatesService.create(
                new PostExchangeRatesDto(
                        Currency.builder().code(baseCode).build(),
                        Currency.builder().code(targetCode).build(),
                        rate));
        objectMapper.writeValue(resp.getWriter(), result);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
