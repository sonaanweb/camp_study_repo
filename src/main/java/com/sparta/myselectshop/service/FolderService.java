package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    // 폴더 생성
    public void addFolders(List<String> folderNames, User user) {

        // 폴더 중복 생성 방지 -> 입력 들어온 폴더 이름을 기준으로 유효성 확인
        List<Folder> existingFolders = folderRepository.findAllByUserAndNameIn(user, folderNames);

        // 여러개 받아옴
        List<Folder> folderList = new ArrayList<>();

        // 중복이 아니면 생성
        for (String folderName : folderNames) {
            if (!isExistFolderName(folderName, existingFolders)){
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            } else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다.");
            }
        }

        folderRepository.saveAll(folderList);

    }


    // 로그인한 회원이 등록된 모든 폴더 조회
    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;
    }


    // 폴더 중복 확인 메서드
    private boolean isExistFolderName(String folderName, List<Folder> existingFolders) {
        for (Folder existFolder : existingFolders) {
            if(folderName.equals(existFolder.getName())){
                return true; // 받아온 이름이 존재하는 폴더와 같으면 true 리턴
            }
        }
        return false; // 일치하는 이름이 없다면 false
    }
}
