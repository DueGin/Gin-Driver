package com.ginDriver.common.minio.service;

import com.ginDriver.common.minio.properties.MinioProperties;
import com.google.common.collect.Lists;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Slf4j
@Component
public class MinioComponent {

    private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;

    @Resource
    private MinioClient minio;

    @Resource
    private MinioProperties minioProperties;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minio.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return true;
        }

        minio.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        return true;
    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     */
    @SneakyThrows
    public List<String> listBucketNames() {
        List<Bucket> list = listBuckets();
        return list.stream().filter(Objects::nonNull).map(o -> o.name()).collect(Collectors.toList());
    }

    /**
     * 列出所有存储桶
     *
     * @return
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minio.listBuckets();
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public boolean removeBucket(String bucketName) {
        Iterable<Result<Item>> myObjects = listObjects(bucketName);
        for (Result<Item> result : myObjects) {
            Item item = result.get();
            // 有对象文件，则删除失败
            if (item.size() > 0) {
                return false;
            }
        }
        // 删除存储桶，注意，只有存储桶为空时才能删除成功。
        minio.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        return !bucketExists(bucketName);
    }


    /**
     * 在test桶里创建文件夹对象
     *
     * @param fileName 文件
     */
    @Deprecated
    public void createFile(String fileName) {
        try {
            if (!bucketExists("test")) {
                // minio服务器创建桶
                makeBucket("test");
            }
            minio.putObject(
                    PutObjectArgs.builder().bucket("test").object(fileName).stream(
                                    new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return iter
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {
        return minio.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        List<String> ret = Lists.newArrayList();
        Iterable<Result<Item>> myObjects = listObjects(bucketName);
        for (Result<Item> result : myObjects) {
            Item item = result.get();
            ret.add(item.objectName());
        }

        return ret;
    }

    /**
     * 文件上传
     *
     * @param bucketName    桶名称
     * @param multipartFile 上传文件
     */
    @SneakyThrows
    public ObjectWriteResponse putObject(String bucketName, MultipartFile multipartFile) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(multipartFile.getName())
                .contentType(multipartFile.getContentType())
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .build();

        return minio.putObject(args);
    }

    /**
     * 通过InputStream上传对象
     *
     * @param bucketName  存储桶名称
     * @param objectName  存储桶里的对象名称
     * @param in          要上传的流
     * @param contentType 要上传的文件类型 MimeTypeUtils.IMAGE_JPEG_VALUE
     * @return
     */
    @SneakyThrows
    public ObjectWriteResponse putObject(String bucketName, String objectName, InputStream in, String contentType) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(contentType)
                .stream(in, in.available(), -1)
                .build();

        return minio.putObject(args);
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            return null;
        }

        StatObjectResponse resp = statObject(bucketName, objectName);
        return resp == null ? null : minio.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {
        return !bucketExists(bucketName) ? null : minio.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param fileName 文件名
     */
    @SneakyThrows
    public Boolean remove(String bucketName, String fileName) {
        try {
            minio.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 获取文件URL
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expiry     过期时间
     * @return url
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName, Integer expiry) {
        GetPresignedObjectUrlArgs.Builder builder = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName);
        GetPresignedObjectUrlArgs build;
        if (expiry != null) {
            build = builder.expiry(expiry).build();
        } else {
            build = builder.build();
        }

        return minio.getPresignedObjectUrl(build);
    }

    /**
     * 获取文件URL，默认按配置文件配置的，未配置则不设置过期时间
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     */
    public String getObjectUrl(String bucketName, String objectName) {
        Integer expiry = null;
        if (minioProperties.getExpiry() != null) {
            expiry = minioProperties.getExpiry();
        }
        return getObjectUrl(bucketName, objectName, expiry);
    }
}

