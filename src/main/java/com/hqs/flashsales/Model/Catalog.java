package com.hqs.flashsales.Model;

import lombok.Data;

/**
 * @author huangqingshi
 * @Date 2019-01-23
 */
@Data
public class Catalog {
    private Long id;
    private String name;
    private Long total;
    private Long sold;
    private Long version;
}
