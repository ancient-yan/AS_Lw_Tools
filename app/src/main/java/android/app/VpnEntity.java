package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class VpnEntity implements Parcelable {

    private static final String TAG = "VpnEntity";

    public static final Creator<VpnEntity> CREATOR = new Creator() {
        @Override
        public VpnEntity[] newArray(int size) {
            return new VpnEntity[size];
        }

        @Override
        public VpnEntity createFromParcel(Parcel source) {
            Log.i(TAG, "createFromParcel");
            return new VpnEntity(source.readString(), source.readInt(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readInt(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readInt());
        }
    };
    private String name;
    private int type;
    private String server;
    private String username;
    private String pwd;
    private String searchDomains;
    private String dnsServers;
    private String routes;
    private int mppe;
    private String mL2tpSecret;
    private String mIpsecIdentifier;
    private String ipsecSecret;
    private String l2tpSecret;
    private String mIpsecUserCert;
    private String mIpsecCaCert;
    private String ipsecServerCert;
    private int mSaveLogin;

    public VpnEntity() {
    }

    public VpnEntity(String name, int type, String server, String username, String pwd, String searchDomains, String dnsServers, String routes, int mppe, String mL2tpSecret, String mIpsecIdentifier, String ipsecSecret, String l2tpSecret, String mIpsecUserCert, String mIpsecCaCert, String ipsecServerCert, int mSaveLogin) {
        this.name = name;
        this.type = type;
        this.server = server;
        this.username = username;
        this.pwd = pwd;
        this.searchDomains = searchDomains;
        this.dnsServers = dnsServers;
        this.routes = routes;
        this.mppe = mppe;
        this.mL2tpSecret = mL2tpSecret;
        this.mIpsecIdentifier = mIpsecIdentifier;
        this.ipsecSecret = ipsecSecret;
        this.l2tpSecret = l2tpSecret;
        this.mIpsecUserCert = mIpsecUserCert;
        this.mIpsecCaCert = mIpsecCaCert;
        this.ipsecServerCert = ipsecServerCert;
        this.mSaveLogin = mSaveLogin;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSearchDomains() {
        return this.searchDomains;
    }

    public void setSearchDomains(String searchDomains) {
        this.searchDomains = searchDomains;
    }

    public String getDnsServers() {
        return this.dnsServers;
    }

    public void setDnsServers(String dnsServers) {
        this.dnsServers = dnsServers;
    }

    public String getRoutes() {
        return this.routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public int getMppe() {
        return this.mppe;
    }

    public void setMppe(int mppe) {
        this.mppe = mppe;
    }

    public String getmL2tpSecret() {
        return this.mL2tpSecret;
    }

    public void setmL2tpSecret(String mL2tpSecret) {
        this.mL2tpSecret = mL2tpSecret;
    }

    public String getmIpsecIdentifier() {
        return this.mIpsecIdentifier;
    }

    public void setmIpsecIdentifier(String mIpsecIdentifier) {
        this.mIpsecIdentifier = mIpsecIdentifier;
    }

    public String getIpsecSecret() {
        return this.ipsecSecret;
    }

    public void setIpsecSecret(String ipsecSecret) {
        this.ipsecSecret = ipsecSecret;
    }

    public String getL2tpSecret() {
        return this.l2tpSecret;
    }

    public void setL2tpSecret(String l2tpSecret) {
        this.l2tpSecret = l2tpSecret;
    }

    public String getmIpsecUserCert() {
        return this.mIpsecUserCert;
    }

    public void setmIpsecUserCert(String mIpsecUserCert) {
        this.mIpsecUserCert = mIpsecUserCert;
    }

    public String getmIpsecCaCert() {
        return this.mIpsecCaCert;
    }

    public void setmIpsecCaCert(String mIpsecCaCert) {
        this.mIpsecCaCert = mIpsecCaCert;
    }

    public String getIpsecServerCert() {
        return this.ipsecServerCert;
    }

    public void setIpsecServerCert(String ipsecServerCert) {
        this.ipsecServerCert = ipsecServerCert;
    }

    public int getSaveLogin() {
        return this.mSaveLogin;
    }

    public void setmSaveLogin(int mSaveLogin) {
        this.mSaveLogin = mSaveLogin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.i(TAG, "writeToParcel");
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.server);
        dest.writeString(this.username);
        dest.writeString(this.pwd);
        dest.writeString(this.searchDomains);
        dest.writeString(this.dnsServers);
        dest.writeString(this.routes);
        dest.writeInt(this.mppe);
        dest.writeString(this.mL2tpSecret);
        dest.writeString(this.mIpsecIdentifier);
        dest.writeString(this.ipsecSecret);
        dest.writeString(this.l2tpSecret);
        dest.writeString(this.mIpsecUserCert);
        dest.writeString(this.mIpsecCaCert);
        dest.writeString(this.ipsecServerCert);
        dest.writeInt(this.mSaveLogin);
    }

    @Override
    public String toString() {
        return String.format("name %s, type %s, server %s, username %s, pwd %s, searchDomains %s, dnsServers %s," +
                        " routes %s, mppe %s, mL2tpSecret %s, mIpsecIdentifier %s, ipsecSecret %s " +
                        "l2tpSecret %s, mIpsecUserCert %s, mIpsecCaCert %s, ipsecServerCert %s, mSaveLogin %s",
                name, type, server, username, pwd, searchDomains, dnsServers, routes, mppe, mL2tpSecret, mIpsecIdentifier, ipsecSecret,
                l2tpSecret, mIpsecUserCert, mIpsecCaCert, ipsecServerCert, mSaveLogin);
    }
}
