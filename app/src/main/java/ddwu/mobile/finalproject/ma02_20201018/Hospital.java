package ddwu.mobile.finalproject.ma02_20201018;

import java.io.Serializable;

public class Hospital implements Serializable {
    private long _id;
    private String addr; //주소
    private String yadmNm; //병원명
    private String telno; //전화번호

    public Hospital() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getYadmNm() {
        return yadmNm;
    }

    public void setYadmNm(String yadmNm) {
        this.yadmNm = yadmNm;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }


    @Override
    public String toString() {
        return "Hospital{" +
                "_id=" + _id +
                ", addr='" + addr + '\'' +
                ", yadmNm='" + yadmNm + '\'' +
                ", telno='" + telno + '\'' +
                '}';
    }
}
