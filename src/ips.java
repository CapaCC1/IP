import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.naming.InterruptedNamingException;



public class ips {
	
	private static String getIPprivada() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		return ip.getHostAddress();
	}
	
	private static String getIPprivada2() throws SocketException {
		String resultado = "";
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while(interfaces.hasMoreElements()) {
			NetworkInterface iface = interfaces.nextElement();
			
			//No queremos interfaces de bucle de retorno o inactivas
			if(iface.isLoopback()|| !iface.isUp()) {
				continue;
			}
			
			Enumeration<InetAddress> direcciones = iface.getInetAddresses();
			while(direcciones.hasMoreElements()) {
				InetAddress addr = direcciones.nextElement();
				
				//Verificamos si es una direccion IP valida y no es una direccion IPv6 local
				if(addr.isLoopbackAddress() && !addr.getAddress().toString().contains(":")) {
					resultado = addr.getHostAddress();
				
				}
			}
		}
		return resultado;
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
            InetAddress dir = InetAddress.getByName("google.es");
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
		List<Integer> elementosAniadir = Arrays.asList(22,80,443);
		puertosComunes.addAll(elementosAniadir);
		
		ArrayList<Integer>puertosAbiertos = new ArrayList<Integer>();
		String host = getIPpublica();
 
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
	
	private static void escanearPuertosNmap() throws IOException {
		String ip = getIPpublica();
		String comando = "nmap -p 22" + ip;
		String linea;
		try {
			Process proceso = Runtime.getRuntime().exec(comando);
			BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
			
			while((linea = reader.readLine()) != null) {
				System.out.println(linea);
			}
			proceso.waitFor();
		}catch(InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
    	
    	
    	try {
    		ArrayList<Integer>puertosAbiertos = escanerPuertos();
    		
    		if(comprobarConexion()) {
    			System.out.println("Conectado a Internet.\n");
    			System.out.println("IP Privada: " + getIPprivada2());
    			System.out.println("IP Publica: " + getIPpublica());
    			
    			if(puertosAbiertos.isEmpty()) {
    				System.out.println("\nNo se encontraron puertos abiertos. ");
    			}else {
    				System.out.println("\nPuertos Abiertos: " + puertosAbiertos);
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
