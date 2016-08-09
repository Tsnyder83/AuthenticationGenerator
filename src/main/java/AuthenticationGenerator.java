import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by TSnyder on 8/3/2016.
 */
public class AuthenticationGenerator {
    private JButton btnExecute;
    private JTextField txtConsumerKey;
    private JTextField txtLoginID;
    private JTextField txtPassword;
    private JLabel lblPassword;
    private JLabel lblLoginID;
    private JLabel lblConsumerkey;
    private JTextPane txtPanelResults;
    private JPanel mainPanel;
    private JLabel txtCreatedBy;
    private JTabbedPane tabbedPane1;

    String loginID;
    String password;
    String basicOAuth;
    String auth;
    String auth64;
    String xConsumerKey;

    public static JSONParser jsonParser = new JSONParser();
    public static CloseableHttpClient client = HttpClientBuilder.create().build();


    public static void main(String[] args) {
        JFrame frame = new JFrame("AuthenticationGenerator");
        frame.setContentPane(new AuthenticationGenerator().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,485);
        frame.setVisible(true);
    }

    public AuthenticationGenerator() {
        btnExecute.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)  {
                txtPanelResults.setAutoscrolls(true);

                try {

                    generateToken();

                } catch (IndexOutOfBoundsException i) {
                    txtPanelResults.setText("IndexOutOfBoundsException: " + i.getMessage());
                } catch (IOException o) {
                    txtPanelResults.setText("Caught IOException: " + o.getMessage());
                }
                catch (ParseException p) {
                    txtPanelResults.setText("Caught ParseException: " + p.getMessage());
                }
                catch (URISyntaxException u) {
                    txtPanelResults.setText("Caught URISyntaxException: " + u.getMessage());
                }
            }
        });
    }


    public void generateToken() throws URISyntaxException, IOException, ParseException
    {
        loginID = txtLoginID.getText();
        password = txtPassword.getText();
        auth = loginID + ":" + password;
        auth64 = Base64.encodeBase64String(auth.getBytes());
        xConsumerKey = txtConsumerKey.getText();
        basicOAuth = "Basic " + auth64;


        // Make API Call to Get User OAuth Data
        URIBuilder url = new URIBuilder("https://www.concursolutions.com/net2/oauth2/accesstoken.ashx");
        HttpGet request = new HttpGet(url.toString());
        request.addHeader("accept", "application/json");
        request.addHeader("Authorization", basicOAuth);
        request.addHeader("X-ConsumerKey", xConsumerKey);

        //Assign JSON String Response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = client.execute(request, responseHandler);

        //System.out.println(responseBody);

        // Create an Object from the parsed API response
        JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody.toString());

        // Create the structure of the API "Access_Token" API Response variables
        //JSONObject structure = (JSONObject) jsonObject.get("Access_Token");

        //System.out.println(responseBody);

        txtPanelResults.setText(responseBody);
    }


















}
