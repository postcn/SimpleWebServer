import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public
class EchoClient {
    public
            static
    void
            main(String[] arstring) {
        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 8001);

            InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            bufferedwriter.write("GET /HM1.jpg HTTP/1.1\nUser-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36\nHost: localhost:8001\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\nConnection: keep-alive\n\n");
            bufferedwriter.flush();
            
            String string = null;
            while ((string = bufferedreader.readLine()) != null) {
                System.out.println(string);
                
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}