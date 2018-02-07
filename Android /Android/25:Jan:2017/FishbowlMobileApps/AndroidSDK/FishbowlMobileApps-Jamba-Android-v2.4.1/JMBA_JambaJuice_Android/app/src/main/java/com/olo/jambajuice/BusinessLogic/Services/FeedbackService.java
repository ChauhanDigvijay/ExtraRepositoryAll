package com.olo.jambajuice.BusinessLogic.Services;

import com.olo.jambajuice.BusinessLogic.Interfaces.FeedbackServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.Feedback;
import com.parse.ParseException;
import com.parse.SaveCallback;

/**
 * Created by Nauman Afzaal on 20/08/15.
 */
public class FeedbackService {
    public static void sendFeedback(Feedback feedback, final FeedbackServiceCallback callback) {
        feedback.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.onSendFeedbackCallback(e);
            }
        });
    }
}
