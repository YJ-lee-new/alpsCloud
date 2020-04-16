package com.alps.platform.log.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
public class LogImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// TODO Auto-generated method stub
//		importingClassMetadata.getAllAnnotationAttributes(EnableEcho.class.getName());
		
		
		
		
		return new String[] { 
				"com.alps.platform.log.aop.LogAnnotationAOP",
				"com.alps.platform.log.service.impl.LogServiceImpl",
				"com.alps.platform.log.config.LogAutoConfig"
				
		};
	}

}