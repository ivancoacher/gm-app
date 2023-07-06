package com.jsnjwj.api.aspect;

import com.jsnjwj.user.dao.OptLogDao;
import com.jsnjwj.user.entity.OptLog;
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
 *
 * @author xiemingzhi
 * @since 2019/3/7 15:00
 */
@Slf4j
@Aspect
@Component
@Configuration
public class SysLogAspect {

	@Resource
	private OptLogDao optLogDao;

	@Resource
	private HttpServletRequest request;

	@Around("execution(* com.jsnjwj..*(..)) && @annotation(methodLog)")
	public Object around(ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
		OperateTypeEnum operType = methodLog.operType();
		OptLog sysLog = new OptLog();
		if (!OperateTypeEnum.LOGIN.equals(operType)) {
			String userIdStr = (String) request.getAttribute("identifyId");
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

		private OptLogDao sysLogMapper;

		private OptLog sysLog;

		public void setSysLog(OptLog sysLog) {
			this.sysLog = sysLog;
		}

		public void setSysLogMapper(OptLogDao sysLogMapper) {
			this.sysLogMapper = sysLogMapper;
		}

		@Override
		public void run() {
			sysLogMapper.insert(sysLog);
		}

	}

}
