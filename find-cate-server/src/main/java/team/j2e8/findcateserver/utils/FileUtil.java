package team.j2e8.findcateserver.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class FileUtil {
    public String FileUtil(MultipartFile multipartFile,String path) throws IOException {
        String completePath = this.getClass().getClassLoader().getResource("static").toString();
        completePath = completePath.substring(completePath.indexOf("/")) + path;
        //创建文件夹在服务器端的存放路径
        File pictureFile = new File(completePath);
        if (!pictureFile.exists()) {
            pictureFile.mkdir();
        }
        //生成文件在服务器的存储名字
        String fileSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileSuffix;
        File files = new File(pictureFile + "/" + fileName);
        //上传
        multipartFile.transferTo(files);
        return fileName;
    }

    public void getPictureFile(String path, String contentType, String pictureName, HttpServletResponse response) throws IOException{
        String completePath = this.getClass().getClassLoader().getResource("static").toString();
        completePath = completePath.substring(completePath.indexOf("/")) + path;
        response.setContentType(contentType);
        FileInputStream fp = new FileInputStream(completePath + pictureName);
        if (fp != null) {
            int i = fp.available(); // 得到文件大小
            byte data[] = new byte[i]; // data是字节
            fp.read(data); // 从输入流里，把图片读到data变量里。
            fp.close(); // 输入流读完要关闭，否则内存会一直占用
            response.setContentType(contentType);  // 设置返回的文件类型
            OutputStream toClient = response.getOutputStream(); // toClient就是response的输出流，就是响应的意思
            toClient.write(data); // 把data写到toClient
            toClient.close();
        }
    }
}
