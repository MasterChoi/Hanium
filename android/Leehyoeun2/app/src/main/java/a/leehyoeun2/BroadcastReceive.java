package a.leehyoeun2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 1 on 2016-07-23.
 */
public class BroadcastReceive extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(null, "Receive Class", Toast.LENGTH_LONG).show();
        try{
            String action = intent.getAction();
            //if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            if(action.equals("android.intent.action.BOOT_COMPLETED")){
                context.startService(new Intent(context,CommunicationService.class));
            } else if(Intent.ACTION_REBOOT.equals(intent.getAction())){
                context.startService(new Intent(context,CommunicationService.class));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
