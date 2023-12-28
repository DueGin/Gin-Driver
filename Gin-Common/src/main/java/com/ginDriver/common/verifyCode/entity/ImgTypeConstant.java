package com.ginDriver.common.verifyCode.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关于常见图片文件的枚举常量
 * @author cuberxp
 */
@Getter
@AllArgsConstructor
public enum ImgTypeConstant {
	JPEG("FFD8FF","jpg"),
	PNG("89504E47","png"),
	GIF("47494638", "gif"),
	XLS("D0CF11E0A1B11AE1", "xls"),
	XLSX("504B0304", "xlsx")
	;

	/**
	 * 文件头标志
	 */
	private final String value;

	/**
	 * 文件的后缀名
	 */
	private final String suffix;
}