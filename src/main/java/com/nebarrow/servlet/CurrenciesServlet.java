package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.dto.PostCurrencyDto;
import com.nebarrow.service.CurrencyService;
import com.nebarrow.util.HttpErrorSender;
import com.nebarrow.util.ServiceLocator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final String CURRENCY_ALREADY_EXISTS = "This currency already exists";
    private ObjectMapper objectMapper;
    private CurrencyService currencyService;

    @Override
    public void init() {
        objectMapper = ServiceLocator.getService(ObjectMapper.class);
        currencyService = ServiceLocator.getService(CurrencyService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        objectMapper.writeValue(resp.getWriter(), currencyService.getAllCurrencies());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var currencyName = req.getParameter("name");
        var currencyCode = req.getParameter("code");
        var currencySign = req.getParameter("sign");
        var result = currencyService.create(new PostCurrencyDto(currencyCode, currencyName, currencySign));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), result);
    }
}

