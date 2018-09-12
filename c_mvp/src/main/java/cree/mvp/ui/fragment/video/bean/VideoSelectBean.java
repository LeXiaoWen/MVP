package cree.mvp.ui.fragment.video.bean;

import java.io.File;

/**
 * Title:
 * Description:
 * CreateTime:2017/7/11  13:39
 *
 * @author luyongjiang
 * @version 1.0
 */
public class VideoSelectBean {
    private File mFile;

    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    @Override
    public String toString() {
        return "VideoSelectBean{" +
                "mFile=" + mFile.toString() +
                '}';
    }
}
