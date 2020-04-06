package uk.gov.ons.census.fwmt.outcomeservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingReceiver;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OutcomePreprocessingQueueConfig {

  public static final String OUTCOME_PREPROCESSING_QUEUE = "Outcome.Preprocessing";
  public static final String OUTCOME_PREPROCESSING_EXCHANGE = "Outcome.Preprocessing.Exchange";
  public static final String OUTCOME_PREPROCESSING_ROUTING_KEY = "Outcome.Preprocessing.Request";
  public static final String OUTCOME_PREPROCESSING_DLQ = "Outcome.PreprocessingDLQ";

  @Autowired
  private AmqpAdmin amqpAdmin;

  // Queues
  @Bean
  @Qualifier("OS_Q")
  public Queue outcomePreprocessingQueue() {
    Queue queue = QueueBuilder.durable(OUTCOME_PREPROCESSING_QUEUE).withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", OUTCOME_PREPROCESSING_DLQ).withArgument("x-death", "")
        .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }

  // Dead Letter Queue
  @Bean
  @Qualifier("OS_DLQ")
  public Queue outcomePreprocessingDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(OUTCOME_PREPROCESSING_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }

  // Exchange
  @Bean
  @Qualifier("OS_E")
  public DirectExchange outcomePreprocessingExchange() {
    DirectExchange directExchange = new DirectExchange(OUTCOME_PREPROCESSING_EXCHANGE);
    directExchange.setAdminsThatShouldDeclare(amqpAdmin);
    return directExchange;
  }

  // Bindings
  @Bean
  @Qualifier("OS_B")
  public Binding outcomePreprocessorBinding(@Qualifier("OS_Q") Queue outcomePreprocessingQueue,
      @Qualifier("OS_E") DirectExchange outcomePreprocessingExchange) {
    Binding binding = BindingBuilder.bind(outcomePreprocessingQueue).to(outcomePreprocessingExchange)
        .with(OUTCOME_PREPROCESSING_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin);
    return binding;
  }

  // Listener Adapter
  @Bean
  @Qualifier("OS_L")
  @Transactional(propagation = Propagation.NEVER)
  public MessageListenerAdapter outcomePreprocessingListenerAdapter(OutcomePreprocessingReceiver receiver,
      @Qualifier("OS_MC") MessageConverter mc) {
    MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "processMessage");
    messageListenerAdapter.setMessageConverter(mc);
    return messageListenerAdapter;
  }

  // Message Listener
  @Bean
  @Qualifier("OS_LC")
  public SimpleMessageListenerContainer outcomePreprocessingMessageListener(ConnectionFactory connectionFactory,
      @Qualifier("OS_L") MessageListenerAdapter messageListenerAdapter,
      RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    Advice[] adviceChain = {retryOperationsInterceptor};
    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(OUTCOME_PREPROCESSING_QUEUE);
    container.setMessageListener(messageListenerAdapter);
    return container;
  }

  @Bean
  @Qualifier("OS_CM")
  public DefaultClassMapper classMapper() {
    DefaultClassMapper classMapper = new DefaultClassMapper();
    Map<String, Class<?>> idClassMapping = new HashMap<>();
    idClassMapping.put("uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome", SPGOutcome.class);
    idClassMapping.put("uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress", NewUnitAddress.class);
    idClassMapping.put("uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress", NewStandaloneAddress.class);
    classMapper.setIdClassMapping(idClassMapping);
    classMapper.setTrustedPackages("*");
    return classMapper;
  }

  @Bean
  @Qualifier("OS_MC")
  public MessageConverter jsonMessageConverter(@Qualifier("OS_CM") DefaultClassMapper cm) {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
    jsonMessageConverter.setClassMapper(cm);
    return jsonMessageConverter;
  }

  @Bean(name = "preprocessingRabbitTemplate")
  @Qualifier("OS_RT")
  public RabbitTemplate rabbitTemplate(@Qualifier("OS_MC") MessageConverter mc,
      ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(mc);
    return template;
  }
}
