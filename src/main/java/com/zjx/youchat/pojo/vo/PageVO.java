package com.zjx.youchat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页数据封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> implements Serializable {
    // 总记录数
    private Integer totalSize;

    // 每页记录数
    private Integer pageSize;

    // 总页数
    private Integer totalPage;

    // 当前页码
    private Integer pageNum;

    // 分页数据
    private List<T> list;
}
