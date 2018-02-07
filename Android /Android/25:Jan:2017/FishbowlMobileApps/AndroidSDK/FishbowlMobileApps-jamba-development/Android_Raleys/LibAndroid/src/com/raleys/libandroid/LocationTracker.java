package com.raleys.libandroid;
 
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
 
public class LocationTracker extends Service implements LocationListener
{
    private final Context _context;
    protected LocationManager _locationManager;
    boolean _isGPSEnabled = false;
    boolean _isNetworkEnabled = false;
    boolean _canGetLocation = false; 
    Location _location;
    double _latitude;
    double _longitude;
 
    private static final long METERS_PER_MILE = 1609;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = METERS_PER_MILE * 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
 
    public LocationTracker(Context context)
    {
        this._context = context;
        getLocation();
    }

    
    public Location getLocation()
    {
        try
        {
            _locationManager = (LocationManager)_context.getSystemService(LOCATION_SERVICE);
            _isGPSEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            _isNetworkEnabled = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if(!_isGPSEnabled && !_isNetworkEnabled)
            {
                Log.e(getClass().getSimpleName(), "GPS is not enabled");
            }
            else
            {
                this._canGetLocation = true;
                
                // first get location from Network Provider
                if(_isNetworkEnabled)
                {
                    _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if(_locationManager != null)
                    {
                        _location = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        
                        if(_location != null)
                        {
                            _latitude = _location.getLatitude();
                            _longitude = _location.getLongitude();
                            Log.i(getClass().getSimpleName(), "Obtained location from network provider, latitude = " + _latitude + ", longitude = " + _longitude);
                        }
                    }
                }
                
                // if GPS is enabled get location using GPS Services
                if(_isGPSEnabled)
                {
                    if(_location == null)
                    {
                        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.i(getClass().getSimpleName(), "GPS Enabled");
                        
                        if(_locationManager != null)
                        {
                            _location = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            
                            if(_location != null)
                            {
                                _latitude = _location.getLatitude();
                                _longitude = _location.getLongitude();
                                Log.i(getClass().getSimpleName(), "Obtained location from GPS, latitude = " + _latitude + ", longitude = " + _longitude);
                           }
                        }
                    }
                }
            }
        }
        catch (Exception e) { e.printStackTrace(); }
 
        return _location;
    }
     

    public void stopUsingGPS()
    {
        if(_locationManager != null)
            _locationManager.removeUpdates(LocationTracker.this);
    }

    
    public double getLatitude()
    {
        if(_location != null)
            _latitude = _location.getLatitude();
         
        return _latitude;
    }

    
    public double getLongitude()
    {
        if(_location != null)
            _longitude = _location.getLongitude();

        return _longitude;
    }

    
    public boolean canGetLocation()
    {
        return this._canGetLocation;
    }

    
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                _context.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            	dialog.cancel();
            }
        });
  
        alertDialog.show();
    }

    
    @Override
    public void onLocationChanged(Location location)
    {
    	_location = location;
        Log.i(getClass().getSimpleName(), "Location changed, latitude = " + _location.getLatitude() + ", _longitude = " + location.getLongitude());
    }

    
    @Override
    public void onProviderDisabled(String provider)
    {
    }
 
    
    @Override
    public void onProviderEnabled(String provider)
    {
    }
 
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
 
}