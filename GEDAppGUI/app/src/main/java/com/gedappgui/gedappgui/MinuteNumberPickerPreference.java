/*
 * MinuteNumberPickerPreference.java
 *
 * Preference for number picking for minute
 *
 * A {@link android.preference.Preference} that displays a number picker as a dialog.
 *
 * Source:
 * Class
 * http://stackoverflow.com/questions/20758986/android-preferenceactivity-dialog-with-number-picker
 *
 * Last Edit: 4-9-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.preference.DialogPreference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

public class MinuteNumberPickerPreference extends DialogPreference {

    // allowed range
    public static final int MAX_VALUE = 59;
    public static final int MIN_VALUE = 0;
    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    private int value;

    /**
     * Constructor
     * @param context Context view is called in
     * @param attrs Attributes for DialogPreference
     */
    public MinuteNumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context Context view is called in
     * @param attrs Attributes for DialogPreference
     * @param defStyleAttr Style attribute value
     */
    public MinuteNumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Creates and returns the dialog view
     * This is the stuff in the middle of the pop-up like the numberpicker
     * @return dialogView
     */
    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    /**
     * Sets up the dialogView including the NumberPicker
     * @param view The view
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(MAX_VALUE);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue());
        picker.setFocusableInTouchMode(true);
        setNumberPickerTextColor(picker);
    }

    /**
     * Reciewves whether the "Ok" or the "Cancel" button was pushed
     * Acts accordingly
     * Saves the chosen value if "Ok" was clicked
     * @param positiveResult Reciewves whether the "OK" or the "Cancel" button was pushed
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    /**
     * Sets the default value
     * @param a Array of items that are in the picker
     * @param index Index in the array that is default
     * @return default value
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, MIN_VALUE);
    }

    /**
     * Sets the initial value to what was previously chosen
     * @param restorePersistedValue Whether to restore the value or set the default
     * @param defaultValue The default value to set it to if not restoring
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE) : (Integer) defaultValue);
    }

    /**
     * Setter
     * Sets the chosen value
     * @param value The chosen value
     */
    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    /**
     * Getter
     * Gets the chosen value
     * @return The chosen value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Sets the text color of the NumberPicker used
     * Source:
     * http://stackoverflow.com/questions/22962075/change-the-text-color-of-numberpicker
     * @param numberPicker
     * @return
     */
    public boolean setNumberPickerTextColor(NumberPicker numberPicker)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(
                            ContextCompat.getColor(this.getContext(), R.color.colorAccent));
                    ((EditText)child).setTextColor(
                            ContextCompat.getColor(this.getContext(), R.color.colorAccent));
                    ((EditText)child).setHintTextColor(
                            ContextCompat.getColor(this.getContext(), R.color.colorHint));
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                }
                catch(IllegalAccessException e){
                }
                catch(IllegalArgumentException e){
                }
            }
        }
        return false;
    }
}