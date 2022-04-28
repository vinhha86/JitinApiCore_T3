package vn.gpay.jitin.core.customer;


import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface ICustomerService extends Operations<Customer>{

	public List<Customer> findbycode(String customercode);
}
