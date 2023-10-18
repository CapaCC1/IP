import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



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
	
	private static ArrayList<Integer> escanerPuertos() throws IOException{
		
		ArrayList<Integer> puertosComunes = new ArrayList<Integer>();
		List<Integer> elementosAñadir = Arrays.asList(80,443,21,22,110,995,143,993,26,25,2525,587,3306,2082,2083,2086,2087,2095,2096,2077,2078);
		puertosComunes.addAll(elementosAñadir);
		
		ArrayList<Integer>puertosAbiertos = new ArrayList<Integer>();
		String host = " localhost";
 
        for (Integer port : puertosComunes) {
        	
            try {
                Socket socket = new Socket(host, port);
                puertosAbiertos.add(port);
                socket.close();
            } catch (IOException e) {
               
            }
        }
        return puertosAbiertos;
	}
	
    public static void main(String[] args) {
    	
    	
    	try {
    		ArrayList<Integer>puertosAbiertos = escanerPuertos();
    		
    		if(comprobarConexion()) {
    			System.out.println("Conectado a Internet.\n");
    			System.out.println("IP Privada: " + getIPprivada());
    			System.out.println("IP Publica: " + getIPpublica());
    			if(puertosAbiertos.isEmpty()) {
    				System.out.println("\nNo se encontraron puertos abiertos. ");
    			}else {
    				System.out.println("Puertos Abiertos: " + puertosAbiertos);
    			}
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
