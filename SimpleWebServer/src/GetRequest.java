


public class GetRequest extends Request {
	
	String resourcePath;

	public GetRequest(String firstHeader) {
		this.resourcePath = firstHeader.split(" ")[1];
		
		if (ForeignFiles.frenchFiles.contains(this.resourcePath)){
			this.resourcePath = appendFr(this.resourcePath);
			System.out.println(this.resourcePath);
		} else if (ForeignFiles.germanFiles.contains(this.resourcePath)){
			//
		} else if (ForeignFiles.spanishFiles.contains(this.resourcePath)){
			//
		}
	}
	
	public String appendFr(String s){
		return s.replace(".html", "_fr.html");
	}
	
	public String appendDe(String s){
		return null;
	}
	
	public String appendEs(String s){
		return null;
	}

	@Override
	public String getFullHeader() throws AccessDeniedException {
		if(resourcePath.contains(".htaccess"))
			throw new AccessDeniedException();
		else
			return "GET " + resourcePath + " HTTP/1.1" + Constants.NEWLINE + super.getCommonHeader();
	}
	
}
