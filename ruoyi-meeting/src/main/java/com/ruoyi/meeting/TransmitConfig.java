package com.ruoyi.meeting;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Snow
 */
@Data
@Component
@ConfigurationProperties(prefix = "meeting.transmit")
public class TransmitConfig {

    @Getter
    private static String appBundle;

    @Getter
    private static String baseDirPath;

    @Getter
    private static String baseDirName;

    @Getter
    private static String subDirDb;

    @Getter
    private static String subDirDraft;

    // @Getter
    // private static String subDirStudy;

    // @Getter
    // private static String subDirMaterial;

    @Getter
    private static Source source = new Source();

    @Getter
    private static Target target = new Target();

    @Data
    public static class Source {
        private String all;
        private String pathDb;
        private String pathDraft;
        // private String pathStudy;
        // private String pathMaterial;
    }

    @Data
    public static class Target {
        private String all;
        private String pathDb;
        // private String pathStudy;
        // private String pathMaterial;
    }

    public void setAppBundle(String appBundle) {
        TransmitConfig.appBundle = appBundle;
    }

    public void setBaseDirPath(String baseDirPath) {
        FileUtil.mkdir(baseDirPath);
        TransmitConfig.baseDirPath = baseDirPath;
    }

    public void setBaseDirName(String baseDirName) {
        FileUtil.mkdir(TransmitConfig.getBaseDirPath() + FileUtil.FILE_SEPARATOR + baseDirName);
        TransmitConfig.baseDirName = baseDirName;
    }

    public void setSubDirDb(String subDirDb) {
        FileUtil.mkdir(TransmitConfig.getBaseDirPath() + FileUtil.FILE_SEPARATOR + TransmitConfig.getBaseDirName() +
                FileUtil.FILE_SEPARATOR + subDirDb);
        TransmitConfig.subDirDb = subDirDb;
    }

    public void setSubDirDraft(String subDirDraft) {
        FileUtil.mkdir(TransmitConfig.getBaseDirPath() + FileUtil.FILE_SEPARATOR + TransmitConfig.getBaseDirName() +
                FileUtil.FILE_SEPARATOR + subDirDraft);
        TransmitConfig.subDirDraft = subDirDraft;
    }

    // public void setSubDirStudy(String subDirStudy) {
    //     TransmitConfig.subDirStudy = subDirStudy;
    // }

    // public void setSubDirMaterial(String subDirMaterial) {
    //     TransmitConfig.subDirMaterial = subDirMaterial;
    // }

    public void setSource(Source source) {
        TransmitConfig.source = source;
    }

    public void setTarget(Target target) {
        TransmitConfig.target = target;
    }

    public static String getDbFileDir() {
        String fileDir =
                TransmitConfig.getBaseDirPath() + FileUtil.FILE_SEPARATOR +
                TransmitConfig.getBaseDirName() + FileUtil.FILE_SEPARATOR + TransmitConfig.getSubDirDb();

        FileUtil.mkdir(fileDir);
        return fileDir;
    }

    private static String getDbFilePath() {
        String dbFilePath = getDbFileDir() + FileUtil.FILE_SEPARATOR + "transmit.db";
        FileUtil.touch(dbFilePath);
        return dbFilePath;
    }

    public static String getTopicFileDir(Long topicId) {
        String fileDir =
                TransmitConfig.getBaseDirPath() + FileUtil.FILE_SEPARATOR +
                TransmitConfig.getBaseDirName() + FileUtil.FILE_SEPARATOR +
                TransmitConfig.getSubDirDraft() + FileUtil.FILE_SEPARATOR + topicId;

        FileUtil.mkdir(fileDir);
        return fileDir;
    }

}
