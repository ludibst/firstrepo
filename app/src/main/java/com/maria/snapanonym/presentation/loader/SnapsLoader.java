package com.maria.snapanonym.presentation.loader;


import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.maria.snapanonym.infrastructure.AppUtils;
import com.maria.snapanonym.infrastructure.NetworkUtils;
import com.maria.snapanonym.model.SimpleLocation;
import com.maria.snapanonym.model.Snap;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.loader.content.AsyncTaskLoader;

public class SnapsLoader extends AsyncTaskLoader<List<Snap>> {

    private Location mCurrentLocation;
    private Double mScope;

    public SnapsLoader(@NonNull Context context, Location currentLocation, double scope) {
        super(context);
        this.mCurrentLocation = currentLocation;
        this.mScope=scope;
    }

    @Override
    protected void onStartLoading() {
        onForceLoad();
        super.onStartLoading();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public List<Snap> loadInBackground() {
       // return NetworkUtils.getSnaps(mCurrentLocation, mScope);

        //TODO request snaps from network
        List<Snap> snaps = NetworkUtils.getSnaps(mCurrentLocation, mScope);

        for (Snap snap : snaps) {

            SimpleLocation currentSimpleLocation = new SimpleLocation(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());

            SimpleLocation snapPostedAtSimpleLocation = snap.getPostedAt();

            double distanceBetweenAsDouble = AppUtils.distanceBetweenAsMeters(currentSimpleLocation, snapPostedAtSimpleLocation, 0,0);

            snap.setDistance((int) distanceBetweenAsDouble);
        }

        return snaps;

    }
}

