package com.identity.arx.student;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.identity.arx.R;
import com.identity.arx.general.ApplicationDialog;
import com.identity.arx.general.ConnectionDetector;
import com.identity.arx.general.MasterActivity;
import com.identity.arx.general.WebUrl;
import com.identity.arx.httpasynctask.AsyncResponse;
import com.identity.arx.httpasynctask.HttpAsyncTask;
import com.identity.arx.objectclass.RequestAttenReportVo;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttendanceReportActivity extends MasterActivity {
    String[] absentDates = new String[]{"2017-02-08", "2017-02-10"};
    List<String> absentlist;
    int courseId;
    private MaterialCalendarView materialCalendarView;
    int month;
    String[] noClassDates = new String[]{"2017-02-03"};
    List<String> noclassList;
    String[] presentDates;
    List<String> presentList;
    String rollNo;
    TextView textViewpresent;
    TextView textviewabsent;
    TextView textviewnoclass;
    int year;
    Calendar calendar;

    class C08301 implements OnMonthChangedListener {
        C08301() {
        }

        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            AttendanceReportActivity.this.getAttendanceService(date.getYear(), date.getMonth() + 1);
        }
    }

    class C08312 implements AsyncResponse {
        C08312() {
        }

        public void asyncResponse(ResponseEntity<?> response) {
            AttendanceReportActivity.this.presentDates = null;
            AttendanceReportActivity.this.absentDates = null;
            AttendanceReportActivity.this.noclassList = null;
            Iterator it = ((ArrayList) response.getBody()).iterator();
            while (it.hasNext()) {
                Map<String, List<String>> insList = (Map) it.next();
                AttendanceReportActivity.this.presentList = (List) insList.get("Present");
                AttendanceReportActivity.this.absentlist = (List) insList.get("Absent");
                AttendanceReportActivity.this.noclassList = (List) insList.get("NoClass");
            }
            try {
                AttendanceReportActivity.this.presentDates = new String[AttendanceReportActivity.this.presentList.size()];
                AttendanceReportActivity.this.absentDates = new String[AttendanceReportActivity.this.absentlist.size()];
                AttendanceReportActivity.this.noClassDates = new String[AttendanceReportActivity.this.noclassList.size()];
                AttendanceReportActivity.this.presentDates = (String[]) AttendanceReportActivity.this.presentList.toArray(AttendanceReportActivity.this.presentDates);
                AttendanceReportActivity.this.absentDates = (String[]) AttendanceReportActivity.this.absentlist.toArray(AttendanceReportActivity.this.absentDates);
                AttendanceReportActivity.this.noClassDates = (String[]) AttendanceReportActivity.this.noclassList.toArray(AttendanceReportActivity.this.noClassDates);
                AttendanceReportActivity.this.setCalanderReport();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C08323 implements OnClickListener {
        C08323() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AttendanceReportActivity.this.finish();
        }
    }

    private class PresentSelection implements DayViewDecorator {
        private HashSet<CalendarDay> mCalendarDayCollection;
        private int mColor;
        String typeAttandance;

        public PresentSelection(int color, HashSet<CalendarDay> calendarDayCollection, String typeAttandance) {
            this.mColor = color;
            this.mCalendarDayCollection = calendarDayCollection;
            this.typeAttandance = typeAttandance;
        }

        public boolean shouldDecorate(CalendarDay day) {
            return this.mCalendarDayCollection.contains(day);
        }

        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(this.mColor));
            if (this.typeAttandance.equalsIgnoreCase("PRESENT")) {
                view.setBackgroundDrawable(ContextCompat.getDrawable(AttendanceReportActivity.this.getApplicationContext(), R.drawable.dayselection));
            } else if (this.typeAttandance.equalsIgnoreCase("ABSENT")) {
                view.setBackgroundDrawable(ContextCompat.getDrawable(AttendanceReportActivity.this.getApplicationContext(), R.drawable.absentdayselection));
            } else {
                view.setBackgroundDrawable(ContextCompat.getDrawable(AttendanceReportActivity.this.getApplicationContext(), R.drawable.noclassday));
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_attendance_report);
        try {
            String courseName = getIntent().getStringExtra("COURSE_NAME");
            this.courseId = getIntent().getIntExtra("COURSE_id", 0);
            this.rollNo = getIntent().getStringExtra("rollNo");
            setActionBarTitle(courseName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        this.textViewpresent = (TextView) findViewById(R.id.textview_present);
        this.textviewabsent = (TextView) findViewById(R.id.textviewabsent);
        this.textviewnoclass = (TextView) findViewById(R.id.textviewnoclass);
        this.materialCalendarView.setSelectionMode(0);
        this.year = this.materialCalendarView.getCurrentDate().getYear();
        this.month = this.materialCalendarView.getCurrentDate().getMonth();
        this.materialCalendarView.setOnMonthChangedListener(new C08301());
    }

    protected void onResume() {
        super.onResume();
        getAttendanceService(this.year, this.month + 1);
    }

    void getAttendanceService(int year, int month) {
        if (ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            RequestAttenReportVo requestAttenReportVo = new RequestAttenReportVo();
            requestAttenReportVo.setYear(String.valueOf(year));
            requestAttenReportVo.setMonth(String.valueOf(month));
            requestAttenReportVo.setCourseId("" + this.courseId);
            requestAttenReportVo.setRollNumber(this.rollNo);
            requestAttenReportVo.setLoginStatus("1");
            new HttpAsyncTask(this, WebUrl.SERVER_ADDRESS + this.ins_id + "/getMonthlyStudentAttendance", requestAttenReportVo, new C08312()).execute(new ResponseEntity[0]);
            return;
        }
        ApplicationDialog.setMessage("Internet Issue", "PLease check your Internet Connection", "", "OK", this).buildDialog(new C08323());
    }

    private void setCalanderReport() {
        int i = 0;
        HashSet<CalendarDay> present = new HashSet();
        for (String dtStart : this.presentDates) {
            try {
                Date dateObj = new SimpleDateFormat("yyyy-MM-dd").parse(dtStart);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateObj);
                present.add(CalendarDay.from(calendar));
            } catch (Exception e) {
            }
        }
        this.textViewpresent.setText(this.presentDates.length + "");
        this.materialCalendarView.addDecorator(new PresentSelection(R.color.green_color, present, "PRESENT"));
        HashSet<CalendarDay> absent = new HashSet();
        Date dateObj;
        for (String dtStart2 : this.absentDates) {
            try {
                dateObj = new SimpleDateFormat("yyyy-MM-dd").parse(dtStart2);
                calendar = Calendar.getInstance();
                calendar.setTime(dateObj);
                absent.add(CalendarDay.from(calendar));
            } catch (Exception e2) {
            }
        }
        this.textviewabsent.setText(this.absentDates.length + "");
        this.materialCalendarView.addDecorator(new PresentSelection(R.color.textview_display_absent, absent, "ABSENT"));
        HashSet<CalendarDay> noclass = new HashSet();
        String[] strArr = this.noClassDates;
        int length = strArr.length;
        while (i < length) {
            try {
                dateObj = new SimpleDateFormat("yyyy-MM-dd").parse(strArr[i]);
                calendar = Calendar.getInstance();
                calendar.setTime(dateObj);
                noclass.add(CalendarDay.from(calendar));
            } catch (Exception e3) {
            }
            i++;
        }
        this.textviewnoclass.setText(this.noClassDates.length + "");
        this.materialCalendarView.addDecorator(new PresentSelection(R.color.button_normal, noclass, "NOCLASS"));
    }
}
