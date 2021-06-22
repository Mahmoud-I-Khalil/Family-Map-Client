package AsyncTasks;

import android.os.AsyncTask;

import com.example.familyclient.Fragments.LoginFragment;

import Net.ServerProxy;
import Request.RegisterRequest;
import Result.RegisterResult;

public class RegisterTask extends AsyncTask<RegisterRequest, RegisterResult, RegisterResult> implements DataTask.DataContext {

    private LoginFragment loginFragment;
    private String serverHost;
    private String serverIP;
    private RegisterResult registerResult = null;
    private boolean success;

    private RegisterContext registerContext;

    @Override
    public void ExecuteComplete(String toastMessage) {
        registerContext.ExecuteComplete(toastMessage);
    }

    public interface RegisterContext{
        void ExecuteComplete(String toastMessage);
    }

    public RegisterTask(String serverHost, String serverIP, LoginFragment fragment, RegisterContext context){
        this.serverHost = serverHost;
        this.serverIP = serverIP;
        loginFragment = fragment;
        registerContext = context;
    }

    @Override
    protected void onPostExecute(RegisterResult registerResult) {
        if (registerResult.isSuccess()){
            DataTask dataTask = new DataTask(serverIP,serverHost,this);
            dataTask.execute(registerResult.getAuthToken());
        }
        else{
            registerContext.ExecuteComplete(registerResult.getMessage());
        }
    }

    @Override
    protected RegisterResult doInBackground(RegisterRequest... registerRequests) {
        ServerProxy serverProxy = ServerProxy.toInitialize();
        registerResult = serverProxy.register(registerRequests[0]);
        success = registerResult.isSuccess();
        return registerResult;

    }

    public boolean isSuccesfull() {
        return success;
    }
}
