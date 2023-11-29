package duegin.ginDriver.common.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*************************************
 *Class Name: gpsExifInfo
 *Description: <获取相机经纬度并且转换实际位置>
 *@author: seminar
 *************************************/
public class GpsExifInfo {

    public static void main(String[] args) throws ImageProcessingException, IOException {
        String imgDir = "/Users/duegin/Desktop";
        File[] files = new File(imgDir).listFiles();
        for (File file : files) {
            if (!file.getName().endsWith(".jpg")) {
                continue;
            }
            System.out.println("----------------------------------------\n" + file.getName());
            // 获取照片信息
            Map exifMap = readPicExifInfo(file);

            // 打印照片信息
            printPicExifInfo(exifMap);
        }
    }

    /**
     * 获取图片文件的Exif信息
     *
     * @param file
     * @return
     * @throws ImageProcessingException
     * @throws IOException
     */
    private static Map<String, String> readPicExifInfo(File file) throws ImageProcessingException, IOException {
        Map<String, String> map = new HashMap<>();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
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
//            convertLatLng2Loaction(latLng2Decimal(map.get("GPS Latitude")), latLng2Decimal(map.get("GPS Longitude")));
        }
    }

    /**
     * api_key：注册的百度api的key
     * coords：经纬度坐标
     * http://api.map.baidu.com/reverse_geocoding/v3/?ak="+api_key+"&output=json&coordtype=wgs84ll&location="+coords
     * <p>
     * 经纬度转地址信息
     *
     * @param gps_latitude
     * @param gps_longitude
     */
//    private static void convertLatLng2Loaction(double gps_latitude, double gps_longitude) throws IOException {
//        String apiKey = "FxzbLsuDDL4CS2U0M4KezOZZbGUY9iWtRn";
//
//        String res = "";
//        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + apiKey + "&output=json&coordtype=wgs84ll&location=" + (gps_latitude + "," + gps_longitude);
//        System.out.println("【url】" + url);
//        CloseableHttpClient client = HttpClients.custom().build();
//        HttpGet httpPost = new HttpGet(url);
//        httpPost.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//        CloseableHttpResponse resp = client.execute(httpPost);
//        if (resp != null) {
//            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                HttpEntity entity = resp.getEntity();
//                res = IOUtils.readStreamAsString(entity.getContent());
//                System.out.println("resJson: " + res);
//                JSONObject object = JSONObject.parseObject(res);
//                if (object.containsKey("result")) {
//                    JSONObject result = object.getJSONObject("result");
//                    if (result.containsKey("addressComponent")) {
//                        JSONObject address = object.getJSONObject("result").getJSONObject("addressComponent");
//                        System.out.println("拍摄地点：" + address.get("country") + " " + address.get("province") + " " + address.get("city") + " " + address.get("district") + " "
//                                + address.get("street") + " " + result.get("formatted_address") + " " + result.get("business"));
//                    }
//                }
//            }
//        }
//    }

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
