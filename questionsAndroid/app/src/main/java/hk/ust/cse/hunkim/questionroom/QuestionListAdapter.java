package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.text.util.Linkify;

import java.util.ArrayList;
import java.util.regex.Pattern;

import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * @author greg
 * @since 6/21/13
 * <p/>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class QuestionListAdapter extends FirebaseListAdapter<Question> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String roomName;
    MainActivity activity;
    private String inputEmail;
    private boolean selectPostByEmail;
    private static ArrayList<String> messagesWithTag;
    private ReplyAdapter mReplyAdapter;
    private Firebase mFirebaseRefReply;

    public QuestionListAdapter(Query ref, Activity activity, int layout, String room_Name, Firebase replies) {
        super(ref, Question.class, layout, activity);
        roomName = room_Name;
        // Must be MainActivity
        assert (activity instanceof MainActivity);

        this.activity = (MainActivity) activity;
        this.mFirebaseRefReply = replies;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view     A view instance corresponding to the layout we passed to the constructor.
     * @param question An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, final Question question) {
       // DBUtil dbUtil = activity.getDbutil();

        // In "find posts by email" mode,
        // posts with different email attributes will be hidden
        if(selectPostByEmail) {
            if( ! question.getEmail().equals(inputEmail)) {
                view.findViewById(R.id.questionLinearLayout).setVisibility(View.GONE);
                return;
            }
        }

        // Map a Chat object to an entry in our listview
        int echo = question.getEcho();

        if(echo < -15) {
            view.findViewById(R.id.questionLinearLayout).setVisibility(View.GONE);
            return;
        }

        TextView echoText = (TextView) view.findViewById(R.id.echo_count);
        echoText.setText("" + echo);

        ImageButton echoButton = (ImageButton) view.findViewById(R.id.echo);

        echoButton.setTag(question.getKey()); // Set tag for button

        echoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        m.popUpLikeDialog(question.getKey());
                    }
                }

        );

        ImageButton shareButton = (ImageButton) view.findViewById(R.id.shareButton);

        shareButton.setTag(question.getKey());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity m = (MainActivity) view.getContext();
                m.shareQuestion((String) view.getTag());
            }
        });

        ImageButton replyButton = (ImageButton) view.findViewById(R.id.replybutton);

        replyButton.setTag(question.getKey());

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity m = (MainActivity) view.getContext();
                m.popUpReplyDialog(question.getKey());
            }
        });

        String msgString = "";

        question.updateNewQuestion();
        if (question.isNewQuestion()) {
            msgString += "<font color=red>NEW </font>";
        }
        //msgString += "<B>" + question.getHead() + "</B>" + question.getDesc();
        // escapeHTML for XSS protection
        msgString += "<B>" + Html.escapeHtml(question.getHead()) + "</B>" + Html.escapeHtml(question.getDesc());
        //Pattern to find if there's a hash tag in the message
        //i.e. any word starting with a # and containing letter or numbers or _
        Pattern tagMatcher = Pattern.compile("[#]+[A-Za-z0-9-_]+\\b");

        //Scheme for Linkify, when a word matched tagMatcher pattern,
        //that word is appended to this URL and used as content URI
        String newActivityURL = "content://hk.ust.cse.hunkim.questionroom.tagdetailsactivity/";

        //Attach Linkify to TextView
        Linkify.addLinks(((TextView) view.findViewById(R.id.head_desc)), tagMatcher, newActivityURL);


        //download image
        ImageView im = (ImageView) view.findViewById(R.id.image);
        if(question.getImage()!=null && !question.getImage().equals(""))
        {
            byte[] data = Base64.decode(question.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            im.getLayoutParams().height = 600;
            im.setImageBitmap(bitmap);
        }
        else
        {
            im.getLayoutParams().height = 0;
            im.setImageBitmap(null);
        }

        ((TextView) view.findViewById(R.id.head_desc)).setText(Html.fromHtml(msgString));
        view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        MainActivity m = (MainActivity) view.getContext();
                                        m.popUpLikeDialog(question.getKey());
                                    }
                                }

        );

        String timeAgo = "";
        long ts = question.getTimestamp();
        long currentTime = new Date().getTime();
        long interval = (currentTime-ts)/1000;

        if(interval < 60){
            timeAgo += "just";
        }
        else if(interval > 59 && interval < 3600) {
            long x = interval/60;
            long min = x - x % 1;
            if(min==1){
                timeAgo += min + " min" + " ago";
            }
            else
                timeAgo += min+" mins"+" ago";
        }
        else if(interval>3599 && interval<86400){
            long x = interval/3600;
            long hr = x - x % 1;
            if(hr==1){timeAgo += hr+ " hour" + " ago";}
            else
                timeAgo += hr + " hours" + " ago";
        }
        else if(interval>86399 && interval<604800){
            long x= interval/86400;
            long day = x - x % 1;
            if(day==1){timeAgo += day+" day"+" ago";}
            else
                timeAgo += day+" days" + " ago";
        }
        else{
            timeAgo += new Date(ts).toString();
        }

        ((TextView) view.findViewById(R.id.timestamp)).setText(timeAgo);

        final ListView replyListView = (ListView) view.findViewById(R.id.replyListView);
        mReplyAdapter = new ReplyAdapter(
                mFirebaseRefReply,
                R.layout.reply, activity, question.getKey());
        mReplyAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
        replyListView.setAdapter(mReplyAdapter);
        // check if we already clicked
        /*boolean clickable = !dbUtil.contains(question.getKey());

        echoButton.setClickable(clickable);
        echoButton.setEnabled(clickable);
        view.setClickable(clickable);


        // http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
        // grey out our button
        if (clickable) {
            echoButton.getBackground().setColorFilter(null);
        } else {
            echoButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }*/


        view.setTag(question.getKey());  // store key in the view
    }

    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels, Collections.reverseOrder());
    }

    @Override
    protected void setKey(String key, Question model) {
        model.setKey(key);
    }

    protected void setSelectPostByEmail(boolean value) { selectPostByEmail = value; }

    protected void setInputEmail(String email) { inputEmail = new String(email); }

    public Question getItem(int i) {
        return mModels.get(i);
    }

    public ArrayList<String> GetAllTaggedQuestions(String tag){
        for (int i = 0; i < getCount(); i++){
            if (getItem(i).getWholeMsg().contains(tag))
                messagesWithTag.add(getItem(i).getWholeMsg());
        }
        return messagesWithTag;
    }
}

