/*
 * NumberPickerPreference.java
 *
 * Preference for number picking for hour
 *
 * A {@link android.preference.Preference} that displays a number picker as a dialog.
 *
 * Source:
 * Class
 * http://stackoverflow.com/questions/20758986/android-preferenceactivity-dialog-with-number-picker
 *
 * Last Edit: 4-23-17
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

public class NumberPickerPreference extends DialogPreference {

    // Default string
    public static final String DEFAULT_VALUE = "15-0";

    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker pickerMin;
    private NumberPicker pickerHr;
    private String value;

    /**
     * Constructor
     * @param context Context view is called in
     * @param attrs Attributes for DialogPreference
     */
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context Context view is called in
     * @param attrs Attributes for DialogPreference
     * @param defStyleAttr Style attribute value
     */
    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Creates and returns the dialog view
     * This is the stuff in the middle of the pop-up like the numberpicker
     * @return dialogView
     */
    @Override
    protected View onCreateDialogView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(20,0,20,0);

        pickerHr = new NumberPicker(getContext());
        pickerHr.setLayoutParams(layoutParams);

        pickerMin = new NumberPicker(getContext());
        pickerMin.setLayoutParams(layoutParams);
        pickerMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        LinearLayout dialogView = new LinearLayout(getContext());
        dialogView.setOrientation(LinearLayout.HORIZONTAL);
        dialogView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        dialogView.setPadding(10,0,10,0);
        dialogView.setGravity(Gravity.CENTER);
        dialogView.addView(pickerHr);
        dialogView.addView(pickerMin);


        return dialogView;
    }

    /**
     * Sets up the dialogView including the NumberPicker
     * @param view The view
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        String[] vals = value.split("[-]");

        pickerHr.setMinValue(0);
        pickerHr.setMaxValue(23);
        pickerHr.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        pickerHr.setValue(Integer.parseInt(vals[0]));
        pickerHr.setFocusableInTouchMode(true);
        setNumberPickerTextColor(pickerHr);

        pickerMin.setMinValue(0);
        pickerMin.setMaxValue(59);
        pickerMin.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        pickerMin.setValue(Integer.parseInt(vals[1]));
        pickerMin.setFocusableInTouchMode(true);
        setNumberPickerTextColor(pickerMin);
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
            pickerHr.clearFocus();
            pickerMin.clearFocus();

            value = Integer.toString(pickerHr.getValue()) + "-" +
                    Integer.toString(pickerMin.getValue());
            if (callChangeListener(value)) {
                setValue(value);
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
        return a.getInt(index, 0);
    }

    /**
     * Sets the initial value to what was previously chosen
     * @param restorePersistedValue Whether to restore the value or set the default
     * @param defaultValue The default value to set it to if not restoring
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedString(DEFAULT_VALUE) : (String) DEFAULT_VALUE);
    }

    /**
     * Setter
     * Sets the chosen value
     * @param value The chosen value
     */
    public void setValue(String value) {
        this.value = value;
        persistString(this.value);
    }

    /**
     * Getter
     * Gets the chosen value
     * @return The chosen value
     */
    public String getValue() {
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
