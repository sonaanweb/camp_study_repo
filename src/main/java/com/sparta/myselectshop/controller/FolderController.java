package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.exception.RestApiException;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 관심 상품에 폴더 0개 ~ N개를 설정할 수 있습니다.
     * 관심상품이 등록되는 시점에는 어느 폴더에도 저장되지 않습니다
     * 관심상품 별로 기 생성 했던 폴더를 선택하여 추가할 수 있습니다.
     */
    // 폴더 조회 - 회원이 등록한 모든 폴더 조회
    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return folderService.getFolders(userDetails.getUser());
    }


    // 예외처리
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        System.out.println("FolderController.handleException");
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }
}
