package com.example.android.sunshine.app.data;

import android.os.Bundle;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Sheraz on 8/25/2015.
 */
public class TestWearable extends AndroidTestCase  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = TestWearable.class.getSimpleName();

    private final String PATH_WITH_FEATURE = "/watch_face/WeatherInfo";

    private final String KEY_WEATHER_ID = "weather_id";
    private final String KEY_TEMPERATURE_HIGH = "temperature_high";
    private final String KEY_TEMPERATURE_LOW = "temperature_low";


    private GoogleApiClient mGoogleApiClient;
    private PutDataMapRequest mPutDataMapRequest;

    public void testBuildWeatherInformationSendingToWearable () {
        mPutDataMapRequest = PutDataMapRequest.create(PATH_WITH_FEATURE);
        DataMap dataMap = mPutDataMapRequest.getDataMap();

        dataMap.putInt(KEY_WEATHER_ID, 511);
        dataMap.putDouble(KEY_TEMPERATURE_HIGH, 4);
        dataMap.putDouble(KEY_TEMPERATURE_LOW, 2);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override // GoogleApiClient.ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onConnected: " + connectionHint);
        }

        Wearable.DataApi.putDataItem(mGoogleApiClient, mPutDataMapRequest.asPutDataRequest())
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        Log.d(TAG, "SaveConfig: " + dataItemResult.getStatus() + ", " + dataItemResult.getDataItem().getUri());
                    }
                });
    }

    @Override // GoogleApiClient.ConnectionCallbacks
    public void onConnectionSuspended(int cause) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onConnectionSuspended: " + cause);
        }
    }

    @Override // GoogleApiClient.OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult result) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onConnectionFailed: " + result);
        }
    }

}
