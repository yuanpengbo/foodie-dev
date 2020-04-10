package org.yuan.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.yuan.utils.JSONResult;

@RestControllerAdvice
public class CustomExceptionHandler {
    /**
     * MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex){
        return JSONResult.errorMsg("文件大小上传大小不能超过500k");
    }
}
