package ch.jdc.lab_spring_machinestate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class LabSpringMachinestateApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LabSpringMachinestateApplication.class, args);
	}

	private final StateMachine<States, Events> stateMachine;

	@Autowired
	public LabSpringMachinestateApplication(StateMachine<States, Events> stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void run(String... args) throws Exception {
		Message<Events> e1 = MessageBuilder.withPayload(Events.E1).build();
		Message<Events> e2 = MessageBuilder.withPayload(Events.E2).build();
		stateMachine.sendEvent(Mono.just(e1)).subscribe();
		stateMachine.sendEvent(Mono.just(e2)).subscribe();
		System.out.println("Current state: " + stateMachine.getState().getId());
	}
}
