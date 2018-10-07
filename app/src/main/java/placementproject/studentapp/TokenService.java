package placementproject.studentapp;

import android.os.AsyncTask;
import java.util.HashMap;

class TokenService {
    private String token;

    void registerTokenInDB(String token) {
        this.token = token;
        new UpdateToken().execute();
    }

    private class UpdateToken extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            params.put("token", token);
            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Configuration.URL_TOKEN_ADD, params);

        }
    }
}
