package com.ginDriver.common.utils;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class ImgTestCode {
    public static void main(String[] args) throws Exception {

        File file = new File("/Users/duegin/Desktop/IMG_20230706_231846.jpg");

//        readMediaInfoMediaExif(file);
        Map<String, Map<String, String>> map = readMediaInfoMap(file);
        map.forEach((k, v) -> {
            System.out.println("集合"+ k+"：");
            v.forEach((k1, v1) -> System.out.println(k1 + "==>>" + v1));
        });
    }

    /**
     * 提取照片里面的信息(提取照片元数据)
     * 读取照片宽高、经纬度、拍摄时间等信息
     * 这些信息属于照片的EXIF(exchangeable image file format：可交换图像文件格式)信息，它被包含在JPEG、PNG等图像文件中
     * EXIF包含很多信息
     * 分辨率
     * 文件类型
     * F值/曝光时间/ISO
     * 图像旋转
     * 日期/时间
     * 白平衡
     * 缩略图
     * 焦距
     * 闪光
     * 镜头
     * 文件类型
     * 相机类型
     * 使用的软件
     * 拍摄时间和GPS标签
     * 等
     * GPS标签中包含经纬度信息
     * 通过经纬度进行逆地址解析就可以得到照片的拍摄地址
     *
     * @param file 照片文件
     * @throws ImageProcessingException
     * @throws Exception
     */
    private static MediaExif readMediaInfoMediaExif(File file) throws ImageProcessingException, Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(file);

        MediaExif mediaExif = new MediaExif();

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                if (tagName.equals("Image Height")) {
                    System.err.println("图片高度: " + desc);
                    mediaExif.imageHeight = Float.valueOf(desc.replace(" pixels", ""));
                } else if (tagName.equals("Image Width")) {
                    System.err.println("图片宽度: " + desc);
                    mediaExif.imageWidth = Float.valueOf(desc.replace(" pixels", ""));
                } else if (tagName.equals("Date/Time Original")) {
                    System.err.println("拍摄时间: " + desc);
                    mediaExif.shootingTime = LocalDateTime.parse(desc, DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
                } else if (tagName.equals("GPS Latitude")) {
                    mediaExif.latitude = desc;
                    System.err.println("纬度(度分秒格式) : " + desc);
                    System.err.println("纬度(十进制格式) : " + pointToLatlong(desc));
                } else if (tagName.equals("GPS Longitude")) {
                    mediaExif.longitude = desc;
                    System.err.println("经度(度分秒格式): " + desc);
                    System.err.println("经度(十进制格式): " + pointToLatlong(desc));
                }
            }
        }

        return mediaExif;
    }

    public static Map<String, Map<String, String>> readMediaInfoMap(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        Map<String, Map<String, String>> map = new HashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            Map<String, String> m = new HashMap<>();
            String directoryName = directory.getName();
            //所有目录
            System.err.println("目录名：" + directoryName);
            //目录下的所有标签
            for (Tag tag : directory.getTags()) {
                int tagType = tag.getTagType();
                String tagName = tag.getTagName();
                String description = tag.getDescription();
                System.out.format("标签类型：%d=====标签名:%s====描述：%s\n", tagType, tagName, description);
                m.put(tagName, description);
            }
            map.put(directoryName, m);
        }
        return map;
    }

    /**
     * 经纬度格式转换为度分秒格式 ,如果需要的话可以调用该方法进行转换
     *
     * @param point 坐标点
     * @return
     */
    public static String pointToLatlong(String point) {
        Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
        Double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
        Double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
        Double duStr = du + fen / 60 + miao / 60 / 60;
        return duStr.toString();
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

    @Getter
    private static class MediaExif {

        private String fileName;
        private Float imageHeight;
        private Float imageWidth;
        /**
         * 设备型号
         */
        private String model;
        /**
         * 拍摄时间
         */
        private LocalDateTime shootingTime;
        private String placeName;
        private String latitude;
        private String longitude;
    }


}

