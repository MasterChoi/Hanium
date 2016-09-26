package a.leehyoeun2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by 1 on 2016-07-23.
 */
public class Settings extends Activity{
    Intent intent;
    ImageView main1,main2,main4;
    View messegeDialogView;
    EditText editText;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("화재경보");
        main1 = (ImageView)findViewById(R.id.main1);
        main2 = (ImageView)findViewById(R.id.main2);
        main4 = (ImageView)findViewById(R.id.main4);

        main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String [] arr = new String[] {"소리","진동","소리+진동"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(Settings.this);
                dlg.setTitle("알람 방식");
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setSingleChoiceItems(arr, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (arr[which].equals("소리")){
                                    CommunicationService.notification.defaults |= Notification.DEFAULT_SOUND;
                                } else if (arr[which].equals("진동")){
                                    CommunicationService.notification.defaults |= Notification.DEFAULT_VIBRATE;
                                } else if (arr[which].equals("소리+진동")){
                                    CommunicationService.notification.defaults |= Notification.DEFAULT_ALL;
                                } else{

                                }
                            }
                        });
                dlg.setPositiveButton("확인",null);
                dlg.show();

            }
        });
        main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(Settings.this, Calendar.class);
                startActivity(intent);
            }
        });

        main4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messegeDialogView = (View)View.inflate(Settings.this,R.layout.messgesetting,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(Settings.this);
                dlg.setTitle("문자 발송 내용");
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setView(messegeDialogView);
                dlg.setNegativeButton("취소", null);
                dlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editText = (EditText) messegeDialogView.findViewById(R.id.editText);
                                try {
                                    OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("messege.txt", MODE_PRIVATE));
                                    osw.write(editText.getText().toString());
                                    osw.close();
                                } catch (IOException e) {
                                }
                            }
                        });
                dlg.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Settings.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                dlg.show();
                //Intent intent = new Intent(Settings.this, MessegeSetting.class);
                //startActivity(intent);
            }
        });

    }
}
