package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class LendRecordDTO {
    private Integer lendid;
    private String bookname;
    private String username;
    private LocalDateTime lendtime;
    private LocalDateTime backtime;

    public LendRecordDTO(Integer lendid,String bookname, String username, LocalDateTime lendtime, LocalDateTime backtime) {
        this.lendid = lendid;
        this.bookname = bookname;
        this.username = username;
        this.lendtime = lendtime;
        this.backtime = backtime;
    }

    @Override
    public String toString() {
        return "LendRecordDTO{" + "bookname='" + bookname + '\'' + ", username='" + username + '\'' + ", lendtime=" + lendtime + ", backtime=" + backtime + '}';
    }
}
