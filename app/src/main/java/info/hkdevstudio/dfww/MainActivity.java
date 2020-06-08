package info.hkdevstudio.dfww;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.*;

public class MainActivity extends Activity implements RewardedVideoAdListener{
    private RewardedVideoAd mRewardedVideoAd;
    private AdView mAdView;
    List<Integer> picked_list = new ArrayList<>();
    boolean[] check_arr = new boolean[1];
    int f = 0;
    int t = 0;

    EditText from;
    EditText to;
    TextView num;

    Button lotto_auto;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);

        from = findViewById(R.id.from);
        to = findViewById(R.id.to);

        Button button = findViewById(R.id.drawButton);
        Button button_list = findViewById(R.id.num_list_button);
        Button lotto_button = findViewById(R.id.lottery);
        lotto_auto = findViewById(R.id.lottery_auto);

        num = findViewById(R.id.number);

        Button request_fnc = findViewById(R.id.req_fnc);
        super.onCreate(savedInstanceState);

        //광고 코드
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        final View.OnClickListener range_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                int view_id = v.getId();

                final TextView view_temp = findViewById(view_id);

                final AlertDialog.Builder builder = new AlertDialog.Builder(view_temp.getContext());

                builder.setTitle(R.string.reset).setMessage("범위 변경 시 현재까지 뽑힘 숫자는 초기화 됩니다.");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        picked_list.clear();
                        imm.showSoftInput(view_temp, 0);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imm.hideSoftInputFromWindow(view_temp.getWindowToken(), 0);
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

        from.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE) { // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        to.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) { // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        from.setOnClickListener(range_OnClickListener);
        to.setOnClickListener(range_OnClickListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawNumber();
            }
        });

        lotto_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("번호 받기").setMessage("광고보고 로또 번호 받으세요!");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        from.setText("1" );
                        to.setText("45");
                        picked_list.clear();

                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        }else{
                            Toast.makeText(MainActivity.this, R.string.ad_load_message, Toast.LENGTH_SHORT).show();
                        }

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

        request_fnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=info.hkdevstudio.dfww";
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public void drawNumber(){
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

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        for(int i = 0; i < 6; i++){
            drawNumber();
        }

        StoreListDialog listDialog = new StoreListDialog(MainActivity.this);
        listDialog.show(picked_list);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}