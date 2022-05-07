package to.msn.wings.anzan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //　選択されるメニューのID配列を定義　0は桁数、1は口数、2は速さについて
        int[] itemId = {R.id.ketasu,R.id.kutisu,R.id.hayasa};
        //　タイトルとなるstring値の配列を定義　0は桁数、1は口数、2は速さのタイトル
        int[] title = {R.string.ketasu, R.string.kutisu, R.string.hayasa};
        //　解説文のstring値の配列を定義　0は桁数、1は口数、2は速さの解説
        int[] kaisetu = {R.string.kaisetu_ketasu, R.string.kaisetu_kutisu, R.string.kaisetu_hayasa};

        //　選択されたメニューの番号を取得する
        Bundle args = requireArguments();
        int num = 0;
        for (int i = 0; i < itemId.length; i++) {
            if (args.getInt("helpselected") == itemId[i])
                num = i;
        }

        //　取得した番号に応じたダイアログの設定と表示
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        return builder.setTitle(getText(title[num]))
                .setMessage(getText(kaisetu[num]))
                .create();
    }
}
