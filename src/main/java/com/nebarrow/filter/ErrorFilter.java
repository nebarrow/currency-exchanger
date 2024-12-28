package com.nebarrow.filter;

import com.nebarrow.exception.ElementAlreadyExistsException;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.util.HttpErrorSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (ElementAlreadyExistsException e) {
            HttpErrorSender.sendError(res, e.getMessage(), HttpServletResponse.SC_CONFLICT);
        } catch (ElementNotFoundException e) {
            HttpErrorSender.sendError(res, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        } catch (RuntimeException e) {
            HttpErrorSender.sendError(res, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
