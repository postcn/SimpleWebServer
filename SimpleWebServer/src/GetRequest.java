


public class GetRequest extends Request {
	
	String resourcePath;

	public GetRequest(String firstHeader) {
		this.resourcePath = firstHeader.split(" ")[1];
		System.out.println(this.resourcePath + "\n");
	}

	@Override
	public String getFullHeader() throws AccessDeniedException {
		if(resourcePath.contains(".htaccess"))
			throw new AccessDeniedException();
		else
			return "GET " + resourcePath + " HTTP/1.1" + Constants.NEWLINE + super.getCommonHeader();
	}
	
}
