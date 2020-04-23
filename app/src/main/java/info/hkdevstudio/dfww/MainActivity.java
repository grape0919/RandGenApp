package info.hkdevstudio.dfww;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.*;

public class MainActivity extends Activity {

    private AdView mAdView;
    List<Integer> picked_list = new ArrayList<>();
    boolean[] check_arr = new boolean[1];
    int f = 0;
    int t = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText from = findViewById(R.id.from);
        final EditText to = findViewById(R.id.to);

        Button button = findViewById(R.id.drawButton);
        Button button_list = findViewById(R.id.num_list_button);
        Button lotto_button = findViewById(R.id.lottery);

        final TextView num = findViewById(R.id.number);
        //광고 코드
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        final View.OnClickListener range_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int view_id = v.getId();

                final TextView view_temp = findViewById(view_id);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.reset).setMessage("범위 변경 시 현재까지 뽑힘 숫자는 초기화 됩니다.");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view_temp.setText("");
                        picked_list.clear();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        };

        from.setOnClickListener(range_OnClickListener);
        to.setOnClickListener(range_OnClickListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(f != Integer.valueOf(String.valueOf(from.getText()))
                        || t != Integer.valueOf(String.valueOf(to.getText()))) {
                    f = Integer.valueOf(String.valueOf(from.getText()));
                    t = Integer.valueOf(String.valueOf(to.getText()));
                    if(f > t){
                        int temp = f;
                        f = t;
                        t = temp;
                    }
                    picked_list.clear();
                }
                if(picked_list.size() == 0){
                    check_arr = new boolean[t-f+1];
                }

                final Queue<Integer> rList = new LinkedList<>();
                final Queue<Integer> tList = new LinkedList<>();

                final Random random = new Random();

                for(int i = 1; i <= 50; i++){
                    int i_buff = random.nextInt((t - f) + 1) + f;
                    rList.add(i_buff);
                    tList.add(i * i + 500);
                }

                //11 ~ 20
                //20-11+1 = 10 [9]
                //is-f = 9
                //0~9
                //11~20
                boolean ch = true;
                for(boolean b : check_arr){
                    if(!(ch &= b)){
                        break;
                    }
                }
                if(!ch) {
                    int i_buff = 0;
                    do {
                        i_buff = random.nextInt((t - f) + 1) + f;
                    } while (picked_list.contains(i_buff));

                    check_arr[i_buff - f] = true;
                    picked_list.add(i_buff);

                    for (int i = 1; i <= 50; i++) {
                        Handler handler = new Handler();
                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (finalI == 50) {
                                    if (picked_list.size() != 0){
                                        num.setText(String.valueOf(picked_list.get(picked_list.size() - 1)));
                                    }else {
                                        int i_buff = 0;
                                        do {
                                            i_buff = random.nextInt((t - f) + 1) + f;
                                        } while (picked_list.contains(i_buff));

                                        check_arr[i_buff - f] = true;
                                        picked_list.add(i_buff);
                                        num.setText(String.valueOf(picked_list.get(picked_list.size() - 1)));
                                    }
                                } else {
                                    num.setText(String.valueOf(rList.poll()));
                                }
                            }
                        }, tList.poll());

                    }
                }else {
                    Toast.makeText(MainActivity.this, R.string.toast_cant_draw, Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreListDialog listDialog = new StoreListDialog(MainActivity.this);
                listDialog.show(picked_list);

            }
        });


        lotto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("초기화").setMessage("범위 변경 시 현재까지 뽑힘 숫자는 초기화 됩니다.");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        from.setText("1" );
                        to.setText("45");
                        picked_list.clear();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}