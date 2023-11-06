package top.huanyv.admin.utils;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.core.ApplicationRunner;
import top.huanyv.bean.utils.StringUtil;

import java.util.Date;

/**
 * @author huanyv
 * @date 2023/3/29 21:03
 */
@Bean
public class Util implements ApplicationRunner {

    public static Long[] parseIds(String ids) {
        String[] split = ids.split(Constants.PATH_ID_SEPARATOR);
        Long[] result = new Long[split.length];
        if (!StringUtil.hasText(ids)) {
            result = new Long[0];
        } else {
            for (int i = 0; i < split.length; i++) {
                result[i] = Long.valueOf(split[i]);
            }
        }
        return result;
    }



    private static long startTime;

    @Override
    public void run(AppArguments appArguments) {
        startTime = new Date().getTime();
    }

    public static long getStartTime() {
        return startTime;
    }
}
