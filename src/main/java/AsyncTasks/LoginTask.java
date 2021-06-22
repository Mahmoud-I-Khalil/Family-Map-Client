package AsyncTasks;

import android.os.AsyncTask;

import com.example.familyclient.Fragments.LoginFragment;

import Net.ServerProxy;
import Request.LoginRequest;
import Result.LoginResult;

public class LoginTask extends AsyncTask<LoginRequest, LoginResult, LoginResult> implements  DataTask.DataContext {

    private String serverIp;
    private String serverHost;
    private LoginFragment loginFragment;
    private boolean success;
    private LoginContext context;

    @Override
    public void ExecuteComplete(String toastMessage) {
        context.ExecuteComplete(toastMessage);
    }

    public interface LoginContext {
        void ExecuteComplete(String toastMessage);
    }


    public LoginTask(String serverIp, String serverHost, LoginFragment loginFragment, LoginContext loginContext){

        this.serverIp = serverIp;
        this.serverHost = serverHost;
        this.loginFragment = loginFragment;
        context = loginContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(LoginResult loginResult) {
        if(loginResult.isSuccess()){
            DataTask dataTask = new DataTask(serverIp,serverHost,this);
            dataTask.execute(loginResult.getAuthtoken());

        }
        else{
            context.ExecuteComplete(loginResult.getMessage());
        }

    }

    @Override
    protected LoginResult doInBackground(LoginRequest... loginRequests) {
        ServerProxy serverProxy = ServerProxy.toInitialize();
        LoginResult loginResult = serverProxy.login(loginRequests[0]);
        success = loginResult.isSuccess();
        return loginResult;
    }

    public boolean isSuccesfull() {
        return success;
    }
}
