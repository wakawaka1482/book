package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LendRecord {
    @TableId
    private Integer lendid;
    private Integer bookid;
    private Integer userid;
    private LocalDateTime lendtime;
    private LocalDateTime backtime;
}
