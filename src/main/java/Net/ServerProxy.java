package Net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Result.LoginResult;

import Result.RegisterResult;

public class ServerProxy {


    public static ServerProxy serverProxy;
    private String serverHost;
    private String serverPort;
    private String authToken;

    public static ServerProxy toInitialize() {
        if(serverProxy == null){
            return new ServerProxy("10.0.2.2","8080");
        }
        return serverProxy;
    }

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.authToken = null;
    }

    public LoginResult login(LoginRequest loginRequest) {
        try{
            URL url = new URL("http://"+ serverHost + ":"+ serverPort +"/user/login");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Accept","application/Json");
            httpURLConnection.connect();
            Gson gson = new Gson();
            String requestInformation = gson.toJson(loginRequest);
            OutputStream requestBody = httpURLConnection.getOutputStream();
            writeString(requestInformation,requestBody);
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = httpURLConnection.getInputStream();
                String responseData = readString(respBody);
                LoginResult loginResult = gson.fromJson(responseData, LoginResult.class);
                httpURLConnection.disconnect();
                return loginResult;
            }
            else {
                return new LoginResult(false, "Incorrect Username or Password \uD83D\uDC68\u200D\uD83D\uDCBB");
            }



        }catch (IOException e){
            e.printStackTrace();
            return new LoginResult(false, "Error Login, GO back to Server Proxy thingy");
        }
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        try{
            URL url = new URL("http://"+ serverHost + ":"+ serverPort +"/user/register");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Accept","application/Json");
            httpURLConnection.connect();
            Gson gson = new Gson();
            String requestInformation = gson.toJson(registerRequest);
            OutputStream requestBody = httpURLConnection.getOutputStream();
            writeString(requestInformation,requestBody);
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = httpURLConnection.getInputStream();
                String responseData = readString(respBody);
                RegisterResult registerResult = gson.fromJson(responseData, RegisterResult.class);
                httpURLConnection.disconnect();
                return registerResult;
            }
            else {
                return new RegisterResult(false, "User Already Exists \uD83D\uDC14");
            }


        }catch (IOException e){
            e.printStackTrace();
            return new RegisterResult(false, "Error Registration, GO back to Server Proxy thingy");
        }
    }

    public PersonsArray getAllPeople() {
        try{
            URL url = new URL("http://"+ serverHost + ":"+ serverPort +"/person");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(false);
            httpURLConnection.addRequestProperty("Authorization", authToken);
            httpURLConnection.addRequestProperty("Accept","application/Json");
            httpURLConnection.connect();

            Gson gson = new Gson();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = httpURLConnection.getInputStream();
                String responseData = readString(respBody);
                PersonsArray personsArray = gson.fromJson(responseData, PersonsArray.class);
                httpURLConnection.disconnect();
                return personsArray;

            }
            else {
                return new PersonsArray(httpURLConnection.getResponseMessage());
            }


        }catch (IOException e){
            e.printStackTrace();
            return new PersonsArray("Error recieving the Data from the server");
        }
    }

    public EventsArray getAllEvents() {
        try{
            URL url = new URL("http://"+ serverHost + ":"+ serverPort +"/event");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(false);
            httpURLConnection.addRequestProperty("Authorization", authToken);
            httpURLConnection.addRequestProperty("Accept","application/Json");
            httpURLConnection.connect();

            Gson gson = new Gson();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = httpURLConnection.getInputStream();
                String responseData = readString(respBody);
                EventsArray eventsArray = gson.fromJson(responseData, EventsArray.class);
                httpURLConnection.disconnect();
                return eventsArray;

            }
            else {
                return new EventsArray(httpURLConnection.getResponseMessage());
            }


        }catch (IOException e){
            e.printStackTrace();
            return new EventsArray("Error recieving the Data from the server");
        }
    }

    public void setAuthToken(String newAuthToken){
        authToken = newAuthToken;
    }


    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}


