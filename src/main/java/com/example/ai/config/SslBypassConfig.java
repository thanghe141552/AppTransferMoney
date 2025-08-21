//package com.example.ai.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SslBypassConfig {
//
//    @PostConstruct
//    public void init() {
//        disableSslVerification();
//    }
//
//    public static void disableSslVerification() {
//        try {
//            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
//                    new javax.net.ssl.X509TrustManager() {
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
//                    }
//            };
//            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
//        } catch (Exception e) {
//            // handle exception
//        }
//    }
//}