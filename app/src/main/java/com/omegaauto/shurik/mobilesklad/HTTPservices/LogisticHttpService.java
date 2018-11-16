package com.omegaauto.shurik.mobilesklad.HTTPservices;

import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.utils.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class LogisticHttpService {

    MobileSkladSettings mobileSkladSettings;

    public LogisticHttpService() {
        mobileSkladSettings = MobileSkladSettings.getInstance();
    }

    public String getContainerJson(String barcode) throws IOException {

        String result = "";

        BufferedReader bufferedReader = null;
        String errorMessage = "";



        try {
            sleep(10000);
            URL url = new URL(Const.URL_HTTP_SERVICE_1C_LOGISTIC + barcode);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(mobileSkladSettings.getTimeout() * 1000); // 2 секунды
            connection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            result = stringBuffer.toString();
        } catch (SocketTimeoutException exTimeout) {
            errorMessage = "Превышено время ожидания...";
        } catch (UnknownHostException exUnknownHostException){
            errorMessage = "Проверьте подключение к Интернет";
        } catch (IOException ex) {
            errorMessage = ex.getMessage();
        } catch (InterruptedException e) {
            errorMessage = e.getMessage();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return result;

    }

    public String login(){
        return "";
    }
}
