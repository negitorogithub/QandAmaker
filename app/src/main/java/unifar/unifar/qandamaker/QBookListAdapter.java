package unifar.unifar.qandamaker;

import android.app.FragmentManager;
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
    private List<Question> qlistToShoow;
    private FragmentManager fragmentManager;
    QBookListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Question> objects, FragmentManager fm ) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        qlistToShoow = objects;
        fragmentManager = fm;
    }


    private static class ViewHolder {
        LinearLayout linearLayout;
        ImageView history;
        TextView question;
        int correct;
        String fileNameToOpen;
        ImageView editOnListView;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder ;

            if (v == null ) {
                v = inflater.inflate(layoutResource, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.question = (TextView) v.findViewById(R.id.textview_questionListItem);
                viewHolder.history = (ImageView) v.findViewById(R.id.questionResult);
                viewHolder.editOnListView = (ImageView)v.findViewById(R.id.editOnQuestionList);
                v.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)v.getTag();
            }
        viewHolder.question.setText(qlistToShoow.get(position).getQuestionName());
        viewHolder.editOnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.viewFlag==2){

                    CustomizedDialog_questionbook customizedDialog_questionbook = CustomizedDialog_questionbook.newInstance(
                            qlistToShoow.get(position).getQuestionName(),
                            qlistToShoow.get(position).getAnswerName(),
                            qlistToShoow.get(position).getTagName()
                            );
                    MainActivity.int_onListViewPositionOn2 = position;
                    MyApplication.bundle.putBoolean("isEditMode",true);
                    fragmentManager.beginTransaction().commit();
                    customizedDialog_questionbook.show(fragmentManager, "firstInputDialog");

                }
            }
        });
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
        try {
            InputStream inputStream = MyApplication.getAppContext().getResources().getAssets().open("images/ic_mode_edit_black_48dp.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            viewHolder.editOnListView.setImageBitmap(bitmap);
            inputStream.close();
        } catch (IOException e) {
            Log.d("onqbook", "Error on set questionResult");
        }
        return v;

    }}
