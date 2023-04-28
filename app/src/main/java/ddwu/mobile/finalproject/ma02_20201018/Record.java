package ddwu.mobile.finalproject.ma02_20201018;

import java.io.Serializable;

public class Record implements Serializable {
    long _id;
    String recordTitle;
    String recordContent;
    String recordDate;
    String recordVName;
    String recordResult;
    String image;

    public Record(long _id, String recordTitle, String recordContent, String recordDate, String recordVName, String recordResult, String image) {
        this._id = _id;
        this.recordTitle = recordTitle;
        this.recordContent = recordContent;
        this.recordDate = recordDate;
        this.recordVName = recordVName;
        this.recordResult = recordResult;
        this.image = image;
    }

    public Record(String recordTitle, String recordContent, String recordDate, String recordVName, String recordResult, String image) {
        this.recordTitle = recordTitle;
        this.recordContent = recordContent;
        this.recordDate = recordDate;
        this.recordVName = recordVName;
        this.recordResult = recordResult;
        this.image = image;
    }

    public Record(long _id, String recordTitle, String recordContent, String recordDate, String recordVName, String recordResult) {
        this._id = _id;
        this.recordTitle = recordTitle;
        this.recordContent = recordContent;
        this.recordDate = recordDate;
        this.recordVName = recordVName;
        this.recordResult = recordResult;
    }

    public Record(String recordTitle, String recordContent, String recordDate, String recordVName, String recordResult) {
        this.recordTitle = recordTitle;
        this.recordContent = recordContent;
        this.recordDate = recordDate;
        this.recordVName = recordVName;
        this.recordResult = recordResult;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordVName() {
        return recordVName;
    }

    public void setRecordVName(String recordVName) {
        this.recordVName = recordVName;
    }

    public String getRecordResult() {
        return recordResult;
    }

    public void setRecordResult(String recordResult) {
        this.recordResult = recordResult;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}