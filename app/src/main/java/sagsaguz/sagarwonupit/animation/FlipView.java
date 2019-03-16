package sagsaguz.sagarwonupit.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import sagsaguz.sagarwonupit.R;

public class FlipView extends FrameLayout {

    public static final int DEFAULT_FLIP_DURATION = 300;
    private int animFlipHorizontalOutId = R.animator.flip_right;
    private int animFlipHorizontalInId = R.animator.flip_left;

    public enum FlipState {
        FRONT_SIDE, BACK_SIDE
    }

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    private boolean flipOnTouch;
    private int flipDuration;
    private boolean flipEnabled;

    private Context context;
    private float x1;
    private float y1;

    private FlipState mFlipState = FlipState.FRONT_SIDE;

    private OnFlipAnimationListener onFlipListener = null;

    public FlipView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public FlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Setting Defaul Values
        flipOnTouch = true;
        flipDuration = DEFAULT_FLIP_DURATION;
        flipEnabled = true;

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            final TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.flip_view, 0, 0);
            try {
                flipOnTouch = attrArray.getBoolean(R.styleable.flip_view_flipOnTouch, true);
                flipDuration = attrArray.getInt(R.styleable.flip_view_flipDuration, DEFAULT_FLIP_DURATION);
                flipEnabled = attrArray.getBoolean(R.styleable.flip_view_flipEnabled, true);

                } finally {
                attrArray.recycle();
            }
        }

        loadAnimations();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 2) {
            throw new IllegalStateException("FlipView can host only two direct children!");
        }

        findViews();
        changeCameraDistance();
    }

    @Override
    public void addView(View v, int pos, ViewGroup.LayoutParams params) {
        if (getChildCount() == 2) {
            throw new IllegalStateException("FlipView can host only two direct children!");
        }

        super.addView(v, pos, params);

        findViews();
        changeCameraDistance();
    }

    @Override
    public void removeView(View v) {
        super.removeView(v);

        findViews();
    }

    @Override
    public void removeAllViewsInLayout() {
        super.removeAllViewsInLayout();

        // Reset the state
        mFlipState = FlipState.FRONT_SIDE;

        findViews();
    }

    private void findViews() {
        // Invalidation since we use this also on removeView
        mCardBackLayout = null;
        mCardFrontLayout = null;

        int childs = getChildCount();
        if (childs < 1) {
            return;
        }

        if (childs < 2) {
            // Only invalidate flip state if we have a single child
            mFlipState = FlipState.FRONT_SIDE;

            mCardFrontLayout = getChildAt(0);
        } else if (childs == 2) {
            mCardFrontLayout = getChildAt(1);
            mCardBackLayout = getChildAt(0);
        }

        if (!isFlipOnTouch()) {
            mCardFrontLayout.setVisibility(VISIBLE);

            if (mCardBackLayout != null) {
                mCardBackLayout.setVisibility(GONE);
            }
        }
    }

    private void loadAnimations() {

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, animFlipHorizontalOutId);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, animFlipHorizontalInId);
        if (mSetRightOut == null || mSetLeftIn == null) {
            throw new RuntimeException("No Animations Found! Please set Flip in and Flip out animation Ids.");
        }

        mSetRightOut.removeAllListeners();
        mSetRightOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (mFlipState == FlipState.FRONT_SIDE) {
                    mCardBackLayout.setVisibility(GONE);
                    mCardFrontLayout.setVisibility(VISIBLE);

                    if (onFlipListener != null)
                        onFlipListener.onViewFlipCompleted(FlipView.this, FlipState.FRONT_SIDE);
                } else {
                    mCardBackLayout.setVisibility(VISIBLE);
                    mCardFrontLayout.setVisibility(GONE);

                    if (onFlipListener != null)
                        onFlipListener.onViewFlipCompleted(FlipView.this, FlipState.BACK_SIDE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        setFlipDuration(flipDuration);

    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;

        if (mCardFrontLayout != null) {
            mCardFrontLayout.setCameraDistance(scale);
        }
        if (mCardBackLayout != null) {
            mCardBackLayout.setCameraDistance(scale);
        }
    }

    /**
     * Play the animation of flipping and flip the view for one side!
     */
    public void flipTheView() {
        if (!flipEnabled || getChildCount() < 2) return;

        if (mSetRightOut.isRunning() || mSetLeftIn.isRunning()) return;

        mCardBackLayout.setVisibility(VISIBLE);
        mCardFrontLayout.setVisibility(VISIBLE);

        if (mFlipState == FlipState.FRONT_SIDE) {
            // From front to back
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mFlipState = FlipState.BACK_SIDE;
        } else {
            // From back to front
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mFlipState = FlipState.FRONT_SIDE;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isEnabled() && flipOnTouch) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    float x2 = event.getX();
                    float y2 = event.getY();
                    float dx = x2 - x1;
                    float dy = y2 - y1;
                    float MAX_CLICK_DISTANCE = 0.5f;
                    if ((dx >= 0 && dx < MAX_CLICK_DISTANCE) && (dy >= 0 && dy < MAX_CLICK_DISTANCE)) {
                        flipTheView();
                    }
                    return true;
            }
        } else {
            return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * Whether view is set to flip on touch or not.
     *
     * @return true or false
     */
    public boolean isFlipOnTouch() {
        return flipOnTouch;
    }

    /**
     * Sets the flip duration (in milliseconds)
     *
     * @param flipDuration duration in milliseconds
     */
    public void setFlipDuration(int flipDuration) {
        this.flipDuration = flipDuration;

        mSetRightOut.getChildAnimations().get(0).setDuration(flipDuration);
        mSetRightOut.getChildAnimations().get(1).setStartDelay(flipDuration / 2);

        mSetLeftIn.getChildAnimations().get(1).setDuration(flipDuration);
        mSetLeftIn.getChildAnimations().get(2).setStartDelay(flipDuration / 2);
    }

    /**
     * The Flip Animation Listener for animations and flipping complete listeners
     */
    public interface OnFlipAnimationListener {
        /**
         * Called when flip animation is completed.
         *
         * @param flipView The current EasyFlipView instance
         * @param newCurrentSide After animation, the new side of the view. Either can be
         *                       FlipState.FRONT_SIDE or FlipState.BACK_SIDE
         */
        void onViewFlipCompleted(FlipView flipView, FlipState newCurrentSide);
    }
}
