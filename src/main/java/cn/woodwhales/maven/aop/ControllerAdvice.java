package cn.woodwhales.maven.aop;

import cn.woodwhales.common.model.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author woodwhales on 2021-11-16 21:35
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public RespVO exceptionHandler(Exception e) {
        if(e instanceof NullPointerException) {
            log.error("request error, cause by {}", "空指针异常", e);
            return RespVO.errorWithErrorMsg("空指针异常");
        }
        log.error("request error, cause by {}", e.getMessage(), e);
        return RespVO.errorWithErrorMsg(e.getMessage());
    }

}
