package a.leehyoeun2;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

/**
 * Created by EmbLab on 2016-09-09.
 */
public class AlarmSetting extends Activity{
    /*
    static int maxpoint;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmsetting);
        textview = (TextView)findViewById(R.id.textview3);
        textview.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final String[] versionArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                AlertDialog.Builder dlg = new AlertDialog.Builder(AlarmSetting.this);
                dlg.setTitle("알림주기 설정");
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setSingleChoiceItems(versionArray,0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                maxpoint = Integer.parseInt(versionArray[which]) * 60;
                                Toast.makeText(AlarmSetting.this,""+maxpoint,Toast.LENGTH_SHORT).show();
                            }
                        });
                dlg.setPositiveButton("저장", null);
                dlg.show();
            }
        });

    }
    */
}
