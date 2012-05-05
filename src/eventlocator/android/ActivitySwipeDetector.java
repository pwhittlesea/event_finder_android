package eventlocator.android;

import android.app.Dialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/*
 * From http://stackoverflow.com/questions/937313/android-basic-gesture-detection
 * Allows the user to "swipe away" Dialogs
 */
public class ActivitySwipeDetector implements View.OnTouchListener {

	static final String logTag = "ActivitySwipeDetector";
	static final int MIN_DISTANCE = 100;
	private float downX, downY, upX, upY;
	Dialog dialog;

	public ActivitySwipeDetector(Dialog dialog) {
		this.dialog = dialog;
	}

	public void onRightToLeftSwipe() {
		Log.i(logTag, "RightToLeftSwipe!");

		dialog.dismiss();
			}

	public void onLeftToRightSwipe() {
		Log.i(logTag, "LeftToRightSwipe!");
	//	dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		dialog.getWindow().setWindowAnimations(R.style.SwipeCloseDialogAnimation);
		dialog.dismiss();
		
	}

	public void onTopToBottomSwipe() {
		Log.i(logTag, "onTopToBottomSwipe!");
		dialog.dismiss();
	}

	public void onBottomToTopSwipe() {
		Log.i(logTag, "onBottomToTopSwipe!");
		dialog.dismiss();
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// swipe horizontal?
			if (Math.abs(deltaX) > MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {
					this.onLeftToRightSwipe();
					return true;
				}
				if (deltaX > 0) {
					this.onRightToLeftSwipe();
					return true;
				}
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaX)
						+ " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
			}

			// swipe vertical?
			if (Math.abs(deltaY) > MIN_DISTANCE) {
				// top or down
				if (deltaY < 0) {
					this.onTopToBottomSwipe();
					return true;
				}
				if (deltaY > 0) {
					this.onBottomToTopSwipe();
					return true;
				}
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaX)
						+ " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
			}

			return true;
		}
		}
		return false;
	}

}
