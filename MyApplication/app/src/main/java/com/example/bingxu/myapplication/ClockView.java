package com.example.bingxu.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public class ClockView extends View {

    private Thread refreshThread;//refresh thread

    private float refresh_time = 1000;//second to refresh


    private float width_circle = 5;//outside width of the clock
    private float width_longer = 5;//width of scale
    private float width_shorter = 3;
    private float length_longer = 60;
    private float length_shorter = 30;
    private float text_size = 60;//size of the character in the clock

    private float radius_center = 15;//radius_center

    private float width_hour = 20;//width of hour scale
    private float width_minutes = 10;//width of minute scale
    private float width_second = 8;//width of second scale


    private float density_second = 0.85f;
    private float density_minute = 0.70f;
    private float density_hour = 0.45f;


    private float mWidth = 1000;
    private float mHeight = 1000;

//    private double millSecond, second, minute, hour;
    private double millSecond = 0;
    private double second = 0;
    private double minute = 0;
    private double hour = 0;


    private double cmillSecond = 0;//compared second
    private double csecond = 0;
    private double cminute = 0;
    private double chour = 0;
    private double bsecond = 0;//before second
    private double bminute = 0;//before minute

    private double rsecond = 0;//resumed second
    private double rminute = 0;//resumed minute
    private double rhour = 0;//resumed hour

    private double ssecond = 0;//set second
    private double sminute = 0;//set minute
    private double shour = 0;//set hour


    private boolean ifStop = true;
    private boolean ifResume = false;
    private boolean ended = false;

    private static final String TAG = "MainActivity";


    public ClockView(Context context) {
        this(context, null, 0);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.clock);


        width_circle = ta.getDimension(R.styleable.clock_width_circle, 5);
        width_longer = ta.getDimension(R.styleable.clock_width_longer, 5);
        width_shorter = ta.getDimension(R.styleable.clock_width_shorter, 3);
        length_longer = ta.getDimension(R.styleable.clock_length_longer, 60);
        length_shorter = ta.getDimension(R.styleable.clock_length_shorter, 30);
        text_size = ta.getDimension(R.styleable.clock_text_size, 60);
        radius_center = ta.getDimension(R.styleable.clock_radius_center, 15);
        width_hour = ta.getDimension(R.styleable.clock_width_hour, 20);
        width_minutes = ta.getDimension(R.styleable.clock_density_minute, 10);
        width_second = ta.getDimension(R.styleable.clock_density_second, 8);
        density_second = ta.getFloat(R.styleable.clock_density_second, 0.85f);
        density_minute = ta.getFloat(R.styleable.clock_density_minute, 0.70f);
        density_hour = ta.getFloat(R.styleable.clock_density_hour, 0.45f);
        refresh_time = ta.getFloat(R.styleable.clock_refresh_time, 1000);
        ta.recycle();
    }


    private void init() {
        refreshThread = new Thread();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mWidth, (int) mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {



        this.mWidth = Math.min(getWidth(), getHeight());
        this.mHeight = Math.max(getWidth(), getHeight());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        if(second==ssecond && minute==sminute && hour==shour){
            ended = true;
        }

        if(!ifStop){
            if(calendar.get(Calendar.SECOND)<csecond){
                    second = (calendar.get(Calendar.SECOND)+60 - csecond + rsecond)%60;
            }else{
                    second = (calendar.get(Calendar.SECOND) - csecond + rsecond)%60;
            }
        }

        minute = rminute;
        hour = rhour;

        if(second==0 && bsecond==59){
            minute = (minute + 1) % 60;
            rminute = minute;
        }
        if(minute==0 && bminute==59){
            hour++;
            rhour = hour;
        }

        if(ended){
            second=ssecond;
            minute=sminute;
            hour=shour;
        }

//        Log.v(TAG,"Second:"+second);
//        Log.v(TAG,"SSecond:"+ssecond);

        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(width_circle);
        canvas.drawCircle(mWidth / 2,
                mHeight / 2, mWidth / 2 - width_circle, paintCircle);


        Paint painDegree = new Paint();
        painDegree.setAntiAlias(true);
        float lineLength = 0;
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                painDegree.setStrokeWidth(width_longer);
                lineLength = length_longer;
            } else {
                painDegree.setStrokeWidth(width_shorter);
                lineLength = length_shorter;
            }
            canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2 + width_circle, mWidth / 2, mHeight / 2 - mWidth / 2 + lineLength, painDegree);
            canvas.rotate(360 / 60, mWidth / 2, mHeight / 2);
        }

        painDegree.setTextSize(text_size);
        String targetText[] = getContext().getResources().getStringArray(R.array.clock);

        float startX = mWidth / 2 - painDegree.measureText(targetText[1]) / 2;
        float startY = mHeight / 2 - mWidth / 2 + 120;
        float textR = (float) Math.sqrt(Math.pow(mWidth / 2 - startX, 2) + Math.pow(mHeight / 2 - startY, 2));

        for (int i = 0; i < 12; i++) {
            float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
            float y = (float) (startY + textR - Math.cos(Math.PI / 6 * i) * textR);
            if (i != 11 && i != 10 && i != 0) {
                y = y + painDegree.measureText(targetText[i]) / 2;
            } else {
                x = x - painDegree.measureText(targetText[i]) / 4;
                y = y + painDegree.measureText(targetText[i]) / 4;
            }
            canvas.drawText(targetText[i], x, y, painDegree);
        }

        Paint paintSecond = new Paint();
        paintSecond.setAntiAlias(true);
        paintSecond.setStrokeWidth(width_second);
        paintSecond.setColor(Color.RED);
        drawSecond(canvas, paintSecond);
        bsecond = second;

        Paint paintMinute = new Paint();
        paintMinute.setAntiAlias(true);
        paintMinute.setStrokeWidth(width_minutes);
        drawMinute(canvas, paintMinute);
        bminute = minute;

        Paint paintHour = new Paint();
        paintHour.setAntiAlias(true);
        paintHour.setStrokeWidth(width_hour);
        drawHour(canvas, paintHour);

        Paint paintPointer = new Paint();
        paintPointer.setAntiAlias(true);
        paintPointer.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius_center, paintPointer);


    }

    private void drawSecond(Canvas canvas, Paint paint) {

        float degree = refresh_time > 1000 ? (float) (second * 360 / 60) : (float) (second * 360 / 60 + millSecond / 1000 * 360 / 60);
        canvas.rotate(degree, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 2 - width_circle) * density_second, paint);
        canvas.rotate(-degree, mWidth / 2, mHeight / 2);
    }

    private void drawMinute(Canvas canvas, Paint paint) {
        float degree = (float) (minute * 360 / 60);
        canvas.rotate(degree, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 2 - width_circle) * density_minute, paint);
        canvas.rotate(-degree, mWidth / 2, mHeight / 2);
    }

    private void drawHour(Canvas canvas, Paint paint) {
        float degreeHour = (float) hour * 360 / 12;
        float degreeMinut = (float) minute / 60 * 360 / 12;
        float degree = degreeHour + degreeMinut;
        canvas.rotate(degree, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - (mWidth / 2 - width_circle) * density_hour, paint);
        canvas.rotate(-degree, mWidth / 2, mHeight / 2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                   SystemClock.sleep((long) refresh_time);
                    postInvalidate();
                }
            }
        });
        refreshThread.start();
    }

    protected void start(){
        if(ended){
            stop();
        }
        ifStop = false;
        Calendar calendar = Calendar.getInstance();
        cmillSecond = calendar.get(Calendar.MILLISECOND);
        csecond = calendar.get(Calendar.SECOND);
        cminute = calendar.get(Calendar.MINUTE);
        chour = calendar.get(Calendar.HOUR);
        ifResume = false;

    }

    protected void resume(){
        ifStop = true;
        ifResume = true;
        rsecond = second;
        rminute = minute;
        rhour = hour;
    }

    protected void stop(){
        ifStop = true;
        second = 0;
        minute = 0;
        hour = 0;
        rsecond = 0;
        rminute = 0;
        rhour = 0;
        ifResume = false;
        ended = false;
    }

    protected void setTime(double ssecond, double sminute, double shour){
        this.ssecond = ssecond;
        this.sminute = sminute;
        this.shour = shour;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        refreshThread.interrupt();
    }
}