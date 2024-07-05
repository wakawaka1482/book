package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CollectRecordDTO {

    private Integer collectrecordid;
    private String image;
    private String bookname;
    private String bookuser;
    private LocalDateTime lendtime;

    public CollectRecordDTO(Integer collectrecordid, String image, String bookname, String bookuser, LocalDateTime lendtime) {
        this.collectrecordid = collectrecordid;
        this.image = image;
        this.bookname = bookname;
        this.bookuser = bookuser;
        this.lendtime = lendtime;


    }
}
