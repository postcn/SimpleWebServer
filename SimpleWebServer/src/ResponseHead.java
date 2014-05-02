
public class ResponseHead extends ResponseGet {

	public ResponseHead(HeadRequest r) {
		super(r);
	}
	
	@Override
	public byte[] getResponse() {
		String s = "HTTP/1.1 200 OK" + Constants.NEWLINE;
		s += super.getCommonHeader();
		byte[] header = s.getBytes();
		return header;
	}

}
