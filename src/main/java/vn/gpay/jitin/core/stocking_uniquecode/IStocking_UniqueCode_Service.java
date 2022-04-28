package vn.gpay.jitin.core.stocking_uniquecode;

import vn.gpay.jitin.core.base.Operations;

public interface IStocking_UniqueCode_Service extends Operations<Stocking_UniqueCode> {
	public Stocking_UniqueCode getby_type(Integer type);
}
