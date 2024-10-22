package ch.jdc.lab_spring_machinestate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.SI, initAction())
                .state(States.SI, entryAction())
                .state(States.S1, entryAction(), exitAction())
                .state(States.S2, entryAction(), exitAction());

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.SI).target(States.S1).event(Events.E1).action(transitionWithExceptionAction(), handleError())
                .and()
                .withExternal()
                .source(States.S1).target(States.S2).event(Events.E2).action(transitionAction());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
//                System.out.println("State change to " + to.getId());
            }
        };
    }

    @Bean
    public Action<States, Events> entryAction() {
        return ctx -> {
            System.out.println(ctx.getTarget().getId()+": entryAction called");
        };
    }

    @Bean
    public Action<States, Events> exitAction() {
        return ctx -> {
            System.out.println(ctx.getSource().getId()+": exitAction called");
        };
    }
    @Bean
    public Action<States, Events> transitionWithExceptionAction() {
        return ctx -> {
            System.out.println(ctx.getSource().getId()+": actionThrowException called");
            throw new RuntimeException(ctx.getSource().getId()+": Something bad happened");
        };
    }

    @Bean
    public Action<States, Events> handleError() {
        return ctx -> {
            Exception e = ctx.getException();
            System.err.println(ctx.getSource().getId()+": "+ e.getMessage());
        };
    }

    @Bean
    public Action<States, Events> transitionAction() {
        return ctx -> {
            System.out.println("transitionAction called from: " + ctx.getSource().getId() + " to: " + ctx.getTarget().getId());
        };
    }

    @Bean
    public Action<States, Events> initAction() {
        return ctx -> {
            System.out.println("initAction called");
        };
    }
}