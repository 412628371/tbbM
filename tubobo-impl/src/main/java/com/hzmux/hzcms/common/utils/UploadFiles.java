package com.hzmux.hzcms.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件工具类
 * @author young
 * @2016年1月14日
 */
public class UploadFiles {
	
	/**
	 * 文件上传
	 * @param files
	 * @param filePath
	 * @param rename ('' means not change filename)
	 * @param requiredExtension extension requaired (".jpg/.png...." split with '/' ,lowerCase)
	 * @param fileSize size limit (unit:M)
	 * @return
	 */
	public static String uploadFiles(List<MultipartFile> files,String filePath ,String rename ,String extensionReq,int fileSize){
		boolean flg = false;
        StringBuilder uploadMsg = new StringBuilder();
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i) == null || files.get(i).equals("")) {
                    uploadMsg.append("上传文件不能为空！");
                    break;
                } else {
                    flg = true;
                }
            }
        } else {
            uploadMsg.append("上传文件不能为空！");
        }
        if (flg) {
            String fileName = "";
            String[] extensionReqArr = extensionReq.split("/");
            // 把得到的文件的集合通过循环的方式读取处理
            for (int i = 0; i < files.size(); i++) {
                // 获取上载文件的文件名称
                fileName = files.get(i).getOriginalFilename();
                // 获取上载文件的扩展名
                String fileNameExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                // 文件格式CHECK
                boolean extensionFlag = false;
                if (extensionReqArr.length < 1) {
                	extensionFlag = true;
				}else {
					for (String extension : extensionReqArr) {
						extension.equals(fileNameExtension);
						extensionFlag = true;
						break;
					}
				}
                if (extensionFlag) {
                    if (fileName != null) {
                        // 把上传的文件写到指定的路径中
                        File filepath = new File(filePath);
                        // 如果指定的路径没有就创建
                        if (!filepath.exists()) {
                            filepath.mkdirs();
                        }
                        Long size = files.get(i).getSize();
                        // 文件大小不能大于500K
                        if (size < 1024 * 1024 * fileSize) {
                        	// 上传图片到服务器
                            MultipartFile file = files.get(i);
                            try {
								file.transferTo(new File(filepath + File.separator,
										StringUtils.isNotBlank(rename) ? rename + fileNameExtension : fileName));
							} catch (IllegalStateException | IOException e) {
								uploadMsg.append(fileName + " 上传失败！   \n");
								e.printStackTrace();
							}
                            uploadMsg.append(fileName + " 上传成功！   \n");
                        }else {
                            uploadMsg.append(fileName + "文件大于"+ fileSize +"M，请重新上传 ！\n");
                        }
                    }
                }else {
                    uploadMsg.append(fileName + "格式不正确，请重新上传！\n");
                    break;
                }
            }
        }
        return uploadMsg.toString();
	}
}

