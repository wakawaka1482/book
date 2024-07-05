package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dao.CollectRecord;
import com.example.dto.PaginationRequest;
import com.example.dto.CollectRecordDTO;

import java.util.List;

public interface CollectRecordService extends IService<CollectRecord> {
    IPage<CollectRecordDTO> findPageByUser(PaginationRequest paginationRequest);

    boolean returnBook(Integer collectrecordid);

    boolean addCollectRecord(CollectRecord collectRecord);

    IPage<CollectRecordDTO> findPageByAdmin(PaginationRequest paginationRequest);
}
