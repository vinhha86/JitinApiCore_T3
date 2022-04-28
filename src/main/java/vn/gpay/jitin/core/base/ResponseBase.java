package vn.gpay.jitin.core.base;

public class ResponseBase {
	public static final int RESPCODE_NOERROR 		= 0;
	public static final int RESPCODE_UNKNOWN_ERROR 	= 9999;
	private int respcode;
	private String message;
	public ResponseBase() {
		this.respcode=RESPCODE_NOERROR;
		this.message="";
	}
	public int getRespcode() {
		return respcode;
	}
	public void setRespcode(int respcode) {
		this.respcode = respcode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static int getRespcodeNoerror() {
		return RESPCODE_NOERROR;
	}
	public static int getRespcodeUnknownError() {
		return RESPCODE_UNKNOWN_ERROR;
	}
}
