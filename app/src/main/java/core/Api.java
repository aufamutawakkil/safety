package core;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by rizky on 9/13/2015.
 */
public class Api {
    private APIStatus status = APIStatus.INACTIVE;
    public String APIKey = "";
    Police police = null;
    Setting setting = null;

    public enum APIStatus {
        INACTIVE,
        ACTIVE
    }

    public Api (String apikey) {
        this.APIKey = apikey;
    }

    /******************
     * Inner Class
     ******************/
    public static abstract class Callback<TArg> {
        public abstract Void success(TArg params) throws JSONException;

        public Void failed(String msg) {
            return null;
        }
    }


    public static class Police{
        public double lat;
        public double lng;
        public String num;
        public String address;
    }

    public static class Setting{
        public String noHardware;
        public String commandRestart;
        public  String user;
        public String pass;
    }

    public static class StaticMap {
        public LinkedHashMap<String, String> data;

        public StaticMap(LinkedHashMap<String, String> data) {
            this.data = data;
        }
    }

    public static class StaticArray {
        public ArrayList<LinkedHashMap<String, String>> data;

        public StaticArray(ArrayList<LinkedHashMap<String, String>> data) {
            this.data = data;
        }
    }

    /******************
     * Insert Police
     ******************/
    private Callback<Police> pushPoliceCallback;

    public void pushPolice(Police police, Callback<Police> callback) {
        this.police = police;
        this.pushPoliceCallback = callback;
        this.startTask("PUSH_POLICE");
    }

    /******************
     * Get data Police by id Task
     ******************/
    private Callback<StaticArray> getPoliceByIdCallback;

    public void getPoliceById(String policeId, Callback<StaticArray> callback) {
        this.getPoliceByIdCallback = callback;
        this.startTask("GET_POLICE_BY_ID", "&id=" + policeId);
    }

    /******************
     * Get All data Police Task
     ******************/
    private Callback<StaticArray> getAllPoliceCallback;

    public void getAllPolice(Callback<StaticArray> callback) {
        this.getAllPoliceCallback = callback;
        this.startTask("GET_ALL_POLICE");
    }

    /******************
     * Insert Setting
     ******************/
    private Callback<Setting> pushSettingCallback;

    public void pushSetting(Api.Setting setting,Callback<Setting> callback) {
        this.setting = setting;
        this.pushSettingCallback = callback;
        this.startTask("PUSH_SETTING");
    }

    /******************
     * Get data Setting Task
     ******************/
    private Callback<StaticArray> getSettingCallBack;

    public void getSetting(Callback<StaticArray> callback) {
        this.getSettingCallBack = callback;
        this.startTask("GET_SETTING");
    }


    /************************
     * Internal API Task
     ************************/

    private void startTask(String taskName) {
        ApiTask task = new ApiTask();
        task.mode = taskName;
        task.getParams = "";
        task.execute();
    }

    private void startTask(String taskName, String getParams) {
        ApiTask task = new ApiTask();
        task.mode = taskName;
        task.getParams = getParams;
        task.execute();
    }

    private class ApiTask extends AsyncTask<Void, Void, String> {
        public String mode;
        public String getParams = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String result = "";
                HttpHeaders header = new HttpHeaders();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


                MultiValueMap<String, String> post = new LinkedMultiValueMap<String, String>();
                String get = "";
                switch (this.mode) {
                    case "GET_POLICE_BY_ID":
                        get = "?api_key=" + Static.API_KEY;
                        get += this.getParams;
                        result = restTemplate.getForObject(Static.BASE_URL + "server?p=police" + get, String.class);
                        break;

                    case "GET_ALL_POLICE":
                        get = "?api_key=" + Static.API_KEY;
                        result = restTemplate.getForObject(Static.BASE_URL + "server?p=police" + get, String.class);
                        break;

                    case "PUSH_POLICE":
                        post.clear();
                        post.add("lat",Double.toString(Api.this.police.lat));
                        post.add("lng", Double.toString(Api.this.police.lng));
                        post.add("number", Api.this.police.num);
                        post.add("address", Api.this.police.address);

                        get = "?api_key=" + Static.API_KEY;
                        result = restTemplate.postForObject(Static.BASE_URL + "server.php?p=police/push" + get, post, String.class);
                        break;

                    case "PUSH_SETTING":
                        post.clear();
                        post.add("noHardware",Api.this.setting.noHardware);
                        post.add("commandRestart", Api.this.setting.commandRestart);

                        get = "?api_key=" + Static.API_KEY;
                        result = restTemplate.postForObject(Static.BASE_URL + "server.php?p=police" + get, post, String.class);
                        break;

                    case "GET_SETTING":
                        get = "?api_key=" + Static.API_KEY;
                        result = restTemplate.getForObject(Static.BASE_URL + "server?p=setting" + get, String.class);
                        break;
                }
                return result;
            } catch (Exception e) {
                return "";
            }
        }

        private void throwFail(String msg) {
            switch (this.mode) {
                case "PUSH_POLICE":
                    Api.this.pushPoliceCallback.failed(msg);
                    break;
                case "GET_ALL_POLICE":
                    Api.this.getAllPoliceCallback.failed(msg);
                    break;
                case "GET_POLICE_BY_ID":
                    Api.this.getPoliceByIdCallback.failed(msg);
                    break;
                case "PUSH_SETTING":
                    Api.this.pushSettingCallback.failed(msg);
                    break;
                case "GET_SETTING":
                    Api.this.getSettingCallBack.failed(msg);
                    break;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            if (json == "") {
                this.throwFail("Empty response from server");
            } else {
                Boolean isSuccess = false;
                String errorMsg = "";
                HashMap<String, Object> map = new HashMap<String, Object>();
                ObjectMapper mapper = new ObjectMapper();

                try {
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    map = mapper.readValue(json, HashMap.class);

                    isSuccess = (Integer) map.get("status") == 0;
                    errorMsg = (String) map.get("message");

                    switch (this.mode) {
                        case "PUSH_POLICE":
                            if (isSuccess) {
                                HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
                                Api.this.status = APIStatus.ACTIVE;
                                Api.this.pushPoliceCallback.success(Api.this.police);
                            } else {
                                Api.this.status = APIStatus.INACTIVE;
                            }
                            break;
                        case "GET_ALL_POLICE":
                            if (isSuccess) {
                                HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
                                StaticArray subdata = new StaticArray((ArrayList<LinkedHashMap<String, String>>) data.get("data"));
                                Api.this.getAllPoliceCallback.success(subdata);
                            }
                            break;
                        case "GET_POLICE_BY_ID":
                            if (isSuccess) {
                                HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
                                StaticArray subdata = new StaticArray((ArrayList<LinkedHashMap<String, String>>) data.get("data"));
                                Api.this.getPoliceByIdCallback.success(subdata);
                            }
                            break;
                        case "PUSH_SETTING":
                            if (isSuccess) {
                                HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
                                Api.this.status = APIStatus.ACTIVE;
                                Api.this.pushSettingCallback.success(Api.this.setting);
                            } else {
                                Api.this.status = APIStatus.INACTIVE;
                            }
                            break;
                        case "GET_SETTING":
                            if (isSuccess) {
                                HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
                                StaticArray subdata = new StaticArray((ArrayList<LinkedHashMap<String, String>>) data.get("data"));
                                Api.this.getSettingCallBack.success(subdata);
                            }
                            break;
                    }

                    if (!isSuccess) {
                        this.throwFail(errorMsg);
                    }
                } catch (Exception e) {
                    this.throwFail(e.getMessage());
                }
            }
        }
    }
}