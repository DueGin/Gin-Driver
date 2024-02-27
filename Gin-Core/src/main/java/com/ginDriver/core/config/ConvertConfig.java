package com.ginDriver.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 有时候因为某些原因，比如表单数据，接收的时候好像就不能利用{@link JacksonObjectMapper}来转换成功
 * @author DueGin
 */
@Configuration
@Slf4j
public class ConvertConfig {
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConvert() {
        // 这里不能写成lambda简写，不然启动会报错
        return new Converter<>() {
            @Override
            public LocalDateTime convert(String source) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = null;
                try {
                    switch (source.length()) {
                        case 10:
                            log.debug("传过来的是日期格式：{}", source);
                            source = source + " 00:00:00";
                            break;
                        case 13:
                            log.debug("传过来的是日期 小时格式：{}", source);
                            source = source + ":00:00";
                            break;
                        case 16:
                            log.debug("传过来的是日期 小时:分钟格式：{}", source);
                            source = source + ":00";
                            break;
                    }
                    dateTime = LocalDateTime.parse(source, df);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage(), e);
                }
                return dateTime;
            }
        };
    }
}
