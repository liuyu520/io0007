package com.common.bean.zip;


import com.common.bean.UploadFileDto;

import java.util.List;

/**
 * Created by whuanghkl on 17/7/18.
 */
public class DeCompressDto {
    private boolean isSuccess;
    private List<UploadFileDto> files;

    public boolean isSuccess() {
        return isSuccess;
    }

    public DeCompressDto setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public List<UploadFileDto> getFiles() {
        return files;
    }

    public void setFiles(List<UploadFileDto> files) {
        this.files = files;
    }
}
