import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;



public class ips {
	
	private static String getIPprivada() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		return ip.getHostAddress();
	}
	
	private static String getIPpublica() throws IOException {
		String error = "No Disponible sin Conexion!";
		String resultado = "";
		if(comprobarConexion()) {
			URL url = new URL("https://api.ipify.org");//Objeto URL con URL de la api
			HttpURLConnection conx = (HttpURLConnection)url.openConnection();//Conexion a URL especificada
			conx.setRequestMethod("GET");//Indica que se quiere obtener informacion
			BufferedReader reader = new BufferedReader(new InputStreamReader(conx.getInputStream()));//Lector para respuesta de la API
			resultado = reader.readLine();
			reader.close();
			return resultado;
		}else {
			return error;
		}
	}
	
	private static boolean comprobarConexion() throws IOException {
        try {
            InetAddress dir = InetAddress.getByName("google.com");
            if (dir.isReachable(5000)) {
                return true;
            } else {
                return false;
            }
        } catch (UnknownHostException e) {
            return false;
        }
    }
	
    public static void main(String[] args) {
    	
    	try {
    		
    		if(comprobarConexion()) {
    			System.out.println("Conectado a Internet.\n");
    			System.out.println("IP Privada: " + getIPprivada());
    			System.out.println("IP Publica: " + getIPpublica());
    		}else {
    			System.out.println("Sin Conexion a Internet!\n");
    			System.out.println("IP Privada: " + getIPprivada());
    			System.out.println("IP Publica: " + getIPpublica());
    		}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}catch(MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
    
}
