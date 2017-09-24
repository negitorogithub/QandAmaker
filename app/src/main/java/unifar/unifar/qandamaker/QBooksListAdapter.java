package unifar.unifar.qandamaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class QBooksListAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private int layoutResource;
    private List<String> questionNameList;

    public QBooksListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        questionNameList = objects;
    }
    private static class ViewHolder {
        LinearLayout linearLayout;
        ImageView history;
        TextView question;
        int correct;
        String fileNameToOpen;
    }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            ViewHolder viewHolder;
            List questionsBuffer[] = new  ArrayList[3];
            int trueAmount = 0;
            float truePercent;
            if (v == null) {
                v = inflater.inflate(layoutResource, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.linearLayout = (LinearLayout)v.findViewById(R.id.questionBookListItemsParent);
                viewHolder.question = (TextView) v.findViewById(R.id.textview_questionBooksListItem);
                viewHolder.history = (ImageView) v.findViewById(R.id.questionBooksResult);

                    for (int i = 0;i<3 ;i++) {
                        //makeQuestionsArrayFromFilesを作る
                        questionsBuffer[i] = MainActivity.makeListFromQuetionArray(MainActivity.makeQuestionsArrayFromFile(questionNameList.get(position)), MainActivity.INT_QfileAnswerHistoryIndex1+i);
                        for (int j = 0;j<questionsBuffer[i].size(); j++ ){
                            if (questionsBuffer[i].get(j).equals(true)){
                                trueAmount++;
                            }
                        }
                    }
                truePercent= (trueAmount / (float)(questionsBuffer[0].size() * 3));
                if (truePercent ==0){
                    viewHolder.correct = 0;
                } else if (0<truePercent&truePercent <=0.20){
                    viewHolder.correct =1;
                } else if (0.20<truePercent&truePercent <=0.40){
                    viewHolder.correct =2;
                } else if (0.40<truePercent&truePercent <=0.60){
                    viewHolder.correct =3;
                } else if (0.60<truePercent&truePercent <=0.80) {
                    viewHolder.correct = 4;
                } else if (0.80<truePercent) {
                    viewHolder.correct = 5;
                }
                    switch (viewHolder.correct) {
                    case 0:
                        viewHolder.fileNameToOpen = "images/Greycircle.png";
                        break;
                    case 1:
                        viewHolder.fileNameToOpen = "images/Redcircle.png";
                        break;
                    case 2:
                        viewHolder.fileNameToOpen = "images/Orangecircle.png";
                        break;
                    case 3:
                        viewHolder.fileNameToOpen = "images/Yellowcircle.png";
                        break;
                    case 4:
                        viewHolder.fileNameToOpen = "images/Greencircle.png";
                        break;
                    case 5:
                        viewHolder.fileNameToOpen = "images/Bluecircle.png";
                        break;

                    }
                v.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) v.getTag();
            }
            viewHolder.question.setText(questionNameList.get(position));
            try {
                InputStream inputStream = MyApplication.getAppContext().getResources().getAssets().open(viewHolder.fileNameToOpen);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                viewHolder.history.setImageBitmap(bitmap);
                inputStream.close();
            } catch (IOException e) {
                Log.d("onqbook", "Error on set questionResult");
            }
            return v;

        }
    }