package mx.org.inai.util.usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Snippet {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder4 = new BCryptPasswordEncoder(4);

		System.out.println();
	    System.out.println("usuario: flores, password:flores, encrypted password = " + encoder4.encode("flujosAdmin1")); //la contraseña del usuario que se usa es esta	  
	    
	}
}
