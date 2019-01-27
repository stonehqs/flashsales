package com.hqs.flashsales.Mapper;

import com.hqs.flashsales.Model.SalesOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author huangqingshi
 * @Date 2019-01-23
 */
@Mapper
public interface SalesOrderMapper {

    @Insert("insert sales_order (cid, name) values (#{cid}, #{name})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertSalesOrder(SalesOrder salesOrder);

    @Update("update sales_order set cid=#{cid}, name=#{name} where id=#{id}")
    Long updateSalesOrder(SalesOrder salesOrder);

    @Select("select * from sales_order where id=#{id}")
    SalesOrder selectSalesOrder(@Param("id") Long id);

    @Delete("Delete from sales_order where id=#{id}")
    Long deleteSalesOrder(@Param("id") Long id);

    @Select("select * from sales_order")
    List<SalesOrder> selectAllSalesOrder();
}
