package to.msn.wings.anzan;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class SubActivity extends AppCompatActivity {
    //　フィールド変数
    public int sum = 0;     //　計算の合計値（答え）を表す変数を宣言、初期化
    public int onryou = 0;  //　音量を表す変数を宣言、初期化
    public boolean otonasi; //　消音を表す変数を宣言、消音ONはtrue、OFFはfalse

    public TextView txt;                    //　計算値や解答結果を表示するTextViewを宣言
    public Button btn_ans;                  //　「解答」ボタンのButtonを宣言
    public CountDownTimer countDownTimer;   //　カウントダウンを宣言
    public ToneGenerator mToneGenerator;    //　トーンジェネレーターを宣言
    public Handler handler = new Handler(); //　ハンドラーを宣言
    public Random random = new Random();    //　ランダムを宣言

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //　Buttonオブジェクトを取得
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        btn_ans = findViewById(R.id.btn_ans);
        //　TextViewオブジェクトを取得
        txt = findViewById(R.id.txt);
        TextView kaitou = findViewById(R.id.kaitou);
        //　EditTextオブジェクトを取得
        EditText anser = findViewById(R.id.anser);

        //　インテントから桁数・口数・速さ・消音・音量の値を受け取る
        int keta = getIntent().getIntExtra("keta", 0);
        int kuti = getIntent().getIntExtra("kuti", 0);
        int hayasa = getIntent().getIntExtra("hayasa", 0);
        otonasi = getIntent().getBooleanExtra("otonasi",true);
        onryou = getIntent().getIntExtra("onryou",0);

        //　画面起動時に計算を開始する
        keisan(keta, kuti, hayasa);

        //　「解答」ボタンを押した時の処理
        btn_ans.setOnClickListener(view -> {
            //　答えが「空白または0」じゃない時または答えが0じゃない（計算実行後）時に実行する
            if (!(anser.getText().toString().equals("") || Integer.parseInt(anser.getText().toString()) == 0 || sum == 0)) {
                //　文字のフォントを変更する　サイズ：100,色：赤
                txt.setTextSize(100);
                txt.setTextColor(Color.rgb(255,0,0));
                //　解答があっていれば「正解！」と表示、間違っていれば「残念！」と表示
                if (Integer.parseInt(anser.getText().toString()) == sum) txt.setText("  正解！");
                else txt.setText("  残念！");
                //　入力された解答と答えの値を表示する
                kaitou.setText("あなたの答え：" + anser.getText().toString() + "　解答：" + sum);
                //　解答欄と答えをリセットする
                anser.setText("");
                sum = 0;
            }
        });

        //　「同じ難易度で再挑戦」ボタンを押した時の処理
        btn1.setOnClickListener(view -> {
            //　カウントダウンタイマー及びハンドラーを停止させる
            countDownTimer.cancel();
            handler.removeCallbacksAndMessages(null);
            //　解答表示と解答欄をリセットする
            kaitou.setText("");
            anser.setText("");
            //　計算を開始する
            keisan(keta, kuti, hayasa);
        });

        //　「リセット」ボタンを押した時の処理
        btn2.setOnClickListener(view -> {
            //　カウントダウンタイマー及びハンドラーを停止させる
            countDownTimer.cancel();
            handler.removeCallbacksAndMessages(null);
            //　解答表示と解答欄、計算表示、答えをリセットする
            kaitou.setText("");
            anser.setText("");
            txt.setText("");
            sum = 0;
        });

        //　「難易度を変更する」ボタンを押した時の処理
        btn3.setOnClickListener(view -> {
            //　カウントダウンタイマー及びハンドラーを停止させる
            countDownTimer.cancel();
            handler.removeCallbacksAndMessages(null);
            //　サブ画面を修了し、メイン画面に戻る
            finish();
        });

    }

    //　計算前のカウントダウンおよび計算を行うメソッド
    public void keisan(int keta, int kuti, int hayasa) {
        //　カウントダウン中および計算中は「解答」ボタンを使用停止にする
        btn_ans.setEnabled(false);

        //　消音ONの場合は音量を0にする、OFFの場合はメイン画面で設定された音量にする
        if (otonasi){
            mToneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, 0);
        }else {
            mToneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, onryou);
        }

        //　カウントダウンタイマーを実行する　4秒間の間に1秒ごと処理を行う
        countDownTimer = new CountDownTimer(4000,1000){
            //　「あと何秒」の表示を行う
            @Override
            public void onTick(long millisUntilFinished){
                if ((int)millisUntilFinished/1000 > 0) {    //　残り時間が3〜1秒の時
                    //　文字のフォントを変更する　サイズ：80,色：白,フォント：デフォルト
                    txt.setTextSize(80);
                    txt.setTextColor(Color.WHITE);
                    txt.setTypeface(Typeface.DEFAULT);
                    //　「あと何秒」を表示し、表示ごとに音を鳴らす
                    txt.setText("あと" + millisUntilFinished / 1000 + "秒");
                    mToneGenerator.startTone(ToneGenerator.TONE_DTMF_A,50);
                }else {     //　残り時間が0秒の時
                    //　文字のフォントを計算表示用に変更する　サイズ：110,色：黄緑,フォント：イタリック
                    txt.setTextSize(110);
                    txt.setTextColor(Color.rgb(0,255,0));
                    txt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    //　「あと何秒」表示をリセットし、計算開始の音を鳴らす
                    txt.setText("");
                    mToneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                }
            }
            //　カウントダウン終了後に計算を実行する
            @Override
            public void onFinish(){
                //　計算値を入れる配列を宣言、口数で初期化
                int[] list = new int[kuti];
                //　入力された桁数からランダム生成時に使用する値を計算　入力された値が1の場合1、2の場合10、3の場合100となる
                int kt = (int) Math.pow(10, keta - 1);
                //　答えをリセットする
                sum = 0;

                //　口数回だけ数字を生成、表示、答えに加算　※最後の+1回は「解答してください」を表示する
                for (int i = 0; i < kuti + 1; i++) {
                    //　桁数に応じて数字をランダムに生成する
                    if (i != kuti) {
                        if (i != 0)     //　生成2回目以降
                            //　1つ前の数字と違う数字になっているか判定し、リストに加える
                            do {
                                list[i] = random.nextInt(9 * kt) + kt;
                            } while (list[i - 1] == list[i]);
                        else list[i] = random.nextInt(9 * kt) + kt;     //　生成1回目
                        //　生成した値を答えに加算する
                        sum += list[i];
                    }

                    //　ハンドラーに現在のi値を渡すための変数を宣言、iを入れる
                    int num = i;
                    handler.postDelayed(() -> {
                        if (num != kuti) {
                            //　生成された数字を表示し、音を鳴らす
                            txt.setText(String.valueOf(list[num]));
                            mToneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 50);
                        } else {    //　口数回、数字を表示し終えた後に実行
                            //　文字のフォントを変更する　サイズ：50,色：白,フォント：デフォルト
                            txt.setTextSize(50);
                            txt.setTextColor(Color.WHITE);
                            txt.setTypeface(Typeface.DEFAULT);
                            txt.setText("解答してください");
                            //　「解答」ボタンを使用可能にする
                            btn_ans.setEnabled(true);
                        }
                    }, i * hayasa * 1000L / kuti);      //　数字1つの表示スピード　全体の速さ÷口数
                }
            }
        }.start();
    }
}