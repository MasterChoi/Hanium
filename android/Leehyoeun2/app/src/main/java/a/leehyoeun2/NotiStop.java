package a.leehyoeun2;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by 1 on 2016-07-23.
 */
public class NotiStop extends Activity{
    NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    static int secondCount = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //secondCount = AlarmSetting.maxpoint;
        nm.cancel(CommunicationService.NOTI_ID);

    }
}
