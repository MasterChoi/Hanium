package a.leehyoeun2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

/**
 * Created by 1 on 2016-07-23.
 */
public class CommunicationService extends Service {
    Socket socket;
    BufferedReader bufferedReader;
    byte threadFlag=0;
    String reciveData;
    static String state, temperature1, temperature2, trash;
    NotificationManager notificationManager;
    static Notification notification;
    static final int NOTI_ID = 101;
    boolean messege = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        temperature1 = "";
        temperature2 = "";
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(),NotiStop.class),PendingIntent.FLAG_CANCEL_CURRENT);
        notification = new Notification.Builder(this)
                .setContentTitle("outbreak of Fire!!!")
                .setContentText("Danger")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Danger")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_INSISTENT;

        try{
            OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("threadState.txt", MODE_PRIVATE));
            osw.write(threadFlag);
            osw.close();
        }catch (IOException e){}
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader (openFileInput("threadState.txt")));
            if(br.read()==0)
                worker.start();
        }catch (IOException e){}

    }
    Thread worker = new Thread() {
        public void run() {
            threadFlag = 1;
            NotiStop.secondCount = -1;
            try{
                while(true) {
                    try {
                        socket = new Socket("192.168.0.4", 5555);
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        reciveData = bufferedReader.readLine();

                        StringTokenizer tk = new StringTokenizer(reciveData,"#");
                        state = tk.nextToken();
                        temperature1 = tk.nextToken();
                        temperature2 = tk.nextToken();
                        trash = tk.nextToken();
                        Log.d("Recive data",reciveData);
                        Log.d("state : ",state);
                        Log.d("temperature1 :", temperature1);
                        Log.d("temperature2", temperature2);
                        if (state.equals("fire") && NotiStop.secondCount < 0) {
                            notificationManager.notify(NOTI_ID, notification);
                            if(messege == true){
                                BufferedReader br = new BufferedReader(new InputStreamReader (openFileInput("messege.txt")));
                                Messenger messenger = new Messenger(getApplicationContext());
                                messenger.sendMessageTo("01082227353", br.readLine());
                                messege = false;
                            }
                        } else if (state.equals("normal")) {
                            //MainActivity.stateImageView.setColorFilter(Color.RED);
                            messege = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //break;
                    }
                    socket.close();
                    bufferedReader.close();
                    NotiStop.secondCount--;
                    try {Thread.sleep(1000);} catch (Exception e) {}
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
