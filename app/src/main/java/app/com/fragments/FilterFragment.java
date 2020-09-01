package app.com.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.com.adapters.NoticeAdapter;
import app.com.models.Notice;
import app.com.notifyme.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {

    TextView apply,clear;
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    private CheckBox yearlevel,deptlevel, courselevel;
    private static Context ctx;
    private static View applyview;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static NoticeAdapter noticeAdapter;
    private static ArrayList<Notice> tempNotices, tempFilterList;
    public static FilterFragment newInstance(Context c, List<Notice> list, NoticeAdapter noticeAdapter) {
        FilterFragment.ctx=c;
        FilterFragment.noticeAdapter=noticeAdapter;
        FilterFragment.tempNotices = new ArrayList<>();
        FilterFragment.tempNotices.clear();
        FilterFragment.tempNotices.addAll(list);
        return new FilterFragment();
    }
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_bottomsheet, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tempFilterList = new ArrayList<>();
        yearlevel = view.findViewById(R.id.yearfilter);
        deptlevel = view.findViewById(R.id.deptfilter);
        courselevel = view.findViewById(R.id.coursefilter);
        pref = ctx.getSharedPreferences("UserVals", 0); // 0 - for private mode
        editor = pref.edit();
        if(pref.getBoolean("yearcheck",false)==true){
            yearlevel.setChecked(true);
            for(Notice notice: tempNotices){
                if(notice.getScope().equals("4")){
                    tempFilterList.add(notice);
                }
            }
            noticeAdapter.swap(tempFilterList);
        }
        if(pref.getBoolean("deptcheck",false)==true){
            for(Notice notice: tempNotices){
                if(notice.getScope().equals("2")){
                    tempFilterList.add(notice);
                }
            }
            noticeAdapter.swap(tempFilterList);
            deptlevel.setChecked(true);
        }
        if(pref.getBoolean("coursecheck",false)==true){
            for(Notice notice: tempNotices){
                if(notice.getScope().equals("3")){
                    tempFilterList.add(notice);
                }
            }
            noticeAdapter.swap(tempFilterList);
            courselevel.setChecked(true);
        }

        yearlevel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    int cn=0;
                    editor.putBoolean("yearcheck", b);
                    ArrayList<Notice>clickedAgain = new ArrayList<>();
                    clickedAgain.addAll(tempFilterList);
                    for (Notice notice : tempFilterList) {
                        if (notice.getScope().equals("4")) {
                            clickedAgain.remove(notice);
                        }
                    }
                    tempFilterList.clear();
                    tempFilterList.addAll(clickedAgain);

                        for (Notice notice : tempNotices) {
                            if (notice.getScope().equals("4")) {
                                tempFilterList.add(notice);
                            }
                        }
                        noticeAdapter.swap(tempFilterList);

                } else {
                    editor.putBoolean("yearcheck",b);
                    if(!deptlevel.isChecked() && !courselevel.isChecked()){
                        noticeAdapter.swap(tempNotices);
                    }else{
                        ArrayList<Notice>temp = new ArrayList<>();
                        for(Notice notice: tempFilterList){
                            if(!notice.getScope().equals("4")){
                                temp.add(notice);
                            }
                        }
                        tempFilterList.clear();
                        tempFilterList.addAll(temp);
                        noticeAdapter.swap(tempFilterList);
                    }
                }
                editor.commit();
            }
        });

        deptlevel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    int cn=0;
                    ArrayList<Notice>clickedAgain = new ArrayList<>();
                    clickedAgain.addAll(tempFilterList);
                    for (Notice notice : tempFilterList) {
                        if (notice.getScope().equals("2")) {
                            clickedAgain.remove(notice);
                        }
                    }
                    tempFilterList.clear();
                    tempFilterList.addAll(clickedAgain);
                    editor.putBoolean("deptcheck",b);
                    for(Notice notice: tempNotices){
                        if(notice.getScope().equals("2")){
                            tempFilterList.add(notice);
                        }
                    }
                    noticeAdapter.swap(tempFilterList);
                }
                else{
                    editor.putBoolean("deptcheck",b);
                    if(!yearlevel.isChecked() && !courselevel.isChecked()){
                        noticeAdapter.swap(tempNotices);
                    }else{
                        ArrayList<Notice>temp = new ArrayList<>();
                        for(Notice notice: tempFilterList){
                            if(!notice.getScope().equals("2")){
                                temp.add(notice);
                            }
                        }
                        tempFilterList.clear();
                        tempFilterList.addAll(temp);
                        noticeAdapter.swap(tempFilterList);
                    }
                }
                editor.commit();
            }
        });

        courselevel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    int cn=0;
                    ArrayList<Notice>clickedAgain = new ArrayList<>();
                    clickedAgain.addAll(tempFilterList);
                    for (Notice notice : tempFilterList) {
                        if (notice.getScope().equals("3")) {
                            clickedAgain.remove(notice);
                        }
                    }
                    tempFilterList.clear();
                    tempFilterList.addAll(clickedAgain);
                    editor.putBoolean("coursecheck",b);
                    for(Notice notice: tempNotices){
                        if(notice.getScope().equals("3")){
                            tempFilterList.add(notice);
                        }
                    }
                    noticeAdapter.swap(tempFilterList);
                }
                else{
                    editor.putBoolean("coursecheck",b);
                    if(!deptlevel.isChecked() && !yearlevel.isChecked()){
                        noticeAdapter.swap(tempNotices);
                    }else{
                        ArrayList<Notice>temp = new ArrayList<>();
                        for(Notice notice: tempFilterList){
                            if(!notice.getScope().equals("3")){
                                temp.add(notice);
                            }
                        }
                        tempFilterList.clear();
                        tempFilterList.addAll(temp);
                        noticeAdapter.swap(tempFilterList);
                    }
                }
                editor.commit();
            }
        });



        TextView clearView = view.findViewById(R.id.clearbutton);
        clearView.setClickable(true);
        clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("deptcheck",false);
                editor.putBoolean("yearcheck",false);
                editor.putBoolean("coursecheck",false);
                editor.commit();
                deptlevel.setChecked(false);
                yearlevel.setChecked(false);
                courselevel.setChecked(false);

                noticeAdapter.swap(tempNotices);
                dismiss();

            }
        });
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
        dismiss();
    }
    public interface ItemClickListener {
    }

}
