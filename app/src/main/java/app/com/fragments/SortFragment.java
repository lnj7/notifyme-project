package app.com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.com.notifyme.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
public class SortFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    public static SortFragment newInstance() {
        return new SortFragment();
    }
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sort_bottomsheet, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sort_priority).setOnClickListener(this);
        view.findViewById(R.id.sort_date).setOnClickListener(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onClick(View view) {
        if(view.getId()==R.id.sort_priority) {
            mListener.onPriorityClick();
        }
        else if(view.getId()==R.id.sort_date){
            mListener.onDateClick();
        }
        dismiss();
    }
    public interface ItemClickListener {
        void onPriorityClick();
        void onDateClick();
    }
}
