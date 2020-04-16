package com.alps.file.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import static com.alps.file.utils.FileUtils.fileProber;

/**
 * @author:Yujie.lee
 * Date:2019年11月4日
 * TodoTODO
 */
public class TargzUtils {

    /**
     * 解压.tar文件
     */
    public static void unTarFile(File srcFile, String destPath)
            throws Exception {

        try (TarArchiveInputStream tais = new TarArchiveInputStream(
                new FileInputStream(srcFile))) {
            dearchive(new File(destPath), tais);
        }
    }


    private static void dearchive(File destFile, TarArchiveInputStream tais)
            throws Exception {

        TarArchiveEntry entry;
        while ((entry = tais.getNextTarEntry()) != null) {

            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);

            // 文件检查
            fileProber(dirFile);

            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                dearchiveFile(dirFile, tais);
            }

        }
    }

    private static void dearchiveFile(File destFile, TarArchiveInputStream tais)
            throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile));

        int count;
        byte data[] = new byte[1024];
        while ((count = tais.read(data, 0, 1024)) != -1) {
            bos.write(data, 0, count);
        }

        bos.close();
    }

    /**
     * 解压tar.gz
     */
    public static void unTargzFile(File gzFile, String descDir) throws Exception {

        GZIPInputStream inputStream = new GZIPInputStream((new FileInputStream(gzFile)));
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        dearchive(new File(descDir), tarArchiveInputStream);

    }
}
