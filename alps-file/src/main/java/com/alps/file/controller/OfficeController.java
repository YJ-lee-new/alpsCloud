package com.alps.file.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alps.file.utils.FileUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * @author:Yujie.lee
 * Date:2019年11月4日
 * TodoTODO
 */
@Controller
@RequestMapping("/office")
public class OfficeController {


    @Value("${fileServer.domain}")
    String domain;

    @Value("${files.docservice.url.api}")
    String doc_api;

    @RequestMapping
    public String office(ModelMap map, String url, String filename) throws UnknownHostException {
        String userAddress = InetAddress.getLocalHost().getHostAddress();
        map.put("key", GenerateRevisionId(userAddress + "/" + filename));

        map.put("url", domain + url);

        map.put("filename", filename);

        map.put("fileType", FileUtils.getExtension(filename).replace(".", ""));

        map.put("doc_api", doc_api);

        map.put("documentType", FileUtils.GetFileType(filename).toString().toLowerCase());

        return "office";
    }

    private static String GenerateRevisionId(String expectedKey) {
        if (expectedKey.length() > 20)
            expectedKey = Integer.toString(expectedKey.hashCode());

        String key = expectedKey.replace("[^0-9-.a-zA-Z_=]", "_");

        return key.substring(0, Math.min(key.length(), 20));
    }
}
