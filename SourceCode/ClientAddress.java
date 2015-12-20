import java.io.*;
import java.net.*;
class ClientAddress implements Serializable
{
	String Username,IP;
	int port;
	ClientAddress(String name,String IPhost,int port_no)
	{	
		Username=name;
		IP=IPhost;
		port=port_no;
	}
	public boolean equals(ClientAddress ca)
	{
		if(this.Username.equals(ca.Username)) return true;
		else return false;
	}
}
		
		