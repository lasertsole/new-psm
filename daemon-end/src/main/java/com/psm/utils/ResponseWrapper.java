package com.psm.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final PrintWriter writer;

    public ResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        this.writer = response.getWriter();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) throws IOException {
                buffer.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(buffer, true);
    }

    public String getContentAsString() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public <T> T getContentAsObject(Class<T> type) {
        try {
            return JSON.parseObject(getContentAsString(), type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON content", e);
        }
    }

    public void setResponseContent(String content) throws RuntimeException {
        try {
            writer.print(content);
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to write content to response", e);
        }
    }

    @Override
    public void setContentType(String type) {
        super.setContentType(type + ";charset=UTF-8");
    }
}