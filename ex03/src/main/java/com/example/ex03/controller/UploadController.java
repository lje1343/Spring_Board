package com.example.ex03.controller;

/* 문제점 및 해결방안
*  1. 동일한 이름으로 파일이 업로드 되었을 때 기존 파일이 사라지는 문제
*  2. 이미지 파일의 경우 원본 파일의 용량이 클 때 썸네일 이미지로 생성해야 하는 문제
*  3. 이미지 파일과 일반 파일을 구분해서 다운로드 혹은 페이지에서 조회할 수 있도록 처리해야 하는 문제
*  4. 첨부파일 공격에 대비하기 위한 업로드 파일의 확장자 제한
*  */

import com.example.ex03.domain.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/upload/*")
public class UploadController {

    @GetMapping("/uploadForm")
    public void uploadForm() {log.info("upload form");}

    @PostMapping("/uploadFormAction")
    public void upload(MultipartFile[] uploadFile) throws IOException {
        String uploadFolder = "C:/upload";

        for(MultipartFile file : uploadFile){
            log.info("-----------------------------------");
            log.info("Upload File Name: " + file.getOriginalFilename());
            log.info("Upload File Size: " + file.getSize());

            File saveFile = new File(uploadFolder, file.getOriginalFilename());
            file.transferTo(saveFile);

        }
    }

    @GetMapping("/uploadAjax")
    public void uploadAjax(){log.info("upload Ajax");}

    @PostMapping("/uploadAjaxAction")
    @ResponseBody
    public List<FileVO> uploadAjaxAction(MultipartFile[] uploadFile) throws IOException {
        String uploadFolder = "C:/upload";
        ArrayList<FileVO> files = new ArrayList<>();

        // yyyy/MM/dd 경로 만들기
        File uploadPath = new File(uploadFolder, getFolder());
        if(!uploadPath.exists()){uploadPath.mkdirs();} /* 경로가 존재하지 않으면 직접 만들어줌 */

        for(MultipartFile file : uploadFile) {
            FileVO fileVO = new FileVO(); /* 전의 데이터가 꽃힐까봐 for문안에서 새로 객체 생성해줌 */
            String uploadFileName = file.getOriginalFilename();

            // UUID
            // 네트워크 상에서 각각의 객체들을 식별하기 위하여 사용되었다.
            // 중복될 가능성이 거의 없다고 인정되기 때문에 많이 사용된다.

            // UUID 개수
            // 340간 2823구 6692양 938자 4634해 6337경...

            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;
            fileVO.setFileName(uploadFileName); /* uuid 네임을 db에 저장 */
            fileVO.setUuid(uuid.toString());
            fileVO.setUploadPath(getFolder());

            log.info("-----------------------------------");
            log.info("Upload File Name: " + uploadFileName);
            log.info("Upload File Size: " + file.getSize());

            File saveFile = new File(uploadPath, uploadFileName);
            file.transferTo(saveFile);

            // 파일이 이미지인지 아닌지 (썸네일을 만들기 위해서)
            if(checkImageType(saveFile)){
                /* 이미지 파일 전달 */
                FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
                /* getInputStream -> 해당 파일을 읽어와주는 메서드, 100 -> 높이 너비 */
                Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
                thumbnail.close();
                fileVO.setImage(true);
            }
            files.add(fileVO); /* 리스트에 계속 담아준다. */
        }
        return files; /* 리스트 리턴 */
    }

    @GetMapping("/display") /* 해당 파일을 byte 배열로 복사해서 보내줌 */
    @ResponseBody
    public byte[] getFile(String fileName) throws IOException{
        File file = new File("C:/upload/", fileName);
        return FileCopyUtils.copyToByteArray(file);
    }

    private boolean checkImageType(File file) throws IOException{
        String contentType = Files.probeContentType(file.toPath()); /* probe -> png,jpg.. */
        return contentType.startsWith("image"); /* image로 시작하는지 */
    }

    private String getFolder(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return sdf.format(date);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(String fileName) throws UnsupportedEncodingException {
        Resource resource =  new FileSystemResource("C:/upload/" + fileName);
        HttpHeaders header = new HttpHeaders();
        String name = resource.getFilename();
        name = name.substring(name.indexOf("_") + 1);
        header.add("Content-Disposition","attachment;filename=" + new String(name.getBytes("UTF-8"), "ISO-8859-1"));
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(String fileName){
        File file = new File("C:/upload/", fileName);
        if(file.exists()){ file.delete(); } /* 썸넬부터 삭제 */

        file = new File("C:/upload/", fileName.replace("s_", ""));
        if(file.exists()){ file.delete(); } /* 원본이미지 삭제 -> s_ 없는것 */
    }
}
