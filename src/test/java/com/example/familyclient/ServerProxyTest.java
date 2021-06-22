package com.example.familyclient;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import Net.EventsArray;
import Net.PersonsArray;
import Net.ServerProxy;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

public class ServerProxyTest {

    private final static String IP_ADDRESS = "127.0.0.1";
    private final static String PORT_NUM = "8080";
    private ServerProxy serverProxy;


    @Before
    public void setUp() {
        serverProxy = new ServerProxy(IP_ADDRESS, PORT_NUM);
        RegisterRequest regReq = new RegisterRequest("Testing","222","germandog`12","Jeff","MynameisJeff", "f");
        RegisterResult registerResult = serverProxy.register(regReq);
    }

    @Test
    public void SuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("Testing", "222");
        LoginResult loginResult = serverProxy.login(loginRequest);
        Assert.assertNotNull(loginResult.getUsername());
    }

    @Test
    public void UserNotFoundFailedLogin() {
        LoginRequest loginRequest = new LoginRequest("test", "nope");
        LoginResult loginResult = serverProxy.login(loginRequest);
        Assert.assertNotNull(loginResult.getMessage());
    }

    @Test
    public void registerFailUserAlreadyExists() {
        RegisterRequest regReq = new RegisterRequest("Testing","1234","no","sam","hopkins", "m");
        RegisterResult registerResult = serverProxy.register(regReq);
        Assert.assertNotNull(registerResult.getMessage());
    }

    @Test
    public void SuccessfulRegister() {
        RegisterRequest registerRequest = new RegisterRequest("hajjjaa","9u8","hioa`12","helo","jefferso", "m");
        RegisterResult registerResult = serverProxy.register(registerRequest);
        Assert.assertNotNull(registerResult.getPersonID());
    }

    @Test
    public void getAllPeopleSuccess(){
        LoginRequest loginRequest = new LoginRequest("Testing", "222");
        LoginResult loginResult = serverProxy.login(loginRequest);
        String authToken = loginResult.getAuthtoken();
        serverProxy.setAuthToken(authToken);
        PersonsArray personsArray = serverProxy.getAllPeople();
        Assert.assertNotNull(personsArray.getData());
    }

    @Test
    public void getAllEventsSuccess(){
        LoginRequest loginRequest = new LoginRequest("Testing", "222");
        LoginResult loginResult = serverProxy.login(loginRequest);
        String authToken = loginResult.getAuthtoken();
        serverProxy.setAuthToken(authToken);
        EventsArray eventsArray = serverProxy.getAllEvents();
        Assert.assertNotNull(eventsArray.getData());
    }


    @Test
    public void failedAuthTokenPerson(){
        LoginRequest loginRequest = new LoginRequest("Wrong", "wrong");
        LoginResult loginResult = serverProxy.login(loginRequest);
        String authToken = loginResult.getAuthtoken();
        PersonsArray personsArray = serverProxy.getAllPeople();
        Assert.assertNull(personsArray.getData());
    }

    @Test
    public void failedAuthTokenEvent(){
        LoginRequest loginRequest = new LoginRequest("Wrong", "wrong");
        LoginResult loginResult = serverProxy.login(loginRequest);
        String authToken = loginResult.getAuthtoken();
        EventsArray eventsArray = serverProxy.getAllEvents();
        Assert.assertNull(eventsArray.getData());
    }


}
