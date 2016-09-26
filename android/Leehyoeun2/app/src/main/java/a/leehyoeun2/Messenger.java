package a.leehyoeun2;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by EmbLab on 2016-09-20.
 */
public class Messenger {
    private Context mContext;

    public Messenger(Context mContext) {
        this.mContext = mContext;
    }

    public void sendMessageTo(String phoneNum, String message) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, message, null, null);

        Toast.makeText(mContext, "Message transmission is completed.", Toast.LENGTH_SHORT).show();
    }
}
