package to.msn.wings.anzan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //　フィールド変数
    public int keta = 1;            //　桁数を表す変数を宣言、初期化
    public int kuti = 10;           //　口数を表す変数を宣言、初期化
    public int hayasa = 10;         //　速さ（秒）を表す変数を宣言、初期化
    public int onryou = 0;          //　音量を表す変数を宣言、初期化
    public boolean otonasi = true;  //　消音を表す変数を宣言、初期化、消音ONはtrue、OFFはfalse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //　Buttonオブジェクトを取得
        Button btn_re = findViewById(R.id.btn_re);
        Button btn_str = findViewById(R.id.btn_str);
        //　SeekBarオブジェクトを取得
        SeekBar seekBar_keta = findViewById(R.id.seekBar_keta);
        SeekBar seekBar_kuti = findViewById(R.id.seekBar_kuti);
        SeekBar seekBar_hayasa = findViewById(R.id.seekBar_hayasa);
        SeekBar seekBar_onryou = findViewById(R.id.seekBar_onryou);
        //　Switchオブジェクトを取得
        Switch sw = findViewById(R.id.sw);
        //　TextViewオブジェクトを取得
        TextView txt1_1s = findViewById(R.id.txt1_1s);
        TextView txt1_2s = findViewById(R.id.txt1_2s);
        TextView txt1_3s = findViewById(R.id.txt1_3s);
        TextView txt2_2s = findViewById(R.id.txt2_2s);

        //　各seekBarの値が変更された時の処理
        //　桁数のseekBarの処理
        seekBar_keta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                keta = i;                   //　バーの値を桁数の値に設定
                txt1_1s.setText(i+"桁");    //　バーの値をバーの真横に再表示
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //　口数のseekBarの処理
        seekBar_kuti.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                kuti = i;                   //　バーの値を口数の値に設定
                txt1_2s.setText(i+"口");    //　バーの値をバーの真横に再表示
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //　速さのseekBarの処理
        seekBar_hayasa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hayasa = i;                 //　バーの値を速さの値に設定
                txt1_3s.setText(i+"秒");    //　バーの値をバーの真横に再表示
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //　音量のseekBarの処理
        seekBar_onryou.setEnabled(false);   //　起動時は消音がONのため無効にする
        seekBar_onryou.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                onryou = i*10;                  //　バーの値*10を音量の値に設定
                txt2_2s.setText(""+onryou);     //　バーの値*10をバーの真横に再表示
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //　消音のON、OFFが切り替わった時の処理
        sw.setOnCheckedChangeListener((buttonView,isChecked) ->{
            otonasi = isChecked;                    //　isCheckedがtrueの時は消音ON、falseの時は消音OFF
            seekBar_onryou.setEnabled(!(otonasi));  //　isCheckedがtrueの時は音量のバーが無効、falseの時は有効
                }
        );

        //　「初期値に戻す」ボタンを押した時の処理
        btn_re.setOnClickListener(view -> {
            //　桁数、口数、速さの値を初期値に再設定
            keta = 1;
            kuti = 10;
            hayasa = 10;
            //　再設定した値をバーに反映
            seekBar_keta.setProgress(keta);
            seekBar_kuti.setProgress(kuti);
            seekBar_hayasa.setProgress(hayasa);
            //　再設定した値をバーの真横の値に再表示
            txt1_1s.setText(keta+"桁");
            txt1_2s.setText(kuti+"口");
            txt1_3s.setText(hayasa+"秒");
        });

        //　「スタート」ボタンを押した時の処理
        btn_str.setOnClickListener(view -> {
            //　インテントオブジェクトを生成、桁数・口数・速さ・消音・音量の値をサブ画面に渡す
            Intent i = new Intent(this,SubActivity.class);
            i.putExtra("keta",keta);
            i.putExtra("kuti",kuti);
            i.putExtra("hayasa",hayasa);
            i.putExtra("otonasi",otonasi);
            i.putExtra("onryou",onryou);
            //　サブ画面を起動
            startActivity(i);
        });
    }

    //　オプションメニューを生成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    //　メニュー選択時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //　ダイアログを生成
        DialogFragment dialog = new MyDialogFragment();
        //　フラグメントに選択された項目を渡す
        Bundle args = new Bundle();
        args.putInt("helpselected",item.getItemId());
        dialog.setArguments(args);
        //　ダイアログを表示
        dialog.show(getSupportFragmentManager(),"dialog_basic");
        return true;
    }
}