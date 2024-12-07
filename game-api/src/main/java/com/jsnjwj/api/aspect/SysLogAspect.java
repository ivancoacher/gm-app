package com.jsnjwj.api.aspect;

import com.jsnjwj.common.utils.ThreadLocalUtil;
import com.jsnjwj.user.dao.TcOptLogDao;
import com.jsnjwj.user.entity.TcOptLog;
import com.jsnjwj.user.enums.OperateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 系统日志记录
 */
@Slf4j
@Aspect
@Component
@Configuration
public class SysLogAspect {

    @Resource
    private TcOptLogDao optLogDao;

    @Resource
    private HttpServletRequest request;

    @Around("execution(* com.jsnjwj..*(..)) && @annotation(methodLog)")
    public Object around(ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
        OperateTypeEnum operType = methodLog.operType();
        TcOptLog sysLog = new TcOptLog();
        if (!OperateTypeEnum.LOGIN.equals(operType)) {
            String userIdStr = ThreadLocalUtil.getCurrentUserId().toString();
            if (null != userIdStr) {
                sysLog.setUserId(Long.valueOf(userIdStr));
            }
        }
        sysLog.setOperateType(operType.getCode());
        sysLog.setRemark(methodLog.remark());
        sysLog.setCreateTime(new Date());

        // 异步保存操作日志
        Operate operate = new Operate();
        operate.setSysLog(sysLog);
        operate.setSysLogMapper(optLogDao);
        new Thread(operate).start();

        return joinPoint.proceed();
    }

    static class Operate implements Runnable {

        private TcOptLogDao sysLogMapper;

        private TcOptLog sysLog;

        public void setSysLog(TcOptLog sysLog) {
            this.sysLog = sysLog;
        }

        public void setSysLogMapper(TcOptLogDao sysLogMapper) {
            this.sysLogMapper = sysLogMapper;
        }

        @Override
        public void run() {
            sysLogMapper.insert(sysLog);
        }

    }

}
