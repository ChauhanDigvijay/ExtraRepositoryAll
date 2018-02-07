package com.clp.sdk;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceReceiver extends BroadcastReceiver {
	Context context;

	Intent broadcastIntent = new Intent();
	private static String CLP_GEOFENCE_CALLBACK = "CLP_GEOFENCE_CALLBACK";

	@Override
	public void onReceive(Context context, Intent intent) {

		CLPSdk _clpSDK = CLPSdk.sharedInstance(context);
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if (geofencingEvent.hasError()) {
			String errorMessage = GeofenceErrorMessages.getErrorString(context,
					geofencingEvent.getErrorCode());
			CLPSdk.sharedInstance(context).localLog("error", errorMessage);
			return;
		}

		// Get the transition type.
		int geofenceTransition = geofencingEvent.getGeofenceTransition();

		// Test that the reported transition was of interest.
		if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
				|| geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

			// Get the geofences that were triggered. A single event can trigger
			// multiple geofences.
			List<Geofence> triggeringGeofences = geofencingEvent
					.getTriggeringGeofences();

			// Get the transition details as a String.
			String geofenceTransitionDetails = getGeofenceTransitionDetails(
					context, geofenceTransition, triggeringGeofences);
			Intent callback_intent = new Intent();
			if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
				callback_intent.putExtra("EVENT", "ENTER");
			} else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
				callback_intent.putExtra("EVENT", "EXIT");
			}
			ArrayList<String> triggeringGeofencesIdsList = new ArrayList<String>();
			for (Geofence geofence : triggeringGeofences) {
				triggeringGeofencesIdsList.add(geofence.getRequestId());
			}
			if (_clpSDK.isMyServiceRunning(LocationService.class, context)) {
				callback_intent.putStringArrayListExtra("REGIONS",
						triggeringGeofencesIdsList);
				callback_intent.setAction(CLP_GEOFENCE_CALLBACK);
				context.sendBroadcast(callback_intent);
			}

			// Send notification and log the transition details.
			// sendNotification(geofenceTransitionDetails);
			_clpSDK.displayLocalPushNotification(geofenceTransitionDetails,
					context.getClass(), R.drawable.ic_launcher, context);
			CLPSdk.sharedInstance(context).localLog("Message", geofenceTransitionDetails);
		} else {
			// Log the error.
			CLPSdk.sharedInstance(context).localLog("Error", "Invalid geofence type");
		}
	}

	/**
	 * Gets transition details and returns them as a formatted string.
	 * 
	 * @param context
	 *            The app context.
	 * @param geofenceTransition
	 *            The ID of the geofence transition.
	 * @param triggeringGeofences
	 *            The geofence(s) triggered.
	 * @return The transition details formatted as String.
	 */
	private String getGeofenceTransitionDetails(Context context,
			int geofenceTransition, List<Geofence> triggeringGeofences) {

		String geofenceTransitionString = getTransitionString(geofenceTransition);

		// Get the Ids of each geofence that was triggered.
		ArrayList<String> triggeringGeofencesIdsList = new ArrayList<String>();
		for (Geofence geofence : triggeringGeofences) {
			triggeringGeofencesIdsList.add(geofence.getRequestId());
		}
		String triggeringGeofencesIdsString = TextUtils.join(", ",
				triggeringGeofencesIdsList);

		return geofenceTransitionString + ": store "
				+ triggeringGeofencesIdsString;
	}

	/**
	 * Maps geofence transition types to their human-readable equivalents.
	 * 
	 * @param transitionType
	 *            A transition type constant defined in Geofence
	 * @return A String indicating the type of transition
	 */
	private String getTransitionString(int transitionType) {
		switch (transitionType) {
		case Geofence.GEOFENCE_TRANSITION_ENTER:
			return "Geofence Entered";
		case Geofence.GEOFENCE_TRANSITION_EXIT:
			return "Geofence Exit";
		default:
			return "Geofence status not known";
		}
	}
}
