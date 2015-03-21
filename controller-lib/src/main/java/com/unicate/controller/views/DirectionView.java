package com.unicate.controller.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import com.telly.mrvector.MrVector;
import com.unicate.controller.R;

/**
 * This View can be used for direction input on a game controller. it has 8 different directions,
 * as it was on older controllers.
 */
public class DirectionView extends InputView {

	private static final int BUTTON_COUNT = 8;

	// @formatter:off
	private static final int BUTTON_RIGHT 		= 0;
	private static final int BUTTON_DOWN_RIGHT 	= 1;
	private static final int BUTTON_DOWN 		= 2;
	private static final int BUTTON_DOWN_LEFT 	= 3;
	private static final int BUTTON_LEFT 		= 4;
	private static final int BUTTON_UP_LEFT 	= 5;
	private static final int BUTTON_UP 			= 6;
	private static final int BUTTON_UP_RIGHT 	= 7;
	// @formatter:on
	private DiagonalMode mode;
	private Drawable[][] drawables = new Drawable[BUTTON_COUNT][2];
	private int[][] resources = new int[BUTTON_COUNT][2];
	public DirectionView(Context context) {
		super(context);
	}

	public DirectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public DirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP) public DirectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (null != attrs) {
			TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DirectionView, 0, R.style.default_directionview);
			super.init(context, attrs, R.style.default_directionview);
			try {
				resources[BUTTON_RIGHT][0] = a.getResourceId(R.styleable.DirectionView_button_r_normal, 0);
				resources[BUTTON_RIGHT][1] = a.getResourceId(R.styleable.DirectionView_button_r_pressed, 0);
				resources[BUTTON_DOWN][0] = a.getResourceId(R.styleable.DirectionView_button_d_normal, 0);
				resources[BUTTON_DOWN][1] = a.getResourceId(R.styleable.DirectionView_button_d_pressed, 0);
				resources[BUTTON_LEFT][0] = a.getResourceId(R.styleable.DirectionView_button_l_normal, 0);
				resources[BUTTON_LEFT][1] = a.getResourceId(R.styleable.DirectionView_button_l_pressed, 0);
				resources[BUTTON_UP][0] = a.getResourceId(R.styleable.DirectionView_button_u_normal, 0);
				resources[BUTTON_UP][1] = a.getResourceId(R.styleable.DirectionView_button_u_pressed, 0);

				mode = DiagonalMode.values()[a.getInteger(R.styleable.DirectionView_diagonal_mode, DiagonalMode.BETWEEN.ordinal())];
				if (DiagonalMode.DRAWABLE.equals(mode)) {
					resources[BUTTON_DOWN_RIGHT][0] = a.getResourceId(R.styleable.DirectionView_button_dr_normal, 0);
					resources[BUTTON_DOWN_RIGHT][1] = a.getResourceId(R.styleable.DirectionView_button_dr_pressed, 0);
					resources[BUTTON_DOWN_LEFT][0] = a.getResourceId(R.styleable.DirectionView_button_dl_normal, 0);
					resources[BUTTON_DOWN_LEFT][1] = a.getResourceId(R.styleable.DirectionView_button_dl_pressed, 0);
					resources[BUTTON_UP_LEFT][0] = a.getResourceId(R.styleable.DirectionView_button_ul_normal, 0);
					resources[BUTTON_UP_LEFT][1] = a.getResourceId(R.styleable.DirectionView_button_ul_pressed, 0);
					resources[BUTTON_UP_RIGHT][0] = a.getResourceId(R.styleable.DirectionView_button_ur_normal, 0);
					resources[BUTTON_UP_RIGHT][1] = a.getResourceId(R.styleable.DirectionView_button_ur_pressed, 0);
				}
			} finally {
				a.recycle();
			}
		}
	}

	@Override protected Drawable getStateDrawable(int buttonIndex, ButtonState state) {
		// if diagonal mode is activated and one of the diagonal buttons are pressed
		if (DiagonalMode.BETWEEN.equals(mode) && (buttonIndex == 1 || buttonIndex == 3 || buttonIndex == 5 || buttonIndex == 7)) {
			return null;
		}
		if (null == drawables[buttonIndex][state.ordinal()]) {
			drawables[buttonIndex][state.ordinal()] = MrVector.inflate(getResources(), resources[buttonIndex][state.ordinal()]);
		}
		return drawables[buttonIndex][state.ordinal()];
	}

	@Override protected int forceDrawButtons(int buttonPressed) {
		if (DiagonalMode.BETWEEN.equals(mode)) {
			for (int i = 1; i < BUTTON_COUNT; i += 2) {
				if (((0x1 << i) & buttonPressed) > 0) {
					switch (i) {
						case BUTTON_DOWN_RIGHT:
							return buttonPressed | (0x1 << BUTTON_DOWN) | (0x1 << BUTTON_RIGHT);
						case BUTTON_DOWN_LEFT:
							return buttonPressed | (0x1 << BUTTON_DOWN) | (0x1 << BUTTON_LEFT);
						case BUTTON_UP_LEFT:
							return buttonPressed | (0x1 << BUTTON_UP) | (0x1 << BUTTON_LEFT);
						case BUTTON_UP_RIGHT:
							return buttonPressed | (0x1 << BUTTON_UP) | (0x1 << BUTTON_RIGHT);
					}
				}
			}
		}
		return buttonPressed;
	}

	@Override protected int getButtonCount() {
		return BUTTON_COUNT;
	}

	private enum DiagonalMode {
		DRAWABLE,
		BETWEEN
	}
}