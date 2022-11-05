package com.skomorokhin.networkChat.Server.auth;

public interface IAuthService {

    void start();

    void stop();

    String getUserNameByLoginAndPassword(String login, String password);

    void updateUsername(String currentUsername, String newUsername);
}
