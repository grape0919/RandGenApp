package info.hkdevstudio.dfww;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class StoreListDialog {

    private Dialog dlg;
    private Button reset_button;
    private Button cancel;
    private LinearLayout picked_list;

    StoreListDialog(Context context){
        dlg = new Dialog(context);
        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.activity_store);
        dlg.setTitle("뽑힌 숫자");
    }

    void show(final List<Integer> list){
        Log.i("INFO","dlg.list : " + list);

        picked_list = dlg.findViewById(R.id.picked_list);
        reset_button = dlg.findViewById(R.id.reset_button);
        cancel = dlg.findViewById(R.id.cancel_button);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        params2.width = LinearLayout.LayoutParams.MATCH_PARENT;


        TextView dlg_title = new TextView(dlg.getContext());
        dlg_title.setLayoutParams(params2);
        dlg_title.setTextSize(20);
        dlg_title.setGravity(Gravity.CENTER);
        dlg_title.setText("뽑힌 숫자");

        picked_list.addView(dlg_title);

        for(int n : list){
            TextView viewBuff = new TextView(dlg.getContext());
            viewBuff.setText(String.valueOf(n));
            viewBuff.setLayoutParams(params2);
            viewBuff.setGravity(Gravity.CENTER);
            picked_list.addView(viewBuff);
        }

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                dlg.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });


        dlg.show();
    }

}
