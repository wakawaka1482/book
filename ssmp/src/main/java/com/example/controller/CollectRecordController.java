package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.CollectRecord;
import com.example.dto.PaginationRequest;
import com.example.dto.CollectRecordDTO;
import com.example.service.CollectRecordService;
import com.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/collect")
public class CollectRecordController {

    @Autowired
    private CollectRecordService collectRecordService;

    @PostMapping("/findPageUser")
    public R findPageUser(@RequestBody PaginationRequest paginationRequest) {
        try {
            IPage<CollectRecordDTO> page = collectRecordService.findPageByUser(paginationRequest);
            return new R(true, page);
        } catch (Exception e) {
            return new R(false, e.getMessage());
        }
    }
    @PostMapping("/findPageAll")
    public R findPageAdmin(@RequestBody PaginationRequest paginationRequest) {
        try {
            IPage<CollectRecordDTO> page=collectRecordService.findPageByAdmin(paginationRequest);
            return new R(true, page);
        }catch (Exception e){
            return new R(false, e.getMessage());
        }
    }


    @PostMapping("/delete")
    public R deleteCollect(@RequestBody Map<String, Integer> payload) {
        Integer collectrecordid = payload.get("collectrecordid");
        boolean result = collectRecordService.returnBook(collectrecordid);
        return new R(result, result ? "取消收藏成功" : "取消收藏失败");
    }

    @PostMapping("/add")
    public R addCollect(@RequestBody CollectRecord collectRecord) {
        try {
            boolean result = collectRecordService.addCollectRecord(collectRecord);
            return new R(result, result ? "收藏成功" : "收藏失败");
        } catch (Exception e) {
            return new R(false, e.getMessage());
        }
    }
}