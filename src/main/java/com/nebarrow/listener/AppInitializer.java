package com.nebarrow.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nebarrow.dao.CurrencyDao;
import com.nebarrow.dao.ExchangeRatesDao;
import com.nebarrow.service.CurrencyService;
import com.nebarrow.service.ExchangeRatesService;
import com.nebarrow.service.ExchangeService;
import com.nebarrow.util.ServiceLocator;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServiceLocator.init(sce.getServletContext());
        sce.getServletContext().setAttribute(CurrencyService.class.getName(), new CurrencyService(CurrencyDao.getInstance()));
        sce.getServletContext().setAttribute(ExchangeRatesService.class.getName(), new ExchangeRatesService(ExchangeRatesDao.getInstance()));
        sce.getServletContext().setAttribute(ExchangeService.class.getName(), new ExchangeService(ExchangeRatesDao.getInstance()));
        sce.getServletContext().setAttribute(ObjectMapper.class.getName(), new ObjectMapper());
    }
}
