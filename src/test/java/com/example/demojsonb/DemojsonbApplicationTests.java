package com.example.demojsonb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class DemojsonbApplicationTests {

	@Autowired
	PersonWithAddressJsonbObjectRepository personWithAddressJsonbObjectRepository;
	@Autowired
	PersonWithoutAddressJsonbObjectRepository personWithoutAddressJsonbObjectRepository;
	@Autowired
	private KafkaConsumer consumer;
	@Autowired
	private KafkaProducer producer;
	@Autowired
	ObjectMapper objectMapper;
	@Value("${test.topic}")
	private String topic;

	@Test
	public void savingPersonWithAddressJsonbObject_canBeRetrievedAsWithoutAddressJsonbObject() throws Exception {
		personWithAddressJsonbObjectRepository.save(PersonWithAddressJsonbObject.builder()
				.pid(1)
				.firstname("first1")
				.lastname("last1")
				.address(Address.builder()
						.streetno("streetno1")
						.streetname("streetname1")
						.build())
				.build());

		PersonWithoutAddressJsonbObject person = personWithoutAddressJsonbObjectRepository.findById(1).get();
		assertThat(person.getFirstname(), equalTo("first1"));
		assertThat(person.getLastname(), equalTo("last1"));
		Address address = objectMapper.readValue(person.getAddress(), Address.class);
		assertThat(address.getStreetname(), equalTo("streetname1"));
		assertThat(address.getStreetno(), equalTo("streetno1"));

		personWithAddressJsonbObjectRepository.deleteAll();
	}

	@Test
	public void savingPersonWithoutAddressJsonbObject_canBeRetrievedAsWithAddressJsonbObject() throws Exception {
		personWithoutAddressJsonbObjectRepository.save(PersonWithoutAddressJsonbObject.builder()
				.pid(1)
				.firstname("first1")
				.lastname("last1")
				.address("{\n" +
						"  \"streetno\": \"streetno1\",\n" +
						"  \"streetname\": \"streetname1\"\n" +
						"}")
				.build());

		PersonWithAddressJsonbObject person = personWithAddressJsonbObjectRepository.findById(1).get();
		assertThat(person.getFirstname(), equalTo("first1"));
		assertThat(person.getLastname(), equalTo("last1"));
		assertThat(person.getAddress().getStreetname(), equalTo("streetname1"));
		assertThat(person.getAddress().getStreetno(), equalTo("streetno1"));

		personWithAddressJsonbObjectRepository.deleteAll();
	}

	@Test
	public void sendingPersonWithoutAddressJsonbObjectToKafkaTopic_canBeRetrievedAsWithAddressJsonbObject() throws Exception {
		personWithoutAddressJsonbObjectRepository.save(PersonWithoutAddressJsonbObject.builder()
				.pid(1)
				.firstname("first1")
				.lastname("last1")
				.address("{\n" +
						"  \"streetno\": \"streetno1\",\n" +
						"  \"streetname\": \"streetname1\"\n" +
						"}")
				.build());

		PersonWithoutAddressJsonbObject person = personWithoutAddressJsonbObjectRepository.findById(1).get();
		producer.send(topic, person);

		Thread.sleep(3000); // sleep 3s is ugly but does the job
		PersonWithAddressJsonbObject retrieved = consumer.getPayload();
		assertThat(retrieved.getFirstname(), equalTo("first1"));
		assertThat(retrieved.getLastname(), equalTo("last1"));
		assertThat(retrieved.getAddress().getStreetname(), equalTo("streetname1"));
		assertThat(retrieved.getAddress().getStreetno(), equalTo("streetno1"));

		personWithAddressJsonbObjectRepository.deleteAll();
	}
}

