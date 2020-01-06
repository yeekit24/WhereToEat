package ploywide.com.wheretoeat.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ploywide.com.wheretoeat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }


    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        TextView resultTextView = view.findViewById(R.id.resultText);
        Button inviteButton = view.findViewById(R.id.inviteButton);

        final String resultString = getArguments().getString("result_text");
        resultTextView.setText(resultString);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //share to other app
                shareToOtherApp(resultString);
                dismiss();
            }
        });
        return view;
    }

    private void shareToOtherApp(String resultString) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //String shareSubText = "Today I will eat at";
        String shareBodyText = "Today I will eat at "+ resultString+".Join me?"
                        +System.getProperty("line.separator") + System.getProperty("line.separator")
                        + " Download WhereToEat Android app today!" + System.getProperty("line.separator")
                        + "https://play.google.com/store/apps/details?id=ploywide.com.wheretoeat";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

}
