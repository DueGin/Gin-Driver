package com.ginDriver.main.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.ginDriver.main.domain.po.MediaExif;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: gpsExifInfo
 * Description: <获取相机经纬度并且转换实际位置>
 *
 * @author seminar
 **/
public class GpsExifInfoUtil {

    public static void main(String[] args) throws ImageProcessingException, IOException {
        String imgDir = "/Users/duegin/Desktop";
        File[] files = new File(imgDir).listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                System.out.println("----------------------------------------\n" + file.getName());
                // 获取照片信息
                Map<String, String> exifMap = readPicExifInfo(file);

                // 打印照片信息
                printPicExifInfo(exifMap);
            }
        }
    }

    public static Map<String, String> readExifInfo(InputStream in) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(in);
        Map<String, String> map = new HashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                // 输出所有属性
                System.out.format("[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());
                map.put(tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
        return map;
    }

    /**
     * 获取图片文件的Exif信息
     *
     * @param file
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    public static Map<String, String> readPicExifInfo(File file) throws ImageProcessingException, IOException {
        Map<String, String> map = new HashMap<>();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        // 获取系统文件字段，例如创建时间
        BasicFileAttributes read = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        Instant instant = read.creationTime().toInstant();
        LocalDateTime createDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        System.out.println("创建时间：" + createDateTime);

        map.put("fileCreateTime", createDateTime.toString());

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                // 输出所有属性
                System.out.format("[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());
                map.put(tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
        return map;
    }

    public static MediaExif convertToExifInfoDTO(Map<String, String> map) {
        MediaExif exif = new MediaExif();
        exif.setWidth(Integer.valueOf(map.get("Image Width").split(" ")[0]));
        exif.setHeight(Integer.valueOf(map.get("Image Height").split(" ")[0]));
        exif.setMimeType(map.get("Detected MIME Type"));

        // 系统读取文件创建时间
        String fileCreateTime = map.get("fileCreateTime");
        // exif文件创建时间
        String fileDateTime = map.get("Date/Time");
        LocalDateTime createTime = null;
        if (fileDateTime != null) {
            createTime = LocalDateTime.parse(fileDateTime, DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        } else if (fileCreateTime != null) {
            createTime = LocalDateTime.parse(fileCreateTime);
        }
        exif.setCreateTime(createTime);

        exif.setModel(map.get("Model"));
        return exif;
    }

    /**
     * 打印照片Exif信息
     *
     * @param map
     */
    private static void printPicExifInfo(Map<String, String> map) {
        String[] strings = new String[]{"Compression", "Image Width", "Image Height", "Make", "Model", "Software",
                "GPS Version ID", "GPS Latitude", "GPS Longitude", "GPS Altitude", "GPS Time-Stamp", "GPS Date Stamp",
                "ISO Speed Ratings", "Exposure Time", "Exposure Mode", "F-Number", "Focal Length 35", "Color Space", "File Source", "Scene Type"};
        String[] names = new String[]{"压缩格式", "图像宽度", "图像高度", "拍摄手机", "型号", "手机系统版本号",
                "gps版本", "经度", "纬度", "高度", "UTC时间戳", "gps日期",
                "iso速率", "曝光时间", "曝光模式", "光圈值", "焦距", "图像色彩空间", "文件源", "场景类型"};
        for (int i = 0; i < strings.length; i++) {
            if (map.containsKey(strings[i])) {
                if ("GPS Latitude".equals(strings[i]) || "GPS Longitude".endsWith(strings[i])) {
                    System.out.println(names[i] + "  " + strings[i] + " : " + map.get(strings[i]) + ", °转dec: " + latLng2Decimal(map.get(strings[i])));
                } else {
                    System.out.println(names[i] + "  " + strings[i] + " : " + map.get(strings[i]));
                }
            }
        }

        // 经纬度转location地址信息
        if (map.containsKey("GPS Latitude") && map.containsKey("GPS Longitude")) {
            System.out.println("lat: " + map.get("GPS Latitude") + " log: " + map.get("GPS Longitude"));
        }
    }


    /***
     * 经纬度坐标格式转换（* °转十进制格式）
     * @param gps
     */
    public static double latLng2Decimal(String gps) {
        String a = gps.split("°")[0].replace(" ", "");
        String b = gps.split("°")[1].split("'")[0].replace(" ", "");
        String c = gps.split("°")[1].split("'")[1].replace(" ", "").replace("\"", "");
        double gps_dou = Double.parseDouble(a) + Double.parseDouble(b) / 60 + Double.parseDouble(c) / 60 / 60;
        return gps_dou;
    }
}
