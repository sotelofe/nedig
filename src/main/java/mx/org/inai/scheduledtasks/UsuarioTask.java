package mx.org.inai.scheduledtasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class UsuarioTask {

	//private static final Logger log = LoggerFactory.getLogger(UsuarioTask.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	//@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		//log.info("The time is now {}", dateFormat.format(new Date()));
		//System.out.println( "The time is now {}"+ dateFormat.format(new Date()));
	}
}
