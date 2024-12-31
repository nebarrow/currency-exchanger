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

@WebServlet("/exchangeRate/*")
public class SpecificExchangeRateServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private ExchangeRatesService exchangeRatesService;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
            return;
        }
        super.service(req, resp);
    }

    @Override
    public void init() {
        objectMapper = ServiceLocator.getService(ObjectMapper.class);
        exchangeRatesService = ServiceLocator.getService(ExchangeRatesService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exchangeRateName = req.getPathInfo().substring(1);
        var exchangeRate = exchangeRatesService.getByName(exchangeRateName);
        objectMapper.writeValue(resp.getWriter(), exchangeRate);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var exchangeRateRequest = req.getAttribute("exchangeRate");
        var exchangeRate = exchangeRatesService.update((ExchangeRateRequest) exchangeRateRequest);
        objectMapper.writeValue(resp.getWriter(), exchangeRate);
    }
}
