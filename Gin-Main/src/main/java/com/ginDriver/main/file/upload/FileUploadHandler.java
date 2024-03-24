package com.ginDriver.main.file.upload;


import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.domain.dto.UploadStatusDTO;
import com.ginDriver.main.file.generator.IFilePathGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DueGin
 */
public abstract class FileUploadHandler {

    private static final Logger log = LoggerFactory.getLogger(FileUploadHandler.class);

    protected IFilePathGenerator filePathGenerator;

//    protected IFileNameGenerator fileNameGenerator;

    abstract public UploadStatusDTO upload(ChunkDTO chunkDto);

    public FileUploadHandler(IFilePathGenerator filePathGenerator) {
        this.filePathGenerator = filePathGenerator;
    }

    //    用户指定路径（逻辑路径）
//    abstract public String upload(ChunkDTO chunkDto, String path);
}
