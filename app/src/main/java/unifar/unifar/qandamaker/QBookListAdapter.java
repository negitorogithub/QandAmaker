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
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class QBookListAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    int layoutResource;
    List<String> qlistToShoow;
    public QBookListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        qlistToShoow = objects;
    }


    private static class ViewHolder {
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
        if(v == null) {
            v = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.question = (TextView)v.findViewById(R.id.textview_questionListItem) ;
            viewHolder.question.setText(qlistToShoow.get(position));
            viewHolder.history = (ImageView)v.findViewById(R.id.questionResult);

            for (int i = 0; i<3 ; i++){
                    if ((MainActivity.historyData.get(position))[i]) {
                        viewHolder.correct++;
                    }
            }
            switch (viewHolder.correct){
                case 0:
                    viewHolder.fileNameToOpen ="QuestionResult0.png";
                case 1:
                    viewHolder.fileNameToOpen ="QuestionResult1.png";
                case 2:
                    viewHolder.fileNameToOpen ="QuestionResult2.png";
                case 3:
                    viewHolder.fileNameToOpen ="QuestionResult3.png";
            }

            try {
                InputStream istream = MyApplication.getAppContext().getResources().getAssets().open(viewHolder.fileNameToOpen);
                Bitmap bitmap = BitmapFactory.decodeStream(istream);
                viewHolder.history.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.d("onqbook","Error on set questionResult");
            }

        }

        return v;
    }}
