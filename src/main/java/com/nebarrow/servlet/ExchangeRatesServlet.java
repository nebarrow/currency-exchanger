package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.dto.request.ExchangeRateRequest;
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
        var exchangeRatesRequest = req.getAttribute("exchangeRates");
        var result = exchangeRatesService.create((ExchangeRateRequest) exchangeRatesRequest);
        objectMapper.writeValue(resp.getWriter(), result);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
