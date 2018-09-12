package cree.mvp.ui.fragment.video.bean;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Title:
 * Description:
 * CreateTime:2017/7/11  15:42
 *
 * @author luyongjiang
 * @version 1.0
 */
public class VideoCacheBean implements Serializable {
    private LinkedList<File> fileList;

    public LinkedList<File> getFileList() {
        return fileList;
    }

    public VideoCacheBean setFileList(LinkedList<File> fileList) {
        this.fileList = fileList;
        return this;
    }

    public static VideoCacheBean getCache() {
        return new VideoCacheBean();
    }
}
