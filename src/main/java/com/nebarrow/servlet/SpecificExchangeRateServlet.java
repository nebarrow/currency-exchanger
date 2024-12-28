package com.nebarrow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.dto.PostExchangeRatesDto;
import com.nebarrow.entity.Currency;
import com.nebarrow.service.ExchangeRatesService;
import com.nebarrow.util.HttpErrorSender;
import com.nebarrow.util.ServiceLocator;
import com.nebarrow.validation.ParametersValidator;
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
        }
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
        var exchangeRateName = req.getPathInfo().substring(1);
        var rateReader = req.getReader().readLine();
        var rateStr = rateReader.replace("rate=","");
        var rateMessageError = ParametersValidator.checkRate(rateStr);

       if (!rateMessageError.isEmpty()) {
            HttpErrorSender.sendError(resp, rateMessageError, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        var baseCode = exchangeRateName.substring(0, 3);
        var targetCode = exchangeRateName.substring(3, 6);
        var rate = Double.parseDouble(rateStr);
        var exchangeRate = exchangeRatesService.update(
                new PostExchangeRatesDto(
                        Currency.builder().code(baseCode).build(),
                        Currency.builder().code(targetCode).build(),
                        rate));

        objectMapper.writeValue(resp.getWriter(), exchangeRate);
    }
}
