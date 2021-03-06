package core.september.speechreminder.activities.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import core.september.android.basement.Util.Logger;
import core.september.speechreminder.R;
import core.september.speechreminder.app.SpeechReminder;
import core.september.speechreminder.app.providers.TTSProvider;
import core.september.speechreminder.config.Config;
import core.september.speechreminder.config.DaysOfWeek;
import core.september.speechreminder.helpers.CRUD;
import core.september.speechreminder.models.Event;

/**
 * Created by christian on 20/03/14.
 */
public class ManageItemFragment extends Fragment {
    final static String ARG_ID = "eventID";
    //long mCurrentID = -1;
    Event selectedItem = null;
    EditText editTitle = null;
    EditText editDescription = null;
    EditText editStartDate = null;
    EditText editStartTime = null;
    //CheckBox checkBoxAllDay = null;
    //EditText editEndDate = null;
    //EditText editEndTime = null;
    Button buttonConfirm = null;
    Button buttonDelete = null;
    CheckBox checkBoxSunday = null;
    CheckBox checkBoxMonday = null;
    CheckBox checkBoxTuesday = null;
    CheckBox checkBoxWednsesday = null;
    CheckBox checkBoxThursday = null;
    CheckBox checkBoxFriday = null;
    CheckBox checkBoxSaturday = null;



   /* public static ManageItemFragment newInstance(int index) {

        ManageItemFragment f = new ManageItemFragment();

        // Supply index input as an argument.

        Bundle args = new Bundle();

        args.putInt(Config.PICKED_ITEM, index);

        f.setArguments(args);

        return f;

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        setHasOptionsMenu(true);

        long id = getArguments().getLong(Config.EXTRA_FIELD);
        selectedItem = SpeechReminder.getInstance().getEvent(id);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.manage_event, container, false);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.manage_item_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_item:
                if (selectedItem.get_id() < 0) {
                    selectedItem.set_id(System.currentTimeMillis());
                }
                createUpdate();
                return true;
            case R.id.delete_item:
                CRUD.getInstance().delete(selectedItem, "_id=?", "" + selectedItem.get_id());
                selectedItem = new Event();
                selectedItem.set_id(-1L);
                updateArticleView();
                return true;
            case R.id.speech_preview:
                SpeechReminder.getInstance().getTTSProvider().sayOne(editDescription.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        updateArticleView();


    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SpeechReminder.getInstance().stopSpeach();

    }

    @Override
    public void onPause() {
        super.onPause();
        createUpdate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putLong(Config.EXTRA_FIELD, mCurrentID);
    }

    private void setDate() {

        final Date refDate = selectedItem.getStart() == null ? new Date() : selectedItem.getStart();
           
        Calendar cal = Calendar.getInstance();
        cal.setTime(refDate);

        //                       // String formattedDate = "".concat(String.valueOf(year)).concat("-").concat(String.valueOf(monthOfYear)).concat("_").concat(String.valueOf(dayOfMonth));

        //String[] today = (new SimpleDateFormat(Config.DATE_FORMAT)).format(refDate).split(Config.DATE_SPLIT);
        DatePickerDialog dpdStart = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {

                    Calendar calendarInner = Calendar.getInstance();
					calendarInner.set(Calendar.YEAR, year);
					calendarInner.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					calendarInner.set(Calendar.MONTH, monthOfYear);
                    selectedItem.setStart(calendarInner.getTime());
                    editStartDate.setText(selectedItem.getStartDate());
                  

                } catch (Throwable e) {
                    Logger.debug(ManageItemFragment.this, e);
                }


            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpdStart.show();
    }

    /*private void dateCorrectness() {
        if (selectedItem.getStart().after(selectedItem.getEnd())) {
            selectedItem.setEnd(selectedItem.getStart());
            editEndDate.setText(selectedItem.getEndDate());
            editEndTime.setText(selectedItem.getEndHour());
        }
    }*/

    private void setTime() {

        final Date refDate = selectedItem.getStart() == null ? new Date() : selectedItem.getStart();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY) ; //Config.IS24HOURVIEW ? calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR);
        int minutes =   calendar.get(Calendar.MINUTE);


