package com.demo.runwu.utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Properties;

@Slf4j
public class SFTPUtil {

    private static ChannelSftp sftp;

    private static SFTPUtil instance = null;

    private SFTPUtil() {
    }

    public static SFTPUtil getInstance(String host, int port, String username, String passwd) {

        if (instance == null) {
            instance = new SFTPUtil();
            // 获取连接
        }
        sftp = instance.connect(host, port, username, passwd);
        return instance;
    }

    public static void close() throws JSchException {
        if (sftp.isConnected()) {
            sftp.disconnect();
            sftp.getSession().disconnect();
        }
    }

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            log.info("SFTP session connected.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("connected to " + host);
        } catch (Exception e) {
            log.error("SFTP 连接异常", e);
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  文件上传路径
     * @param uploadFile 要上传的文件
     * @return 上传结果
     */
    public boolean upload(String directory, String uploadFile) {

        try {
            sftp.cd(directory);
            try {
                // 在目标路径下创建二级路径，如不需要，可以注释掉
                sftp.mkdir("20200114");
                sftp.cd("20200114");
            } catch (Exception e) {
                log.error("创建文件夹失败");
            }
            File file = new File(uploadFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            sftp.put(fileInputStream, file.getName());
            fileInputStream.close();
            return true;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public Boolean download(String directory, String downloadFile, String saveFile) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, Files.newOutputStream(file.toPath()));
            return true;
        } catch (Exception e) {
            log.error("下载文件失败", e);
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public Boolean delete(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            return true;
        } catch (Exception e) {
            log.info("文件删除异常", e);
            return false;
        }
    }

    public static void main(String[] args) {

        // SFTP 服务器信息
        String host = "48.101.227.5";
        int port = 22;
        String username = "testuser";
        String passwd = "testpassword";

        SFTPUtil instance = SFTPUtil.getInstance(host, port, username, passwd);

        // 上传文件
        String directory = "sftpfile/";
        String uploadfile = "D:\\testfile.log";
        boolean uploadResult = instance.upload(directory, uploadfile);
        log.info("上传文件 {} 结果：{}", uploadfile, uploadResult);

        // 下载文件
        String directory1 = "/home/testuser/sftpfile/20200114";
        String downloadFile = "testfile.log";
        String saveFile = "C:\\testfile.log";
        boolean downloadResult = instance.download(directory1, downloadFile, saveFile);
        log.info("下载文件 {} 结果：{}", (directory1 + "/" + downloadFile), downloadResult);

        // 删除文件
        String directory2 = "/home/testuser";
        String deleteFile = "testcopy.txt";
        Boolean deleteResult = instance.delete(directory2, deleteFile);
        log.info("删除文件 {} 结果：{}", (directory2 + "/" + deleteFile), deleteResult);

        try {
            SFTPUtil.close();
        } catch (JSchException e) {
            log.info("sftp关闭异常");
        }
    }
}
