package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.dto.request.ExchangeRequest;
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
        var exchangeRequest = req.getAttribute("exchangeRequest");
        var exchange = exchangeService.exchangeCurrenciesWithAmount((ExchangeRequest) exchangeRequest);
        objectMapper.writeValue(resp.getWriter(), exchange);
    }
}
