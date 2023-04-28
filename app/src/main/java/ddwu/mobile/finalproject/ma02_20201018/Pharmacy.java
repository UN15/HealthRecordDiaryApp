package ddwu.mobile.finalproject.ma02_20201018;

import java.io.Serializable;

public class Pharmacy implements Serializable {
    private long _id;
    private String pAddr; //주소
    private String pYadmNm; //약국명
    private String pTelno; //전화번호

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getpAddr() {
        return pAddr;
    }

    public void setpAddr(String pAddr) {
        this.pAddr = pAddr;
    }

    public String getpYadmNm() {
        return pYadmNm;
    }

    public void setpYadmNm(String pYadmNm) {
        this.pYadmNm = pYadmNm;
    }

    public String getpTelno() {
        return pTelno;
    }

    public void setpTelno(String pTelno) {
        this.pTelno = pTelno;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "_id=" + _id +
                ", pAddr='" + pAddr + '\'' +
                ", pYadmNm='" + pYadmNm + '\'' +
                ", pTelno='" + pTelno + '\'' +
                '}';
    }
}