        TimePickerDialog dpdStart = new TimePickerDialog(this.getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hours, int minutes) {
                try {


//                    String formattedDate = new SimpleDateFormat(Config.DATE_FORMAT).format(refDate);
//                    String formattedHour = "".concat(String.valueOf(hours)).concat(Config.HOUR_SPLIT)
//                            .concat(String.valueOf(minutes));

                    Calendar calInner = Calendar.getInstance();
                    calInner.setTime(refDate);
                    calInner.set(Calendar.HOUR_OF_DAY,hours);
                    calInner.set(Calendar.MINUTE,minutes);

                    selectedItem.setStart(calInner.getTime());
                    editStartTime.setText(selectedItem.getStartHour());
                   

                } catch (Throwable e) {
                    Logger.debug(ManageItemFragment.this, e);
                }


            }
        }, hour, minutes, Config.getInstance().IS24HOURVIEW());
        dpdStart.show();
    }

    public void updateArticleView() {

        editTitle = (EditText) getActivity().findViewById(R.id.editTitle);
        editDescription = (EditText) getActivity().findViewById(R.id.editDescription);

        editStartDate = (EditText) getActivity().findViewById(R.id.editStartDate);
        editStartDate.setFocusable(false);
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        editStartTime = (EditText) getActivity().findViewById(R.id.editStartTime);
        editStartTime.setFocusable(false);
        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });


        /*checkBoxAllDay = (CheckBox) getActivity().findViewById(R.id.checkBoxSAllDay);
        editEndDate = (EditText) getActivity().findViewById(R.id.editEndDate);
        editEndDate.setFocusable(false);
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(false);
            }
        });


        editEndTime = (EditText) getActivity().findViewById(R.id.editEndTime);
        editEndTime.setFocusable(false);
        editEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(false);
            }
        });*/

        checkBoxSunday = (CheckBox) getActivity().findViewById(R.id.checkBoxSunday);
        checkBoxMonday = (CheckBox) getActivity().findViewById(R.id.checkBoxMonday);
        checkBoxTuesday = (CheckBox) getActivity().findViewById(R.id.checkBoxTuesday);
        checkBoxWednsesday = (CheckBox) getActivity().findViewById(R.id.checkBoxWednsesday);
        checkBoxThursday = (CheckBox) getActivity().findViewById(R.id.checkBoxThursday);
        checkBoxFriday = (CheckBox) getActivity().findViewById(R.id.checkBoxFriday);
        checkBoxSaturday = (CheckBox) getActivity().findViewById(R.id.checkBoxSaturday);


        int repeatBit = selectedItem.getRepeatBit();
        editTitle.setText(selectedItem.getTitle());
        editDescription.setText(selectedItem.getDescription());

        editStartDate.setText(selectedItem.getStartDate());
        editStartTime.setText(selectedItem.getStartHour());

        //checkBoxAllDay.setChecked(selectedItem.isAllDay());

        //editEndDate.setText(selectedItem.getEndDate());
        //editEndTime.setText(selectedItem.getEndHour());

        List<DaysOfWeek> repeatDays = DaysOfWeek.getRepeating(selectedItem.getRepeatBit());

        checkBoxSunday.setChecked(repeatDays.contains(DaysOfWeek.SUNDAY));
        checkBoxMonday.setChecked(repeatDays.contains(DaysOfWeek.MONDAY));
        checkBoxTuesday.setChecked(repeatDays.contains(DaysOfWeek.TUESDAY));
        checkBoxWednsesday.setChecked(repeatDays.contains(DaysOfWeek.WEDNESDAY));
        checkBoxThursday.setChecked(repeatDays.contains(DaysOfWeek.THURSDAY));
        checkBoxFriday.setChecked(repeatDays.contains(DaysOfWeek.FRIDAY));
        checkBoxSaturday.setChecked(repeatDays.contains(DaysOfWeek.SATURDAY));


    }


    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putLong(ARG_ID, mCurrentPosition);
    }*/

    private void toModel() {

        selectedItem.setTitle(editTitle.getText().toString());
        selectedItem.setDescription(editDescription.getText().toString());
        //String startDate = editStartDate.getText().toString().concat("|").concat(editStartTime.getText().toString());
        //Date sDate = selectedItem.toDate(startDate, Config.DATE_FORMAT.concat("|").concat(Config.getInstance().HOUR_FORMAT()));
        //selectedItem.setStart(sDate);

        //selectedItem.setAllDay(checkBoxAllDay.isChecked());

        //String endDate = editEndDate.getText().toString().concat("|").concat(editEndTime.getText().toString());
        //Date eDate = selectedItem.toDate(endDate, Config.DATE_FORMAT.concat("|").concat(Config.HOUR_FORMAT));
        //selectedItem.setEnd(eDate);

        int repeatNumber = (checkBoxSunday.isChecked() ? 1 : 0) +
                (checkBoxMonday.isChecked() ? 2 : 0) +
                (checkBoxTuesday.isChecked() ? 4 : 0) +
                (checkBoxWednsesday.isChecked() ? 8 : 0) +
                (checkBoxThursday.isChecked() ? 16 : 0) +
                (checkBoxFriday.isChecked() ? 32 : 0) +
                (checkBoxSaturday.isChecked() ? 64 : 0);

        selectedItem.setRepeatBit(repeatNumber);


    }

    private void createUpdate() {
        if (selectedItem == null || selectedItem.get_id() < 0) return;
        toModel();
        long id;
        boolean exist = CRUD.getInstance().selectById(Event.class, selectedItem.get_id()) != null;
        if (exist) {
            id = selectedItem.get_id();
            CRUD.getInstance().update(selectedItem, "_id=?", "" + selectedItem.get_id());
        } else {
            id = CRUD.getInstance().insert(selectedItem);
            selectedItem.set_id(id);
        }
    }

}
