package com.ginDriver.main.file.upload;


import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.generator.IFilePathGenerator;

public abstract class FileUploadHandler {

    protected IFilePathGenerator filePathGenerator;

//    protected IFileNameGenerator fileNameGenerator;

    abstract public UploadStatus upload(ChunkDTO chunkDto);


//    用户指定路径（逻辑路径）
//    abstract public String upload(ChunkDTO chunkDto, String path);
}
