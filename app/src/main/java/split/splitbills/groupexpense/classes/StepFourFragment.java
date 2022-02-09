package split.splitbills.groupexpense.classes;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import split.splitbills.groupexpense.R;

import static android.content.Context.MODE_PRIVATE;

public class StepFourFragment extends Fragment {
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_four, container, false);
        btn = view.findViewById(R.id.LETSSTART);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("shared prefrence", MODE_PRIVATE).edit();
                editor.putInt("idName", 100);
                editor.apply();
                startActivity(new Intent(getActivity(), currencyselector.class));
            }
        });
        return view;
    }

}
