package com.psm.infrastructure.utils;

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
    private final ServletOutputStream originalOutputStream;
    private final ServletOutputStream cacheOutputStream= new ServletOutputStream() {
        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {}

        @Override
        public void write(int b) throws IOException {
            buffer.write(b);
        }
    };


    public ResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        this.originalOutputStream = response.getOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return cacheOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(buffer, true);
    }

    public byte[] getContent() {
        return buffer.toByteArray();
    }

    public String getContentAsString() {
        return buffer.toString();
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
            //设置原来response的响应内容并发送
            originalOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write content to response", e);
        }
    }

    @Override
    public void setContentType(String type) {
        super.setContentType(type + ";charset=UTF-8");
    }

    public void sendResponse() throws RuntimeException{
        try {
            originalOutputStream.flush();

            //关闭输出流
            originalOutputStream.close();
            cacheOutputStream.close();
        } catch (Exception e){
            throw new RuntimeException("Failed to send response", e);
        }
    }
}