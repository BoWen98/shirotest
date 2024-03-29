package com.zhao.shirotest.controller;

import com.zhao.shirotest.model.Blog;
import com.zhao.shirotest.repository.BlogRepository;
import com.zhao.shirotest.util.QiniuUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class HomeController {

    private static String UPLOADED_FOLDER = "D:/QQ/";
    @Autowired
    BlogRepository blogRepository;

    @RequestMapping("edit")
    public String editor() {
        return "edit";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("submit")
    @ResponseBody
    public void submit(Blog blog) {
        System.out.println(blog.getContent());
        System.out.println(blog.getHtmlContent());
        blogRepository.save(blog);
    }

    @RequestMapping("wxcontent")
    @ResponseBody
    public Blog wxcontent() {
        Blog blog = blogRepository.findBlogById(2l);
        System.out.println(blog.getHtmlContent());
        return blog;
    }

    //处理文件上传
    @RequestMapping(value = "/uploadimg")
    @ResponseBody
    public Map<String, Object> demo(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        System.out.println(request.getContextPath());
        String realPath = UPLOADED_FOLDER;
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
/*        File targetFile = new File(realPath, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }*/
        //保存
        try {
            /*            file.transferTo(targetFile);*/
            //byte[] bytes = file.getBytes();
            //七牛云
            String upload = new QiniuUploadUtil().upload(UUID.randomUUID().toString() + fileName, file.getBytes());
            //本地
            /*Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);*/
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url", upload);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }
        System.out.println(resultMap.get("success"));
        return resultMap;


    }
}
