package com.hqs.flashsales.Model;

import lombok.Data;

import java.util.Date;

/**
 * @author huangqingshi
 * @Date 2019-01-23
 */
@Data
public class SalesOrder {
    private Long id;
    private Long cid;
    private String name;
    private Date createTime;

}
