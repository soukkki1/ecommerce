package com.soukaina.product.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.net.Socket;

@Service
public class ClamAVService {
    private static final String CLAMAV_HOST = System.getenv().getOrDefault("CLAMAV_HOST", "clamav");
    private static final int CLAMAV_PORT = Integer.parseInt(System.getenv().getOrDefault("CLAMAV_PORT", "3310"));

    public boolean scanFile(File file) throws IOException {
        try (Socket socket = new Socket(CLAMAV_HOST, CLAMAV_PORT);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {

            out.write("zINSTREAM\0".getBytes());
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.write(new byte[]{0, 0, 0, 0});
            out.flush();

            byte[] response = new byte[1024];
            int responseLength = in.read(response);
            String result = new String(response, 0, responseLength);

            return !result.contains("FOUND");
        }
    }
}
