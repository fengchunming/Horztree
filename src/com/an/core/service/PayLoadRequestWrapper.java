package com.an.core.service;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PayLoadRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public PayLoadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = IOUtils.toString(request.getInputStream()).getBytes();
    }

    @Override
    public BufferedReader getReader() throws IOException {

        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(
                body));
        return new BufferedReader(isr);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        return new ServletInputStream() {
            private int i;

            @Override
            public int read() throws IOException {
                byte b;
                if (body.length > i)
                    b = body[i++];
                else
                    b = -1;

                return b;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };

    }

    public String getBody() throws IOException {
        return new String(body, "UTF-8");
    }
}
