package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by Tsui Ka Wai on 2015/11/22.
 */
public class ReplyAdapter extends FirebaseListAdapter<Reply>  {
    private String questionID;

    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public ReplyAdapter(Firebase mRef, int mLayout, Activity activity, String questionID) {
        super(mRef, Reply.class, mLayout, activity);
        this.questionID = questionID;
    }

    @Override
    protected void populateView(View view, Reply reply) {
        if(!reply.getQuestionID().equals(questionID)){
            view.findViewById(R.id.replyLinearLayout).setVisibility(View.GONE);
        }
         else {
            TextView text = (TextView) view.findViewById(R.id.replyDesc);
            text.setText(reply.getDesc());
        }
    }

    @Override
    protected void sortModels(List<Reply> mModels) {
        Collections.sort(mModels, new Comparator<Reply>() {
            @Override
            public int compare(Reply r1, Reply r2) {
                return new Long(r2.getTimestamp()).compareTo(r1.getTimestamp());
            }
        });
    }

    @Override
    protected void setKey(String key, Reply model) {
        model.setKey(key);
    }
}


