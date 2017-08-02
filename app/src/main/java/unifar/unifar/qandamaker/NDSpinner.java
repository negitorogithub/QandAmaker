package unifar.unifar.qandamaker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by 三悟 on 2017/08/02.
 */

public class NDSpinner extends android.support.v7.widget.AppCompatSpinner {
    public boolean isDropDownMenuShown=false;

    public NDSpinner(Context context) {
        super(context);
    }

    public NDSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NDSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void
    setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public boolean performClick() {
        this.isDropDownMenuShown = true; //Flag to indicate the spinner menu is shown
        return super.performClick();
    }
    public boolean isDropDownMenuShown(){
        return isDropDownMenuShown;
    }
    public void setDropDownMenuShown(boolean isDropDownMenuShown){
        this.isDropDownMenuShown=isDropDownMenuShown;
    }
    @Override
    public void
    setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
