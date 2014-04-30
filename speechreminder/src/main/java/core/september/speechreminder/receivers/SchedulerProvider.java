package core.september.speechreminder.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import core.september.speechreminder.app.SpeechReminder;
import core.september.speechreminder.config.Config;
import core.september.speechreminder.helpers.CRUD;
import core.september.speechreminder.models.Event;

public class SchedulerProvider extends BroadcastReceiver {
    public SchedulerProvider() {
    }
    
    private Context context;
    private List<Event> eventList;

    @Override
    public void onReceive(Context context, Intent intent) {
	
		this.context = context;
		eventList = CRUD.getInstance().select(Event.class);
				       
        if(eventList!= null) {
				for (Event event : eventList) {
				event.assign();
			}
        
//			SpeechReminder.getInstance().notifyOnBar(this.getClass(),"Speech schedule",
//				" scheduled "+eventList.size()+" events at "+(new SimpleDateFormat(Config.HOUR_FORMAT)).format(new Date(System.currentTimeMillis()))
//			);
		}
        

        //Context context = SpeechReminder.getInstance();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, this.getClass());
        //intent.putExtra(Config.EXTRA_FIELD, get_id());

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //am.cancel(pi);

        DateTime date = new DateTime(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay();
        DateTime tomorrow = date.plusDays(1);

        am.set(AlarmManager.RTC_WAKEUP, tomorrow.toDateTime().toDate().getTime(), pi);
    }
    
  
}
