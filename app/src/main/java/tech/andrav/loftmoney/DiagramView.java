package tech.andrav.loftmoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DiagramView extends View {

    private float mExpences;
    private float mIncome;

    private Paint mExpencePaint = new Paint();
    private Paint mIncomePaint = new Paint();


    public DiagramView(Context context) {
        super(context);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    /*
        Вызывается при определения размеров View на экране.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /*
        Вызывается при расположении View на экране.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /*
        Рисует View на экране.
        Вызывается много раз при отрисовки.
        Поэтому в этом методе не желательно писать создание новых объектов.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = mExpences + mIncome;

        float expenceAngle = 360f*mExpences / total;
        float incomeAngle = 360f*mIncome / total;

        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenceAngle/2, expenceAngle, true, mExpencePaint);

        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomeAngle/2, incomeAngle,true, mIncomePaint);
    }


    /*
        custom method
     */
    public void update(float expences, float income) {
        mExpences = expences;
        mIncome = income;
        invalidate();   // вызывает перерисовку всего View, т.е. вызовется onDraw
    }

    /*
        custom method
     */
    private void init() {
        mExpencePaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_sky_blue));
        mIncomePaint.setColor(ContextCompat.getColor(getContext(), R.color.apple_green));
    }
}
