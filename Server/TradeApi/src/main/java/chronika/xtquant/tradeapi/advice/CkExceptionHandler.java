package chronika.xtquant.tradeapi.advice;

import chronika.xtquant.tradeapi.model.CkApiResponse;
import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.common.infra.exception.CkException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class CkExceptionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CkExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CkException.class)
    public CkApiResponse<?> handleGkException(CkException ex) {
        return CkApiResponse.error(ex.getCode(), ex.getMsg(), ex.getData());
    }


    @ExceptionHandler(Exception.class)
    public CkApiResponse<?> handleException(Exception ex) {
        log.error("Oops, surprise~!", ex);
        return CkApiResponse.error(CkError.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public CkApiResponse<?> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            String msg = CkError.INVALID_PARAM + ":'" + fieldError.getField() + "'";
            return CkApiResponse.error(CkError.INVALID_PARAM.code, msg);
        } else {
            return CkApiResponse.error(CkError.INVALID_PARAM);
        }
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public CkApiResponse<?> handleIllegalArgumentException(Exception e) {
        return CkApiResponse.error(CkError.INVALID_PARAM.code, e.getMessage());
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public CkApiResponse<?> handleNoResourceFoundException(NoResourceFoundException e) {
        return CkApiResponse.error(CkError.FUNC_NOT_SUPPORTED);
    }

}
