package com.joggim.ktalk.common.util;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class AudioFileUtil {

    public static ByteArrayResource toByteArrayResource(MultipartFile audioFile) {
        try {
            return new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
        }
    }
}
