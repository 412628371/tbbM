package com.xinguang.tubobo.impl.disconf;

import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;
import com.hzmux.hzcms.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class UpdatePipelineCallback implements IDisconfUpdatePipeline {

    private static Logger logger = LoggerFactory.getLogger(UpdatePipelineCallback.class);

    /**
     * 配置文件更改回调方法
     * @param key 文件名
     * @param filePath 文件路径
     * @throws Exception
     */

    @Override
    public void reloadDisconfFile(String key, String filePath) throws Exception {
        logger.info("Disconf update callback. key: " + key + ", filePath: " + filePath);
        logger.info(readFile(filePath, Charset.defaultCharset()));
        //清除本地map所有缓存
        Global.disConfUpdatedAndClearMap();
    }

    @Override
    public void reloadDisconfItem(String key, Object content) throws Exception {

    }

    /**
     * 读取文件内容
     * @param path 文件路径
     * @param encoding 编码
     * @return 文件内容
     * @throws IOException
     */
    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}

