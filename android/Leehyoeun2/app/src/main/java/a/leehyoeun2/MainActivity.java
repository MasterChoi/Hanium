package a.leehyoeun2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    ImageView setting;
    TextView textViewTemp1, textViewTemp2;
    ImageView stateImageView;
    byte threadFlag=0;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting = (ImageView)findViewById(R.id.setting);
        textViewTemp1 = (TextView)findViewById(R.id.textview5);
        textViewTemp2 = (TextView)findViewById(R.id.textview7);
        stateImageView = (ImageView)findViewById(R.id.imageView2);
        intent = new Intent(this,CommunicationService.class);
        startService(intent);
        ChangeView changeView = new ChangeView(textViewHandler);
        changeView.setDaemon(true);
        changeView.start();

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "APP Destroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
        try{
            OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("threadState.txt", MODE_PRIVATE));
            threadFlag=0;
            osw.write(threadFlag);
            osw.close();
        }catch (IOException e){}
    }

    Handler textViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                try{
                    textViewTemp1.setText(CommunicationService.temperature1 + "ºC");
                    textViewTemp2.setText(CommunicationService.temperature2 + "ºC");
                    if (CommunicationService.state.equals("normal")){
                        stateImageView.setImageDrawable(getDrawable(R.drawable.green));
                    } else if(CommunicationService.state.equals("fire"))
                        stateImageView.setImageDrawable(getDrawable(R.drawable.red));
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    };

    class ChangeView extends Thread{
        Handler handler;
        ChangeView(Handler handler){
            this.handler = handler;
        }
        @Override
        public void run() {
            while(true) {
                handler.sendEmptyMessage(0);
                try {Thread.sleep(1000);} catch (Exception e) {}
            }
        }
    }
}
