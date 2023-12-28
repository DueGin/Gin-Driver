package com.ginDriver.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ResponseUtils {

    private  final static String RESPONSE_TYPE= "application/json;charset=utf-8";

    public static void result(HttpServletResponse response, int code, Object msg) throws IOException {
        response.setStatus(code);
        response.setContentType(RESPONSE_TYPE);
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(msg));
    }

}
