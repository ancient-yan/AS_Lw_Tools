//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ApnEntity implements Parcelable {

    private static final String TAG = "ApnEntity";

    public long id;
    public int authType;
    public int carrierEnabled;
    public int bearer;
    public String name;
    public String apn;
    public String proxy;
    public String port;
    public String user;
    public String server;
    public String password;
    public String mmsc;
    public String mcc;
    public String mnc;
    public String mmsProxy;
    public String mmsPort;
    public String type;
    public String protocol;
    public String roamingProtocol;
    public String mvnoType;
    public String mvnoMatchData;

    public static final Creator<ApnEntity> CREATOR = new Creator<ApnEntity>() {

        @Override
        public ApnEntity[] newArray(int size) {
            return new ApnEntity[size];
        }

        @Override
        public ApnEntity createFromParcel(Parcel source) {
            Log.i(TAG, "createFromParcel");
            ApnEntity entity = new ApnEntity();

            entity.id = source.readLong();
            entity.authType = source.readInt();
            entity.carrierEnabled = source.readInt();
            entity.bearer = source.readInt();

            entity.name = source.readString();
            entity.apn = source.readString();
            entity.proxy = source.readString();
            entity.port = source.readString();
            entity.user = source.readString();
            entity.server = source.readString();
            entity.password = source.readString();
            entity.mmsc = source.readString();
            entity.mcc = source.readString();
            entity.mnc = source.readString();
            entity.mmsProxy = source.readString();
            entity.mmsPort = source.readString();
            entity.type = source.readString();
            entity.protocol = source.readString();
            entity.roamingProtocol = source.readString();
            entity.mvnoType = source.readString();
            entity.mvnoMatchData = source.readString();
            return entity;
        }
    };

    public ApnEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(authType);
        dest.writeInt(carrierEnabled);
        dest.writeInt(bearer);

        dest.writeString(name);
        dest.writeString(apn);
        dest.writeString(proxy);
        dest.writeString(port);
        dest.writeString(user);
        dest.writeString(server);
        dest.writeString(password);
        dest.writeString(mmsc);
        dest.writeString(mcc);
        dest.writeString(mnc);
        dest.writeString(mmsProxy);
        dest.writeString(mmsPort);
        dest.writeString(type);
        dest.writeString(protocol);
        dest.writeString(roamingProtocol);
        dest.writeString(mvnoType);
        dest.writeString(mvnoMatchData);
    }

    @Override
    public String toString() {
        return String.format("id %d, name %s, apn %s, proxy %s, port %s, user %s, server %s, password %s," +
                " mmsc %s, mcc %s, mnc %s, mmsProxy %s, mmsPort %s " +
                "authType %s, apnType %s, protocol %s, roamingProtocol %s, bearer %s, mvnoType %s, " +
                "mvnoMatchData %s, carrierEnabled %s",
                id, name, apn, proxy, port, user, server, password, mmsc, mcc, mnc, mmsProxy, mmsPort,
                authType, type, protocol, roamingProtocol, bearer, mvnoType, mvnoMatchData, carrierEnabled);
    }
}
