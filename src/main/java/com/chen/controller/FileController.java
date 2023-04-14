package com.chen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.chen.base.ByteFileType;
import com.chen.base.Render;
import com.chen.base.UserKit;
import com.chen.bean.Result;
import com.chen.bean.User;
import com.chen.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author @Chenxc
 * @Date 2023/3/31 16:20
 */
@Controller
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService service;


    @GetMapping("/findAll")
    public String list(ModelMap map){
        Integer userId = UserKit.getUser().getId();
        List<com.chen.bean.File> files = service.list(userId);
        map.addAttribute("files",files);
        return "file/index.html";
    }



    @GetMapping("/upload")
    public String upload(){
         return "file/upload.html";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request,@RequestParam("upload") MultipartFile file){
        //当前块
        Integer schunk = null;
        //总的块数
        Integer schunks = null;
        //文件名
        String fileName = null;
        //输出流
        BufferedOutputStream bos = null;

        try{
            String path = ResourceUtils.getURL("classpath:").getPath();//FileController.class.getResource("/").getPath()
            //临时文件保存路径
            String uploadTempPath = path+File.separator+"tempFile";
            //结果文件保存路径
            String uploadPath = path+File.separator+"upload";

            if(file != null && request.getParameter("chunks") == null){
                fileName = file.getOriginalFilename();
                //重命名
                String simpleUUID = IdUtil.simpleUUID();
                String ext = fileName.substring(fileName.lastIndexOf("."));
                File resultFile = new File(uploadPath,simpleUUID+ext);
                file.transferTo(resultFile);
                boolean b = service.saveFile(file, resultFile,UserKit.getUser().getId(), uploadPath,simpleUUID+ext);
                return "上传成功："+ fileName;
            }

            schunk = Integer.parseInt(request.getParameter("chunk"));
            schunks = Integer.parseInt(request.getParameter("chunks"));
            fileName = request.getParameter("name");

            if(file != null){
                if(fileName != null){
                    if(schunk != null){
                        String tempFileName = schunk+"_"+fileName;
                        //写入临时文件
                        File chunkFile = new File(uploadTempPath,tempFileName);
                        if(!chunkFile.exists()){
                            file.transferTo(chunkFile);
                        }
                    }
                }
            }
            //文件合并
            if(schunk != null && schunk.intValue() == schunks.intValue() - 1){
                //重命名
                String simpleUUID = IdUtil.simpleUUID();
                String ext = fileName.substring(fileName.lastIndexOf("."));
                File resultFile = new File(uploadPath,simpleUUID+ext);
                bos = new BufferedOutputStream(new FileOutputStream(resultFile));
                for (int i = 0; i < schunks; i++) {
                    File tempFile = new File(uploadTempPath,i+"_"+fileName);
                    while (!tempFile.exists()){
                        TimeUnit.MILLISECONDS.sleep(100);//因为是并发，不确定哪一块先上传玩
                    }
                    byte[] bytes = IoUtil.readBytes(new FileInputStream(tempFile));
                    bos.write(bytes);
                    bos.flush();
                    tempFile.delete();//删除临时文件
                }
                bos.flush();
                boolean b = service.saveFile(file, resultFile,UserKit.getUser().getId(), uploadPath,simpleUUID+ext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != bos){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "上传成功:" + fileName;
    }



    @RequestMapping("/checkFileOnline/{id}")
    public void checkFileOnline(HttpServletResponse response,@PathVariable("id") Long id) throws IOException {
        com.chen.bean.File file = service.getById(id);
        if(null == file){
            log.error("文件记录不存在:"+id);
            return;
        }
        File f = new File(file.getLocalPath());
        if(!f.exists()){
            log.error("文件不存在:"+id);
            return;
        }
        byte[] bytes = IoUtil.readBytes(new FileInputStream(f));
        String ext = file.getName().substring(file.getName().lastIndexOf("."));
        if(ext.equals(".txt")){
            Render.renderByteToTxt(bytes,response);
        }else if(ext.equals(".pdf")){
            Render.renderByteToPDF(bytes,response);
        } else if(ext.equals(".jpg")){
            Render.renderByteToImage(bytes,response, ByteFileType.JPG);
        }else if(ext.equals(".mp4")){
            Render.renderByteToAudio(bytes,response);
        }
    }


    @RequestMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        String path = FileController.class.getResource("/").getPath()+File.separator+"upload"+File.separator+"5bb282c7df4ef413e3831b4b3ea9f072.gif";
        System.out.println(path);
        // 读到流中
        InputStream inputStream = new FileInputStream(path);
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(path).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Long id){
        return service.deleteById(id);
    }

    @PostMapping("/checkFileExist")
    @ResponseBody
    public String checkFileExist(@RequestParam("md5") String md5){
        List<com.chen.bean.File> list = service.list(UserKit.getUser().getId());
        List<String> md5s = list.stream().map((file) -> {
            return file.getFileMd5();
        }).collect(Collectors.toList());
        if(md5s.contains(md5)){
            return "1";
        }
        md5s.add(md5);
        return "0";
    }
}
