package com.manocorbax.adblocka.core.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestParser {

    public ParsedHttpRequest parse(InputStream in) throws IOException {

        // Reads the first line from the req
        String requestLine = readLine(in);

        Map<String,String> headers = new LinkedHashMap<>();

        // Will iterate over the req lines and parse them into headers
        String line;
        while (!(line = readLine(in)).isEmpty()) {

            int idx = line.indexOf(":");

            String name = line.substring(0, idx).trim();
            String value = line.substring(idx + 1).trim();

            headers.put(name.toLowerCase(), value);
        }

        // Reads the req body
        byte[] body = readBody(in, headers);

        return new ParsedHttpRequest(requestLine, headers, body);
    }

    private String readLine(InputStream in) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int prev = -1;
        int curr;

        while ((curr = in.read()) != -1) {

            if (prev == '\r' && curr == '\n') {
                break;
            }

            if (prev != -1) buffer.write(prev);

            prev = curr;
        }

        return buffer.toString();
    }

    private byte[] readBody(InputStream in,
                            Map<String,String> headers)
            throws IOException {

        if (headers.containsKey("transfer-encoding") &&
                headers.get("transfer-encoding").equalsIgnoreCase("chunked")) {

            return readChunkedBody(in);
        }

        if (headers.containsKey("content-length")) {

            int length = Integer.parseInt(headers.get("content-length"));

            byte[] body = new byte[length];

            int read = 0;

            while (read < length) {
                read += in.read(body, read, length - read);
            }

            return body;
        }

        return new byte[0];
    }

    private byte[] readChunkedBody(InputStream in)
            throws IOException {

        ByteArrayOutputStream body = new ByteArrayOutputStream();

        while (true) {

            String line = readLine(in);

            int chunkSize = Integer.parseInt(line.trim(), 16);

            if (chunkSize == 0) {
                readLine(in);
                break;
            }

            byte[] chunk = new byte[chunkSize];

            int read = 0;

            while (read < chunkSize) {
                read += in.read(chunk, read, chunkSize - read);
            }

            body.write(chunk);

            readLine(in);
        }

        return body.toByteArray();
    }
}
