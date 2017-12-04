package top.moverco.common.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.qiujuer.genius.ui.widget.Loading;

import top.moverco.common.R;
import top.moverco.common.common.app.Application;
import top.moverco.common.common.widget.convention.PlaceHolderView;

/**
 * @author Jamal
 */

public class EmptyView extends LinearLayout implements PlaceHolderView {
    private ImageView mEmptyImg;
    private TextView mStatusText;
    private Loading mLoading;

    private int[] mDrawableIds = new int[]{0, 0};
    private int[] mTextIds = new int[]{0, 0, 0};

    private View[] mBindViews;

    public EmptyView(Context context) {
        super(context);
        init(null, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.lay_empty, this);
        mEmptyImg = (ImageView) findViewById(R.id.im_empty);
        mStatusText = (TextView) findViewById(R.id.txt_empty);
        mLoading = (Loading) findViewById(R.id.loading);

        final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyView, defStyle, 0);
        try {
            mDrawableIds[0] = array.getInt(R.styleable.EmptyView_comEmptyDrawable, R.drawable.status_empty);
            mDrawableIds[1] = array.getInt(R.styleable.EmptyView_comErrorDrawable, R.drawable.status_empty);
            mTextIds[0] = array.getInt(R.styleable.EmptyView_comEmptyText, R.string.prompt_empty);
            mTextIds[1] = array.getInt(R.styleable.EmptyView_comErrorText, R.string.prompt_empty);
            mTextIds[2] = array.getInt(R.styleable.EmptyView_comLoadingText, R.string.prompt_empty);
        } finally {
            array.recycle();
        }

    }

    public void bindViews(View... views) {
        this.mBindViews = views;
    }

    private void changeBindViewVisibility(int visible) {
        final View[] views = mBindViews;
        if (views == null || views.length == 0) {
            return;
        }
        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    @Override
    public void triggerEmpty() {
        mLoading.setVisibility(GONE);
        mLoading.stop();
        mEmptyImg.setImageResource(mDrawableIds[0]);
        mStatusText.setText(mTextIds[0]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerNetError() {
        mLoading.setVisibility(GONE);
        mLoading.stop();
        mEmptyImg.setImageResource(mDrawableIds[1]);
        mStatusText.setText(mTextIds[1]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerError(int strRes) {
        Application.showToast(strRes);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerLoading() {
        mEmptyImg.setVisibility(GONE);
        mStatusText.setText(mTextIds[2]);
        setVisibility(VISIBLE);
        mLoading.setVisibility(VISIBLE);
        mLoading.start();
        changeBindViewVisibility(GONE);
    }

    @Override
    public void triggerOk() {
        setVisibility(GONE);
        changeBindViewVisibility(VISIBLE);

    }

    @Override
    public void triggerOkOrEmpty(boolean isOk) {
        if (isOk) {
            triggerOk();
        } else triggerEmpty();
    }
}
