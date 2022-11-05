package com.skomorokhin.networkChat.Data;

public enum CommandType {
    AUTH,
    AUTH_OK,
    ERROR,
    PUBLIC_MESSAGE,
    PRIVATE_MESSAGE,
    CLIENT_MESSAGE,
    END,
    UPDATE_USER_LIST,
    AUTH_TIME_IS_OVER,
    UPDATE_USERNAME,
    UNSUBSCRIBE_USER
}
