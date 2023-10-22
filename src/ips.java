import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.InterruptedNamingException;



public class ips {
	
	private static String getIPprivada() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		return ip.getHostAddress();
	}
	
	private static String getNombreRed() throws UnknownHostException {
		InetAddress nombreRed = InetAddress.getLocalHost();
		return nombreRed.getCanonicalHostName();
	}
	
	private static String getIPprivada2() throws SocketException {
		String resultado = "";
		
		//Obtiene una enumeracion de todas las interfaces de red disponibles
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while(interfaces.hasMoreElements()) {
			NetworkInterface iface = interfaces.nextElement();
			
			//No queremos interfaces de bucle de retorno o inactivas
			if(iface.isLoopback()|| !iface.isUp()) {
				continue; //Salta a la siguiente interfaz si es de bucle de retorno o inacitva
			}
			
			Enumeration<InetAddress> direcciones = iface.getInetAddresses();
			while(direcciones.hasMoreElements()) {
				InetAddress addr = direcciones.nextElement();
				
				//Verificamos si es una direccion IP valida y no es una direccion IPv6 local
				if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
	                return addr.getHostAddress();
	            }
			}
		}
		resultado = "IP Privada no encontrada.";
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
	
	

	    public static ArrayList<Integer> escanerPuertosMH() throws IOException {
	        ArrayList<Integer> puertosComunes = new ArrayList<Integer>();
	        List<Integer> elementosAniadir = Arrays.asList(
	        		20,
	        	    21, 
	        	    22, 
	        	    23,
	        	    25,
	        	    53,
	        	    80,
	        	    110,
	        	    143,
	        	    443,
	        	    465,
	        	    587,
	        	    993,
	        	    995,
	        	    3306,
	        	    3389);
	        puertosComunes.addAll(elementosAniadir);

	        ArrayList<Integer> puertosAbiertos = new ArrayList<Integer>();
	        String host = getIPpublica();

	        for (Integer port : puertosComunes) {
	            try {
	                // Crea un socket y establece un tiempo de espera
	                Socket socket = new Socket();
	                socket.connect(new InetSocketAddress(host, port), 10); // 1000 ms (1 segundo) de tiempo de espera
	                socket.close();
	                puertosAbiertos.add(port);
	            } catch (IOException e) {
	                // El puerto no pudo ser alcanzado dentro del tiempo de espera
	            }
	        }

	        return puertosAbiertos;
	    }
	
	    
	    private static String getPuertaEnlace() throws IOException {
	    	Process resultado = Runtime.getRuntime().exec("traceroute -m 1 www.amazon.com");
	    	
	    	BufferedReader output = new BufferedReader(new InputStreamReader(resultado.getInputStream()));
	        String thisLine = output.readLine();
	        StringTokenizer st = new StringTokenizer(thisLine);
	        st.nextToken();
	        st.nextToken();
	        st.nextToken();
	        st.nextToken();
	        st.nextToken();
	        st.nextToken();
	        st.nextToken();
	        String gateway = st.nextToken();
	        return gateway;
	    }
	    
    public static void main(String[] args) {
    	
    	
    	try {
    		ArrayList<Integer>puertosAbiertos = escanerPuertosMH();
    		
    		if(comprobarConexion()) {
    			System.out.println("Conectado a Internet.\n");
    			System.out.println("Nombre: " + getNombreRed());
    			System.out.println("IP Privada: " + getIPprivada2());
    			System.out.println("IP Publica: " + getIPpublica());
    			System.out.println("Puerta de Enlace: " + getPuertaEnlace());
    			
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
			System.out.println("ERROR");
		}catch(MalformedURLException e) {
			System.out.println("ERROR");
		} catch (IOException e) {
			System.out.println("ERROR");
		}

    }
    
}
