package name.heqian.cs528.googlefit;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

/**
 * Created by Paul on 2/1/16.
 */
public class ActivityRecognizedService extends IntentService {

    private int highestConfidence;
    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        highestConfidence = 0;
        Intent activityIntent = new Intent();
        activityIntent.setAction(MainActivity.ResponseReceiver.ACTION_RESP);
        activityIntent.addCategory(Intent.CATEGORY_DEFAULT);
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    if (activity.getConfidence() > highestConfidence) {
                        activityIntent.putExtra("ACTION", "You are in a vehicle");
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if (activity.getConfidence() > highestConfidence) {
                        activityIntent.putExtra("ACTION", "You are running");
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if (activity.getConfidence() > highestConfidence) {
                        activityIntent.putExtra("ACTION", "You are still");
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    /*if( activity.getConfidence() >= 75 ) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText( "Are you walking?" );
                        builder.setSmallIcon( R.mipmap.ic_launcher );
                        builder.setContentTitle( getString( R.string.app_name ) );
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }*/
                    if (activity.getConfidence() > highestConfidence) {
                        activityIntent.putExtra("ACTION", "You are walking");
                        highestConfidence = activity.getConfidence();
                    }
                    break;
                }
                default: {
                    //do nothing
                    break;
                }
            }
        }
        sendBroadcast(activityIntent);
    }
}
