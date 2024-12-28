package com.nebarrow.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.PrintWriter;

@UtilityClass
public class HttpErrorSender {

    private static final String MESSAGE_TEMPLATE = "{\"message\": \"%s\"}";

    public static void sendError(HttpServletResponse response, String errorMessage, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonMessage;
        if (!errorMessage.startsWith("{")) {
            jsonMessage = MESSAGE_TEMPLATE.formatted(errorMessage);
        } else {
            jsonMessage = errorMessage;
        }
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}