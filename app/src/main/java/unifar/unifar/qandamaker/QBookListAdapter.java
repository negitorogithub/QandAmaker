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
import java.util.List;

class QBookListAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private int layoutResource;
    private List<String> qlistToShoow;
    QBookListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        qlistToShoow = objects;
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
        ViewHolder viewHolder ;

            if (v == null ) {
                v = inflater.inflate(layoutResource, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.linearLayout = (LinearLayout)v.findViewById(R.id.questionListItemsParent);
                viewHolder.linearLayout.setLayoutParams(MainActivity.layoutParams);
                viewHolder.question = (TextView) v.findViewById(R.id.textview_questionListItem);
                viewHolder.history = (ImageView) v.findViewById(R.id.questionResult);
                v.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)v.getTag();
            }
        viewHolder.question.setText(qlistToShoow.get(position));
        viewHolder.correct = 0;
        for (int i = 0; i < 3; i++) {
                    if (MainActivity.getHistoryData().size() > 0) {
                        if ((MainActivity.getHistoryData().get(position))[i]) {
                            viewHolder.correct++;
                        }
                    }
                }
                switch (viewHolder.correct) {
                    case 0:
                        viewHolder.fileNameToOpen = "images/QuestionResult0.png";
                        break;
                    case 1:
                        viewHolder.fileNameToOpen = "images/QuestionResult1.png";
                        break;
                    case 2:
                        viewHolder.fileNameToOpen = "images/QuestionResult2.png";
                        break;
                    case 3:
                        viewHolder.fileNameToOpen = "images/QuestionResult3.png";
                        break;
                }
        try {
            InputStream inputStream = MyApplication.getAppContext().getResources().getAssets().open(viewHolder.fileNameToOpen);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            viewHolder.history.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException e) {
            Log.d("onqbook", "Error on set questionResult");
        }
        return v;

    }}
