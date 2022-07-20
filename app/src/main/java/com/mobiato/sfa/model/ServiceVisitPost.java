package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Himm on 3/13/2018.
 */

public class ServiceVisitPost implements Parcelable {

    public String serviceType;
    public String ticketNo;
    public String timeIn;
    public String timeOut;
    public String latitude;
    public String longitude;
    public String ownerName;
    public String outletName;
    public String serialNo;
    public String modelNo;
    public String assetNo;
    public String brand;
    public String serialImage;
    public String landmark;
    public String location;
    public String townVillage;
    public String contactNumber;
    public String contactNumber2;
    public String contactPerson;
    public String natureOfCall;
    public String currentVolt;
    public String amps;
    public String temprature;
    public String workstatus;
    public String pendingReason;
    public String pendingSpare;
    public String workDoneType;
    public String workDoneComment;
    public String anyDispute;
    public String workSpare;
    public String workSpareUsed;
    public String disputeImage1;
    public String disputeImage2;
    public String techRating;
    public String qualityRate;
    public String customerSignature;
    public String workingId;
    public String conditionImage;
    public String cleanlessId;
    public String cleanlessImage;
    public String coilId;
    public String coilImage;
    public String gasketId;
    public String gasketImage;
    public String lightId;
    public String lightImage;
    public String brandingId;
    public String brandingImage;
    public String ventilationId;
    public String ventilationImage;
    public String levelingId;
    public String levelingImage;
    public String stockPer;
    public String stockImage;
    public String ctsStatus;
    public String coolerImage1;
    public String coolerImage2;
    public String district;
    public String ctcComment;
    public String otherSpecific;


    public ServiceVisitPost() {
    }

    protected ServiceVisitPost(Parcel in) {
        serviceType = in.readString();
        ticketNo = in.readString();
        timeIn = in.readString();
        timeOut = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        ownerName = in.readString();
        outletName = in.readString();
        serialNo = in.readString();
        modelNo = in.readString();
        assetNo = in.readString();
        brand = in.readString();
        serialImage = in.readString();
        landmark = in.readString();
        location = in.readString();
        townVillage = in.readString();
        contactNumber = in.readString();
        contactNumber2 = in.readString();
        contactPerson = in.readString();
        natureOfCall = in.readString();
        currentVolt = in.readString();
        amps = in.readString();
        temprature = in.readString();
        workstatus = in.readString();
        workSpare = in.readString();
        workSpareUsed = in.readString();
        pendingReason = in.readString();
        pendingSpare = in.readString();
        workDoneType = in.readString();
        workDoneComment = in.readString();
        anyDispute = in.readString();
        disputeImage1 = in.readString();
        disputeImage2 = in.readString();
        techRating = in.readString();
        qualityRate = in.readString();
        customerSignature = in.readString();
        workingId = in.readString();
        conditionImage = in.readString();
        cleanlessId = in.readString();
        cleanlessImage = in.readString();
        coilId = in.readString();
        coilImage = in.readString();
        gasketId = in.readString();
        gasketImage = in.readString();
        lightId = in.readString();
        lightImage = in.readString();
        brandingId = in.readString();
        brandingImage = in.readString();
        ventilationId = in.readString();
        ventilationImage = in.readString();
        levelingId = in.readString();
        levelingImage = in.readString();
        stockPer = in.readString();
        stockImage = in.readString();
        ctsStatus = in.readString();
        coolerImage1 = in.readString();
        coolerImage2 = in.readString();
        district = in.readString();
        ctcComment = in.readString();
        otherSpecific = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceType);
        dest.writeString(ticketNo);
        dest.writeString(timeIn);
        dest.writeString(timeOut);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(ownerName);
        dest.writeString(outletName);
        dest.writeString(serialNo);
        dest.writeString(modelNo);
        dest.writeString(assetNo);
        dest.writeString(brand);
        dest.writeString(workSpare);
        dest.writeString(workSpareUsed);
        dest.writeString(serialImage);
        dest.writeString(landmark);
        dest.writeString(location);
        dest.writeString(townVillage);
        dest.writeString(contactNumber);
        dest.writeString(contactNumber2);
        dest.writeString(contactPerson);
        dest.writeString(natureOfCall);
        dest.writeString(currentVolt);
        dest.writeString(amps);
        dest.writeString(temprature);
        dest.writeString(workstatus);
        dest.writeString(pendingReason);
        dest.writeString(pendingSpare);
        dest.writeString(workDoneType);
        dest.writeString(workDoneComment);
        dest.writeString(anyDispute);
        dest.writeString(disputeImage1);
        dest.writeString(disputeImage2);
        dest.writeString(techRating);
        dest.writeString(qualityRate);
        dest.writeString(customerSignature);
        dest.writeString(workingId);
        dest.writeString(conditionImage);
        dest.writeString(cleanlessId);
        dest.writeString(cleanlessImage);
        dest.writeString(coilId);
        dest.writeString(coilImage);
        dest.writeString(gasketId);
        dest.writeString(gasketImage);
        dest.writeString(lightId);
        dest.writeString(lightImage);
        dest.writeString(brandingId);
        dest.writeString(brandingImage);
        dest.writeString(ventilationId);
        dest.writeString(ventilationImage);
        dest.writeString(levelingId);
        dest.writeString(levelingImage);
        dest.writeString(stockPer);
        dest.writeString(stockImage);
        dest.writeString(ctsStatus);
        dest.writeString(coolerImage1);
        dest.writeString(coolerImage2);
        dest.writeString(district);
        dest.writeString(ctcComment);
        dest.writeString(otherSpecific);

    }

    @SuppressWarnings("unused")
    public static final Creator<ServiceVisitPost> CREATOR = new Creator<ServiceVisitPost>() {
        @Override
        public ServiceVisitPost createFromParcel(Parcel in) {
            return new ServiceVisitPost(in);
        }

        @Override
        public ServiceVisitPost[] newArray(int size) {
            return new ServiceVisitPost[size];
        }
    };
}

