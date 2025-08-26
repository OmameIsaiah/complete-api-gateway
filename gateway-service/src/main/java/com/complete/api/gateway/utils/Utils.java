package com.complete.api.gateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

public class Utils {
    public static String getClientIp(ServerWebExchange exchange) {
        try {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                String ipAddress = remoteAddress.getAddress().getHostAddress();
                // Handle IPv6 localhost
                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
                    return "127.0.0.1";
                }
                return ipAddress;
            }
        } catch (Exception e) {
            System.err.println("Error getting client IP: " + e.getMessage());
        }
        return "unknown";
    }
    public static String getClientIp(ServerHttpRequest request) {
        String ipAddress = "unknown";
        try {
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                ipAddress = remoteAddress.getAddress().getHostAddress();

                // Handle IPv6 localhost (convert to IPv4 for consistency)
                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "::1".equals(ipAddress)) {
                    ipAddress = "127.0.0.1";
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting client IP: " + e.getMessage());
        }
        return ipAddress;
    }
}
