package cz.prasoprd.msglogger.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    public Integer getIndex() {
        return mIndex.getValue();
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }
}
