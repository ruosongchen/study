package com.zx.code.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class FileUtils {
    private static final int BUFFER = 5 * 1024 * 1024;

    public static void copyFile(Path srcPath, Path tarPath) throws IOException {
        Files.copy(srcPath, tarPath, StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES);
    }

    public static void clearFolder(Path folderPath) throws IOException {
        try {
            if (folderPath != null && Files.exists(folderPath)) {
                Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        try {
                            // 判断文件不被占用，则删除
                            if (file.toFile().renameTo(file.toFile())) {
                                Files.delete(file);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc)
                            throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                });
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
                    for (Path entry : stream) {
                        if (entry.toFile().isDirectory()) {
                            // 判断非空目录，空目录则删�?
                            String[] files = entry.toFile().list();
                            if (files != null && files.length == 0) {
                                // Files.delete(entry);
                                // 判断文件不被占用，则删除
                                if (entry.toFile().renameTo(entry.toFile())) {
                                    Files.delete(entry);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    public static void delFile(File file) {
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFolder(File folder) {
        for (File file : folder.listFiles()) {
            delFile(file);
        }
        delFile(folder);
    }

    // 取得�?新的匹配文件，注意匹配规则应只使用小�?
    public static File getNewestFile(String folder, String fileNameRule) {
        File[] files = Paths.get(folder).toFile().listFiles();
        long lastDate = 0;
        File lastFile = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String fileName = files[i].getName().toLowerCase();
                fileNameRule = fileNameRule.toLowerCase();
                if (fileName.matches(fileNameRule) && files[i].lastModified() >= lastDate) {
                    lastDate = files[i].lastModified();
                    lastFile = files[i];
                }
            }
        }
        return lastFile;
    }

    // 取得跟当前月份相匹对的文�?
    public static File getFileByMonth(String folder, String fileNameRule) {
        File[] files = Paths.get(folder).toFile().listFiles();
        DateFormat format = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        String monthStr = format.format(date);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String fileName = files[i].getName().toLowerCase();
                fileNameRule = fileNameRule.toLowerCase();
                String fileNameNoFormat = FileUtils.getFileName(fileName);
                if (fileName.matches(fileNameRule) && fileNameNoFormat.equals(monthStr)) {
                    return files[i];
                }
            }
        }
        return null;
    }

    /**
     * 获取不含格式的文件名，如abc.txt，则获取到abc
     * 
     * @param fileName 完整文件�?
     * @return
     */
    private static String getFileName(String fileName) {
        if (fileName != null) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return "";
    }

    public static void createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 根据byte数组，生成文�?
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        File file = new File(filePath + "\\" + fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);) {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getFileBytes(String filePath, int size) {
        byte[] buffer = null;
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(size);) {
            byte[] b = new byte[size];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
