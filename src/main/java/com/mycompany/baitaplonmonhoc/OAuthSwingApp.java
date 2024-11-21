//package com.mycompany.baitaplonmonhoc;
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.JsonOxygenFactory;
//import com.google.api.client.extensions.java6.auth.oauth2.GooglePromptFlow;
//import com.google.api.client.extensions.java6.auth.oauth2.GooglePrompt;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.IOException;
//
//public class OAuthSwingApp {
//
//    private static final String CLIENT_ID = "your-client-id";
//    private static final String CLIENT_SECRET = "your-client-secret";
//    private static final String REDIRECT_URI = "http://localhost";
//    private static final String AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/auth";
//    private static final String ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";
//    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//    private static final JsonFactory JSON_FACTORY = new JsonFactory();
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("OAuth 2.0 Example");
//            JButton authenticateButton = new JButton("Authenticate with Google");
//
//            authenticateButton.addActionListener(e -> authenticateWithGoogle());
//
//            frame.setLayout(new FlowLayout());
//            frame.add(authenticateButton);
//            frame.setSize(300, 200);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setVisible(true);
//        });
//    }
//
//    private static void authenticateWithGoogle() {
//        try {
//            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                    HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET,
//                    Collections.singleton("https://www.googleapis.com/auth/userinfo.profile"))
//                    .setAccessType("offline")
//                    .setApprovalPrompt("force")
//                    .build();
//
//            Credential credential = flow.loadCredential("user");
//
//            if (credential == null) {
//                // Step 1: Start OAuth 2.0 flow
//                GooglePromptFlow promptFlow = new GooglePromptFlow(
//                        AUTHORIZATION_SERVER_URL,
//                        ACCESS_TOKEN_URL,
//                        CLIENT_ID,
//                        CLIENT_SECRET,
//                        REDIRECT_URI
//                );
//                promptFlow.startAuthentication();
//            } else {
//                System.out.println("Authenticated with token: " + credential.getAccessToken());
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//}
