package com.identity.arx.progressbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class SunBabyLoadingView extends View {
    private static final String BG_COLOR = "#00FFFFFF";
    private static final int DEFAULT_DIAMETER_SIZE = 120;
    private static final int DEFAULT_OFFSET_Y = 20;
    private static final String PAINT_COLOR = "#ffa500";
    private static final float RATIO_ARC_START_X = 0.4f;
    private static final float RATIO_LINE_START_X = 0.8333333f;
    private static final float RATIO_LINE_START_Y = 0.75f;
    private static final float SPACE_SUNSHINE = 12.0f;
    private static final float SUNSHINE_LINE_LENGTH = 15.0f;
    private static final float SUNSHINE_RISE_HEIGHT = 12.0f;
    private static final float SUNSHINE_SEPARATIO_ANGLE = 45.0f;
    private static final float SUN_EYES_RADIUS = 6.0f;
    private static final String TAG = "SunBaby";
    private Paint bgPaint;
    private Paint eyePaint;
    private boolean isDrawEyes;
    private float lineLength;
    private float lineStartX;
    private float lineStartY;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private float maxEyesTurn;
    private float offsetAngle;
    private float offsetSpin;
    private float offsetY;
    private boolean once;
    private float orectBottom;
    private float orectLeft;
    private float orectRight;
    private float orectTop;
    private RectF rectF;
    private Paint sunPaint;
    private float sunRadius;
    private double sunshineStartX;
    private double sunshineStartY;
    private double sunshineStopX;
    private double sunshineStopY;
    private float tempOffsetY;
    private float textX;
    private float textY;
    private float turnOffsetX;

    class C08233 extends AnimatorListenerAdapter {

        class C08221 extends AnimatorListenerAdapter {

            class C08211 extends AnimatorListenerAdapter {
                C08211() {
                }

                public void onAnimationEnd(Animator animation) {
                    SunBabyLoadingView.this.initTurnEyesLeftAnimator().start();
                }
            }

            C08221() {
            }

            public void onAnimationEnd(Animator animation) {
                ValueAnimator blink1Anima = SunBabyLoadingView.this.initBlink1Animator();
                blink1Anima.addListener(new C08211());
                blink1Anima.start();
            }
        }

        C08233() {
        }

        public void onAnimationEnd(Animator animation) {
            ValueAnimator turnEyesRightAnima = SunBabyLoadingView.this.initTurnEyesRightAnimator();
            turnEyesRightAnima.start();
            turnEyesRightAnima.addListener(new C08221());
        }
    }

    class C08255 extends AnimatorListenerAdapter {
        C08255() {
        }

        public void onAnimationEnd(Animator animation) {
            SunBabyLoadingView.this.once = true;
        }
    }

    class C08266 implements AnimatorUpdateListener {
        C08266() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            SunBabyLoadingView.this.offsetY = SunBabyLoadingView.this.tempOffsetY - Float.parseFloat(animation.getAnimatedValue().toString());
            SunBabyLoadingView.this.calcAndSetRectPoint();
            SunBabyLoadingView.this.calcOffsetAngle();
            SunBabyLoadingView.this.postInvalidate();
        }
    }

    class C08277 extends AnimatorListenerAdapter {
        C08277() {
        }

        public void onAnimationEnd(Animator animation) {
            SunBabyLoadingView.this.tempOffsetY = SunBabyLoadingView.this.offsetY;
        }
    }

    class C08288 implements AnimatorUpdateListener {
        C08288() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            SunBabyLoadingView.this.turnOffsetX = Float.parseFloat(animation.getAnimatedValue().toString());
            SunBabyLoadingView.this.postInvalidate();
        }
    }

    class C08299 implements AnimatorUpdateListener {
        C08299() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            int animaValue = Integer.parseInt(animation.getAnimatedValue().toString());
            if (animaValue == 0) {
                SunBabyLoadingView.this.isDrawEyes = false;
            } else if (animaValue == 1) {
                SunBabyLoadingView.this.isDrawEyes = true;
            }
            SunBabyLoadingView.this.postInvalidate();
        }
    }

    public SunBabyLoadingView(Context context) {
        this(context, null);
    }

    public SunBabyLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunBabyLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.once = true;
        this.isDrawEyes = true;
        this.offsetY = 20.0f;
        this.tempOffsetY = this.offsetY;
        setLayerType(1, null);
        setBackgroundColor(Color.parseColor(BG_COLOR));
        initRes();
    }

    private void initRes() {
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeWidth(5.0f);
        this.mPaint.setColor(Color.parseColor(PAINT_COLOR));
        this.sunPaint = new Paint(1);
        this.sunPaint.setStyle(Style.STROKE);
        this.sunPaint.setStrokeWidth(10.0f);
        this.sunPaint.setColor(Color.parseColor(PAINT_COLOR));
        this.eyePaint = new Paint(1);
        this.eyePaint.setStyle(Style.FILL);
        this.eyePaint.setStrokeCap(Cap.ROUND);
        this.eyePaint.setStrokeJoin(Join.ROUND);
        this.eyePaint.setStrokeWidth(1.0f);
        this.eyePaint.setColor(Color.parseColor(PAINT_COLOR));
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.setStyle(Style.FILL_AND_STROKE);
        this.mTextPaint.setStrokeWidth(1.0f);
        this.mTextPaint.setTextSize(20.0f);
        this.mTextPaint.setColor(Color.parseColor(PAINT_COLOR));
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.bgPaint = new Paint(1);
        this.bgPaint.setStyle(Style.FILL);
        this.bgPaint.setStrokeCap(Cap.ROUND);
        this.bgPaint.setStrokeJoin(Join.ROUND);
        this.bgPaint.setStrokeWidth(1.0f);
        this.bgPaint.setColor(Color.parseColor(BG_COLOR));
        this.rectF = new RectF();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Resources r = Resources.getSystem();
        if (widthMode == 0 || widthMode == Integer.MIN_VALUE) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) TypedValue.applyDimension(1, 120.0f, r.getDisplayMetrics()), 1073741824);
        }
        if (heightMode == 0 || heightMode == Integer.MIN_VALUE) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) TypedValue.applyDimension(1, 120.0f, r.getDisplayMetrics()), 1073741824);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int height = getHeight();
        this.lineLength = ((float) width) * RATIO_LINE_START_X;
        this.lineStartX = (((float) width) - this.lineLength) * 0.5f;
        this.lineStartY = ((float) height) * RATIO_LINE_START_Y;
        this.textX = ((float) width) * 0.5f;
        this.textY = (this.lineStartY + ((((float) height) - this.lineStartY) * 0.5f)) + (Math.abs(this.mTextPaint.descent() + this.mTextPaint.ascent()) * 0.5f);
        this.sunRadius = (this.lineLength - (this.lineLength * RATIO_ARC_START_X)) * 0.5f;
        this.maxEyesTurn = (this.sunRadius + (this.sunPaint.getStrokeWidth() * 0.5f)) * 0.5f;
        calcAndSetRectPoint();
        calcOffsetAngle();
        initAnimaDriver();
    }

    private void calcOffsetAngle() {
        this.offsetAngle = (float) ((Math.asin((double) (this.offsetY / this.sunRadius)) * 180.0d) / 3.141592653589793d);
    }

    private void calcAndSetRectPoint() {
        float rectLeft = (this.lineStartX + (this.lineLength * 0.5f)) - this.sunRadius;
        float rectTop = (this.lineStartY - this.sunRadius) + this.offsetY;
        this.rectF.set(rectLeft, rectTop, (this.lineLength - rectLeft) + (this.lineStartX * 2.0f), rectTop + (this.sunRadius * 2.0f));
    }

    private void initAnimaDriver() {
        startSpinAnima();
        final ValueAnimator rise1SlowAnima = initRise1Animator();
        rise1SlowAnima.addListener(new AnimatorListenerAdapter() {

            class C08171 extends AnimatorListenerAdapter {
                C08171() {
                }

                public void onAnimationEnd(Animator animation) {
                    SunBabyLoadingView.this.playRisedEyesAnimator();
                    SunBabyLoadingView.this.playRisedCyclingAnimator(rise1SlowAnima);
                }
            }

            public void onAnimationEnd(Animator animation) {
                ValueAnimator riseFastAnima = SunBabyLoadingView.this.initRiseFastAnimator();
                riseFastAnima.start();
                riseFastAnima.addListener(new C08171());
            }
        });
        rise1SlowAnima.start();
    }

    private void playRisedCyclingAnimator(final ValueAnimator rise1SlowAnima) {
        ValueAnimator rise2SlowAnima = initRise2SlowAnimator();
        rise2SlowAnima.start();
        rise2SlowAnima.addListener(new AnimatorListenerAdapter() {

            class C08191 extends AnimatorListenerAdapter {
                C08191() {
                }

                public void onAnimationEnd(Animator animation) {
                    rise1SlowAnima.start();
                }
            }

            public void onAnimationEnd(Animator animation) {
                ValueAnimator sinkAnima = SunBabyLoadingView.this.initSinkAnimator();
                sinkAnima.start();
                sinkAnima.addListener(new C08191());
            }
        });
    }

    private void playRisedEyesAnimator() {
        ValueAnimator blink2Anima = initBlink2Animator();
        blink2Anima.start();
        blink2Anima.addListener(new C08233());
    }

    @NonNull
    private ValueAnimator initSinkAnimator() {
        final float middleValue = (20.0f - this.tempOffsetY) * 0.5f;
        this.orectLeft = this.rectF.left;
        this.orectTop = this.rectF.top;
        this.orectRight = this.rectF.right;
        this.orectBottom = this.rectF.bottom;
        ValueAnimator sinkAnima = ValueAnimator.ofFloat(new float[]{0.0f, 0.0f});
        sinkAnima.setDuration(200);
        sinkAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        sinkAnima.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float animaValue = Float.parseFloat(animation.getAnimatedValue().toString());
                float ratioValue;
                if (animaValue < middleValue) {
                    ratioValue = animaValue * 0.5f;
                    SunBabyLoadingView.this.rectF.set(SunBabyLoadingView.this.orectLeft + ratioValue, SunBabyLoadingView.this.orectTop + animaValue, SunBabyLoadingView.this.orectRight - ratioValue, SunBabyLoadingView.this.orectBottom + animaValue);
                } else {
                    if (SunBabyLoadingView.this.once) {
                        SunBabyLoadingView.this.orectLeft = SunBabyLoadingView.this.rectF.left;
                        SunBabyLoadingView.this.orectTop = SunBabyLoadingView.this.rectF.top;
                        SunBabyLoadingView.this.orectRight = SunBabyLoadingView.this.rectF.right;
                        SunBabyLoadingView.this.orectBottom = SunBabyLoadingView.this.rectF.bottom;
                        SunBabyLoadingView.this.once = false;
                    }
                    ratioValue = (animaValue - middleValue) * 0.5f;
                    SunBabyLoadingView.this.rectF.set(SunBabyLoadingView.this.orectLeft - ratioValue, SunBabyLoadingView.this.orectTop + animaValue, SunBabyLoadingView.this.orectRight + ratioValue, SunBabyLoadingView.this.orectBottom + animaValue);
                }
                SunBabyLoadingView.this.offsetY = SunBabyLoadingView.this.tempOffsetY + animaValue;
                SunBabyLoadingView.this.calcOffsetAngle();
                SunBabyLoadingView.this.postInvalidate();
            }
        });
        sinkAnima.addListener(new C08255());
        return sinkAnima;
    }

    @NonNull
    private ValueAnimator initRise2SlowAnimator() {
        ValueAnimator rise2SlowAnima = ValueAnimator.ofFloat(new float[]{0.0f, 18.0f});
        rise2SlowAnima.setDuration(3000);
        rise2SlowAnima.setInterpolator(new LinearInterpolator());
        rise2SlowAnima.addUpdateListener(new C08266());
        rise2SlowAnima.addListener(new C08277());
        return rise2SlowAnima;
    }

    @NonNull
    private ValueAnimator initTurnEyesLeftAnimator() {
        ValueAnimator turnEyesLeftAnima = ValueAnimator.ofFloat(new float[]{this.maxEyesTurn, 0.0f});
        turnEyesLeftAnima.setStartDelay(800);
        turnEyesLeftAnima.setDuration(150);
        turnEyesLeftAnima.addUpdateListener(new C08288());
        return turnEyesLeftAnima;
    }

    @NonNull
    private ValueAnimator initBlink1Animator() {
        ValueAnimator blink1Anima = ValueAnimator.ofInt(new int[]{0, 1});
        blink1Anima.setInterpolator(new LinearInterpolator());
        blink1Anima.setStartDelay(600);
        blink1Anima.setDuration(100);
        blink1Anima.addUpdateListener(new C08299());
        return blink1Anima;
    }

    @NonNull
    private ValueAnimator initTurnEyesRightAnimator() {
        ValueAnimator turnEyesRightAnima = ValueAnimator.ofFloat(new float[]{0.0f, this.maxEyesTurn});
        turnEyesRightAnima.setStartDelay(200);
        turnEyesRightAnima.setDuration(150);
        turnEyesRightAnima.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SunBabyLoadingView.this.turnOffsetX = Float.parseFloat(animation.getAnimatedValue().toString());
                SunBabyLoadingView.this.postInvalidate();
            }
        });
        return turnEyesRightAnima;
    }

    @NonNull
    private ValueAnimator initBlink2Animator() {
        ValueAnimator blink2Anima = ValueAnimator.ofInt(new int[]{0, 1, 0, 1});
        blink2Anima.setInterpolator(new LinearInterpolator());
        blink2Anima.setStartDelay(400);
        blink2Anima.setDuration(500);
        blink2Anima.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int animaValue = Integer.parseInt(animation.getAnimatedValue().toString());
                if (animaValue == 0) {
                    SunBabyLoadingView.this.isDrawEyes = false;
                } else if (animaValue == 1) {
                    SunBabyLoadingView.this.isDrawEyes = true;
                }
                SunBabyLoadingView.this.postInvalidate();
            }
        });
        return blink2Anima;
    }

    @NonNull
    private ValueAnimator initRiseFastAnimator() {
        this.orectLeft = this.rectF.left;
        this.orectTop = this.rectF.top;
        this.orectRight = this.rectF.right;
        this.orectBottom = this.rectF.bottom;
        ValueAnimator riseFastAnima = ValueAnimator.ofFloat(new float[]{0.0f, 30.0f});
        riseFastAnima.setDuration(200);
        riseFastAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        riseFastAnima.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float animaValue = Float.parseFloat(animation.getAnimatedValue().toString());
                if (animaValue < SunBabyLoadingView.SUNSHINE_LINE_LENGTH) {
                    SunBabyLoadingView.this.rectF.set(SunBabyLoadingView.this.orectLeft - animaValue, SunBabyLoadingView.this.orectTop + animaValue, SunBabyLoadingView.this.orectRight + animaValue, SunBabyLoadingView.this.orectBottom - animaValue);
                } else {
                    if (SunBabyLoadingView.this.once) {
                        SunBabyLoadingView.this.orectLeft = SunBabyLoadingView.this.rectF.left;
                        SunBabyLoadingView.this.orectTop = SunBabyLoadingView.this.rectF.top;
                        SunBabyLoadingView.this.orectRight = SunBabyLoadingView.this.rectF.right;
                        SunBabyLoadingView.this.orectBottom = SunBabyLoadingView.this.rectF.bottom;
                        SunBabyLoadingView.this.once = false;
                    }
                    SunBabyLoadingView.this.rectF.set(SunBabyLoadingView.this.orectLeft + (animaValue - SunBabyLoadingView.SUNSHINE_LINE_LENGTH), SunBabyLoadingView.this.orectTop - (animaValue - SunBabyLoadingView.SUNSHINE_LINE_LENGTH), SunBabyLoadingView.this.orectRight - (animaValue - SunBabyLoadingView.SUNSHINE_LINE_LENGTH), SunBabyLoadingView.this.orectBottom + (animaValue - SunBabyLoadingView.SUNSHINE_LINE_LENGTH));
                }
                SunBabyLoadingView.this.offsetY = SunBabyLoadingView.this.tempOffsetY - animaValue;
                SunBabyLoadingView.this.calcOffsetAngle();
                SunBabyLoadingView.this.postInvalidate();
            }
        });
        riseFastAnima.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SunBabyLoadingView.this.tempOffsetY = SunBabyLoadingView.this.offsetY;
                SunBabyLoadingView.this.once = true;
            }
        });
        return riseFastAnima;
    }

    @NonNull
    private ValueAnimator initRise1Animator() {
        ValueAnimator rise1SlowAnima = ValueAnimator.ofFloat(new float[]{0.0f, 12.0f});
        rise1SlowAnima.setDuration(2500);
        rise1SlowAnima.setInterpolator(new LinearInterpolator());
        rise1SlowAnima.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SunBabyLoadingView.this.offsetY = 20.0f - Float.parseFloat(animation.getAnimatedValue().toString());
                SunBabyLoadingView.this.calcAndSetRectPoint();
                SunBabyLoadingView.this.calcOffsetAngle();
                SunBabyLoadingView.this.postInvalidate();
            }
        });
        rise1SlowAnima.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                SunBabyLoadingView.this.tempOffsetY = SunBabyLoadingView.this.offsetY;
            }
        });
        return rise1SlowAnima;
    }

    private void startSpinAnima() {
        ValueAnimator spinAnima = ValueAnimator.ofFloat(new float[]{0.0f, 360.0f});
        spinAnima.setRepeatCount(-1);
        spinAnima.setDuration(24000);
        spinAnima.setInterpolator(new LinearInterpolator());
        spinAnima.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                SunBabyLoadingView.this.offsetSpin = Float.parseFloat(animation.getAnimatedValue().toString());
                SunBabyLoadingView.this.postInvalidate();
            }
        });
        spinAnima.start();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Canvas canvas2 = canvas;
        canvas2.drawLine(this.lineStartX, this.lineStartY, this.lineLength + this.lineStartX, this.lineStartY, this.mPaint);
        canvas.drawArc(this.rectF, this.offsetAngle - 0.024902344f, 180.0f - (this.offsetAngle * 2.0f), false, this.sunPaint);
        if (this.isDrawEyes) {
            drawSunEyes(canvas);
        }
        drawSunshine(canvas);
        drawUnderLineView(canvas);
    }

    private void drawUnderLineView(Canvas canvas) {
        canvas.save();
        Canvas canvas2 = canvas;
        canvas2.drawRect(0.0f, (this.mPaint.getStrokeWidth() * 0.5f) + this.lineStartY, (float) getWidth(), (float) getHeight(), this.bgPaint);
        canvas.drawText("Please Wait...", this.textX, this.textY, this.mTextPaint);
        canvas.restore();
    }

    private void drawSunshine(Canvas canvas) {
        for (int a = 0; a <= 360; a = (int) (((float) a) + SUNSHINE_SEPARATIO_ANGLE)) {
            this.sunshineStartX = (Math.cos(Math.toRadians((double) (((float) a) + this.offsetSpin))) * ((double) ((this.sunRadius + 12.0f) + this.sunPaint.getStrokeWidth()))) + ((double) (((float) getWidth()) * 0.5f));
            this.sunshineStartY = ((Math.sin(Math.toRadians((double) (((float) a) + this.offsetSpin))) * ((double) ((this.sunRadius + 12.0f) + this.sunPaint.getStrokeWidth()))) + ((double) this.offsetY)) + ((double) this.lineStartY);
            this.sunshineStopX = (Math.cos(Math.toRadians((double) (((float) a) + this.offsetSpin))) * ((double) (((this.sunRadius + 12.0f) + SUNSHINE_LINE_LENGTH) + this.sunPaint.getStrokeWidth()))) + ((double) (((float) getWidth()) * 0.5f));
            this.sunshineStopY = ((Math.sin(Math.toRadians((double) (((float) a) + this.offsetSpin))) * ((double) (((this.sunRadius + 12.0f) + SUNSHINE_LINE_LENGTH) + this.sunPaint.getStrokeWidth()))) + ((double) this.offsetY)) + ((double) this.lineStartY);
            if (this.sunshineStartY <= ((double) this.lineStartY) && this.sunshineStopY <= ((double) this.lineStartY)) {
                canvas.drawLine((float) this.sunshineStartX, (float) this.sunshineStartY, (float) this.sunshineStopX, (float) this.sunshineStopY, this.mPaint);
            }
        }
    }

    private void drawSunEyes(Canvas canvas) {
        float lcx = ((((float) getWidth()) * 0.5f) - ((this.sunRadius + (this.sunPaint.getStrokeWidth() * 0.5f)) * 0.5f)) + this.turnOffsetX;
        float lcy = (this.lineStartY + this.offsetY) - SUN_EYES_RADIUS;
        if (lcy + SUN_EYES_RADIUS < this.lineStartY) {
            float rcx = (((float) getWidth()) * 0.5f) + this.turnOffsetX;
            float rcy = lcy;
            canvas.drawCircle(lcx, lcy, SUN_EYES_RADIUS, this.eyePaint);
            canvas.drawCircle(rcx, rcy, SUN_EYES_RADIUS, this.eyePaint);
        }
    }
}
