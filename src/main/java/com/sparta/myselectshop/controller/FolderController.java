package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    /**
     * 회원별 폴더를 추가할 수 있습니다.
     * 폴더를 추가할 때 1~N개를 한번에 추가할 수 있습니다.
     * 회원별 -> 저장한 폴더 조회
     */
    // 폴더 생성
    @PostMapping("/folders")
    public void addFolders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody FolderRequestDto folderRequestDto){

        List<String> folderNames = folderRequestDto.getFolderNames(); // 반환 값 folderNames라는 이름으로
        folderService.addFolders(folderNames, userDetails.getUser());
    }
    
    
    
    // 폴더 조회
}
