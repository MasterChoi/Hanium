package a.leehyoeun2;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by EmbLab on 2016-09-23.
 */
public class MessegeSetting extends Activity{
    EditText editText;
    Button button;
    String messege;
    ImageView imageView;
    View messegeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messgesetting);
        setTitle("문자 발송 내용");

        imageView = (ImageView)findViewById(R.id.main4);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messegeView = (View) View.inflate(MessegeSetting.this,R.layout.messgesetting,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MessegeSetting.this);
                dlg.setTitle("문자 발송 내용");
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setView(messegeView);
                dlg.setPositiveButton("확인", null);
                dlg.setNegativeButton("취소", null);
                dlg.show();
                messege = editText.getText().toString();
                try{
                    OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("messege.txt", MODE_PRIVATE));
                    osw.write(messege);
                    osw.close();
                }catch (IOException e){}
            }
        });
    }
}
