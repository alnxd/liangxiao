package zhoushi.ist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SendHttp extends AppCompatActivity {

    private EditText key, value;
    private Button btn1;
    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key = (EditText) findViewById(R.id.key);
        value = (EditText) findViewById(R.id.value);
        btn1 = (Button) findViewById(R.id.sendBtn);
        msg = (TextView) findViewById(R.id.msg);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = null;
                String httpUrl = "http://10.10.136.25:888/register";
                try {
                    url = new URL(httpUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (url != null) {
                    try {
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setDoInput(true);
                        urlConn.setDoOutput(true);
                        urlConn.setRequestMethod("POST");
                        urlConn.setUseCaches(false);
                        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        urlConn.connect();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConn.getOutputStream()));
                        String keyText = SendHttp.this.key.getText().toString();
                        String valueText = SendHttp.this.value.getText().toString();
                        String content = URLEncoder.encode(keyText, valueText);
                        bw.write(content);
                        bw.flush();
                        bw.close();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        String input, resultDate = null;
                        while ((input = reader.readLine()) != null) {
                            resultDate += input + "\n";
                        }
                        reader.close();
                        urlConn.disconnect();
                        if (resultDate != null)
                            msg.setText(resultDate);
                        else
                            msg.setText("读取的内容为空");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }
}
