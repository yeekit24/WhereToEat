package ploywide.com.wheretoeat.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import ploywide.com.wheretoeat.API.APIHelper;
import ploywide.com.wheretoeat.API.Callback;
import ploywide.com.wheretoeat.Helper.ForceUpdateChecker;
import ploywide.com.wheretoeat.R;
import ploywide.com.wheretoeat.app.AppController;
import ploywide.com.wheretoeat.fragment.ResultFragment;
import ploywide.com.wheretoeat.model.ChoicesItem;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class MainActivity extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener{

    List<LuckyItem> data = new ArrayList<>();
    List<ChoicesItem> choicesItems = new ArrayList<>();

    LuckyWheelView luckyWheelView;
    Toolbar toolbar;

    Button playButton;

    private static final int ADD_FOOD_ACTION = 111;

    private void getChoices() {
        APIHelper.get_choices(getApplicationContext(), AppController.user_id, new Callback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray choices = response.getJSONArray("choices");

                    if (choices.length() > 0) {
                        for(int i= 0;i < choices.length();i++){
                            JSONArray choice = choices.getJSONArray(i);

                            choicesItems.add(new ChoicesItem(choice.getString(1), 0, choice.getLong(0)));
                        }
                    }
                    else {
                        prepareDefaultLuckyWheelData();
                    }
                    reInitializeLuckyWheel(choicesItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playButton = findViewById(R.id.play);

        luckyWheelView = findViewById(R.id.luckyWheel);
        getChoices();

    }

    @Override
    public void onUpdateNeeded(String updateUrl) {
        showUpdateDialog(updateUrl);
    }

    private void showUpdateDialog(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue enjoy WhereToEat.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void prepareDefaultLuckyWheelData() {

        ChoicesItem choice = new ChoicesItem("Jolibee",R.drawable.joli, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Mc Donalds",R.drawable.mcd, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Pizza Hut",R.drawable.pizza, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Carl Jr",R.drawable.carl, -1);
        choicesItems.add(choice);

        choice = new ChoicesItem("Starbucks",R.drawable.starbucks, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Wendy's",R.drawable.wendys, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Texas Chicken",R.drawable.texas, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Mos Burgers",R.drawable.mos, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Sushi King",R.drawable.sushi, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("KFC",R.drawable.kfc, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("Domino",R.drawable.domino, -1);
        choicesItems.add(choice);

        choice= new ChoicesItem("4 Fingers",R.drawable.fingers, -1);
        choicesItems.add(choice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_FOOD_ACTION){

            Bundle bundle;
            if (data != null) {
                bundle = data.getExtras();
                ArrayList<ChoicesItem> intentData = null;
                if (bundle != null) {
                    intentData = bundle.getParcelableArrayList("choiceArray");
                }
                if (intentData != null) {
                    Log.d("lol 123",intentData.toString());
                    updateWheelItem(intentData);

                }
            }
        }
    }

    private void updateWheelItem(ArrayList<ChoicesItem> intentData) {
        choicesItems.clear();
        data.clear();
        this.choicesItems.addAll(intentData);
        reInitializeLuckyWheel(this.choicesItems);
    }

    private void reInitializeLuckyWheel(List<ChoicesItem> choicesItems){
        int[] colorArray = {getResources().getColor(R.color.rose),getResources().getColor(R.color.rose2),getResources().getColor(R.color.rose3),
                getResources().getColor(R.color.rose4),getResources().getColor(R.color.rose5),getResources().getColor(R.color.rose6),getResources().getColor(R.color.rose7)};


        for(int i = 0; i< choicesItems.size(); i++){
            LuckyItem luckyItem = new LuckyItem();
            luckyItem.topText = this.choicesItems.get(i).getChoices();

            if(this.choicesItems.get(i).getDrawable() != 0){
                luckyItem.icon = this.choicesItems.get(i).getDrawable();
            }


            luckyItem.color = colorArray[getRandomColor(colorArray)];
            data.add(luckyItem);

            luckyWheelView.setData(data);

            setDataToLuckyWheel();
        }
    }

    private void setDataToLuckyWheel() {
        luckyWheelView.setData(data);
        luckyWheelView.setRound(getRandomRound());

        luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        //luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = getRandomIndex();
                luckyWheelView.startLuckyWheelWithTargetIndex(index);
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                for (int i = 0; i < choicesItems.size(); i++ ){
                    if (data.get(index).topText == choicesItems.get(i).getChoices()) {
                        APIHelper.spin(getApplicationContext(), AppController.user_id, choicesItems.get(i).server_choice_id);

                    }
                }
                showFragmentForResult(data.get(index).topText);
            }
        });
    }

    FragmentManager fm = getSupportFragmentManager();
    private void showFragmentForResult(String topText) {
        ResultFragment rf = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("result_text", topText);
        rf.setArguments(bundle);
        rf.show(fm,"Title");
    }

    private int getRandomColor(int[] colorArray){
        Random rand = new Random();
        return rand.nextInt(colorArray.length - 1);
    }
    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1);
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(5);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.more:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("choiceArray", (ArrayList<? extends Parcelable>) choicesItems);

                Intent intent = new Intent(MainActivity.this,AddFoodChoiceActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,ADD_FOOD_ACTION);
                break;
            case R.id.share:
                shareLinkToOtherApp();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void shareLinkToOtherApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //String shareSubText = "Today I will eat at";
        String shareBodyText = "Hey there,I using WhereToEat Android app now."+ " " +"Join me?"
                +System.getProperty("line.separator") + System.getProperty("line.separator")
                + "Download WhereToEat today!" + System.getProperty("line.separator")
                + "https://play.google.com/store/apps/details?id=ploywide.com.wheretoeat"+System.getProperty("line.separator")+"Powered by PloyWide";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }


}
