package com.retailstore.dao;

import com.retailstore.dto.ProductBillDto;

/**
 *  Created by wangh  on 10-07-2016.
 */
public interface ICheckOutDao {

    void saveProductBill(ProductBillDto productBillDto);
}
