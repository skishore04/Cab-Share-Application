package com.example.mycabshare.Calback;

import com.example.mycabshare.Model.DriverGeoModel;

public interface IFirebaseDriverInfoListener {

    void onDriverInfoLoadSuccess(DriverGeoModel driverGeoModel);
}
