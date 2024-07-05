package com.example.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectRecord {
    @TableId
    private Integer collectrecordid;
    private Integer bookid;
    private Integer userid;
    private LocalDateTime lenddate;
}